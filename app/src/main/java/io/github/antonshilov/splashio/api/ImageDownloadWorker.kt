package io.github.antonshilov.splashio.api

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.ImageDownloadWorker.Companion.bundleInput
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.di.provideOkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * [ImageDownloadWorker] downloads a file from url passed to [bundleInput]
 * and saves it to the internal application storage.
 */
class ImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

  private val client = provideOkHttpClient()
  private val notificationManager by lazy { ImageDownloadNotificationManager(applicationContext) }
  /**
   * Downloads the image from the url which was passed in the parameters
   * Then sets that image as a wallpaper using [WallpaperManager]
   * Displays progress notification while downloading
   */
  override fun doWork(): Result {
    // input data parsing and validation
    val photoUrl = inputData.getString(PHOTO_URL)
    if (photoUrl.isNullOrBlank()) return Result.FAILURE

    notificationManager.start()
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    val request = Request.Builder()
      .url(photoUrl)
      .build()
    try {
      Timber.d("Execute Load Image")
      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        Timber.d("Load Success")

        val wallpaperFile = saveToInternalStorage(response)
        // todo fix android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: files._data (code 2067)
        val uri = applicationContext.getImageContentUri(wallpaperFile.absolutePath)
        applicationContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        sendSetWallpaperIntent(uri!!)
        Timber.d("Image is successfully set as wallpaper")
      }
    } catch (e: IOException) {
      Timber.e(e)
      notificationManager.stop()
      return Result.FAILURE
    }

    notificationManager.stop()
    return Result.SUCCESS
  }

  /**
   * Saves image from the [response] to the internal storage
   * @param response response to the image download request
   */
  private fun saveToInternalStorage(response: Response): File {
    // https://stackoverflow.com/a/35381424/4998244
    val file = getPrivateAlbumStorageDir(applicationContext)
    Timber.d("%s file created", file.absolutePath)
    val sink = Okio.buffer(Okio.sink(file))
    sink.writeAll(response.body()!!.source())
    Timber.d("File saved")
    sink.close()
    response.body()!!.close()
    return file
  }

  fun Context.getImageContentUri(absPath: String): Uri? {
    val cursor = contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      arrayOf(MediaStore.Images.Media._ID),
      MediaStore.Images.Media.DATA + "=? ",
      arrayOf(absPath),
      null
    )

    val result = if (cursor != null && cursor.moveToFirst()) {
      val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
      cursor.close()
      Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id))
    } else if (!absPath.isEmpty()) {
      val values = ContentValues()
      values.put(MediaStore.Images.Media.DATA, absPath)
      contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
      )
    } else {
      null
    }
    cursor?.close()
    return result
  }

  private fun sendSetWallpaperIntent(wallpaperUri: Uri) {
    try {
      Timber.d("Crop and Set:$wallpaperUri")
      val wallpaperIntent = WallpaperManager.getInstance(applicationContext).getCropAndSetWallpaperIntent(wallpaperUri)
      wallpaperIntent.setDataAndType(wallpaperUri, "image/*")
      wallpaperIntent.putExtra("mimeType", "image/*")
      applicationContext.startActivity(wallpaperIntent)
    } catch (e: Exception) {
      Timber.e(e)
      Timber.d("Chooser: $wallpaperUri")
      val wallpaperIntent = Intent(Intent.ACTION_ATTACH_DATA)
      wallpaperIntent.setDataAndType(wallpaperUri, "image/*")
      wallpaperIntent.putExtra("mimeType", "image/*")
      wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT)
      wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
      applicationContext.startActivity(Intent.createChooser(wallpaperIntent, "Set wallpaper"))
    }
  }

  private fun getPrivateAlbumStorageDir(context: Context): File {
    // Get the directory for the app's private pictures directory.
    return File(context.getExternalFilesDir("pictures"), "testImage.jpg")
  }

  companion object {
    const val PHOTO_URL = "photo"
    /**
     * Puts [photo] to the data bundle for worker
     *
     * @return [Data] with the bundled input
     */
    fun bundleInput(photo: Photo): Data {
      return Data.Builder()
        .putString(PHOTO_URL, photo.urls.full)
        .build()
    }

    /**
     * Creates [WorkRequest] for the [ImageDownloadWorker]
     */
    fun createWork(photo: Photo): OneTimeWorkRequest {
      val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
      val data = bundleInput(photo)
      return OneTimeWorkRequestBuilder<ImageDownloadWorker>()
        .setInputData(data)
        .setConstraints(constraints)
        .build()
    }
  }

  /**
   * Handles the notification operations for the [ImageDownloadWorker]
   */
  internal class ImageDownloadNotificationManager(val context: Context) {

    private val CHANNEL_ID = "downloadStatus"

    private val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.stat_sys_download)
      .setProgress(0, 0, true)
      .setContentTitle(context.getString(R.string.notification_set_wallpaper_progress))
      .build()

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
      createNotificationChannel()
    }

    /**
     * Initialize the [NotificationChannel] for the Oreo and higher to publish download notifications
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
          CHANNEL_ID, context.getString(R.string.download_channel_name),
          NotificationManager.IMPORTANCE_LOW
        )
        channel.description = context.getString(R.string.download_channel_description)
        val oreoNotificationManager = context.getSystemService(NotificationManager::class.java)
        oreoNotificationManager.createNotificationChannel(channel)
      }
    }

    /**
     * Displays a indeterminate progress notification with animated download icon
     */
    fun start() {
      notificationManager.notify(WALLPAPER_NOTIFICATION_ID, notification)
    }

    /**
     * Cancels the notification that has been displayed in [start]
     */
    fun stop() {
      notificationManager.cancel(WALLPAPER_NOTIFICATION_ID)
    }

    companion object {
      private const val WALLPAPER_NOTIFICATION_ID = 16
    }
  }
}
