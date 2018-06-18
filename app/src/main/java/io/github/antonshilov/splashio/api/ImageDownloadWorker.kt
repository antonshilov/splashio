package io.github.antonshilov.splashio.api

import android.app.WallpaperManager
import androidx.work.Data
import androidx.work.Worker
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
class ImageDownloadWorker : Worker() {

  val client = provideOkHttpClient()

  override fun doWork(): WorkerResult {
    // input data parsing and validation
    val photoUrl = inputData.getString(PHOTO_URL, "")
    if (photoUrl.isBlank()) return WorkerResult.FAILURE

    val request = Request.Builder()
        .url(photoUrl)
        .build()

    try {
      Timber.d("Execute Load Image")
      val response = client.newCall(request).execute()!!
      if (response.isSuccessful) {
        Timber.d("Load Success")
        // TODO use external intent so set a wallpaper
        WallpaperManager.getInstance(applicationContext).setStream(response.body()!!.byteStream())
        Timber.d("Image is successfully set as wallpaper")
      }
    } catch (e: IOException) {
      Timber.e(e)
      return WorkerResult.FAILURE
    }


    return WorkerResult.SUCCESS

  }

  /**
   * Saves image from the [response] to the internal storage
   * @param response response to the image download request
   */
  private fun saveToInternalStorage(response: Response) {
    // https://stackoverflow.com/a/35381424/4998244
    val file = File(applicationContext.filesDir, "testImage.jpg")
    Timber.d("%s file created", file.absolutePath)
    val sink = Okio.buffer(Okio.sink(file))
    sink.writeAll(response.body()!!.source())
    Timber.d("File saved")
    sink.close()
    response.body()!!.close()
//    val intent = Intent(Intent.ACTION_ATTACH_DATA)
//    intent.setDataAndType(file.toUri(), "image/*")
//    intent.putExtra("jpg", "image/*")
//    applicationContext.startActivity(Intent.createChooser(intent, "Set wallpaper"))
  }

  companion object {
    const val PHOTO_URL = "photo"
    fun bundleInput(photo: Photo): Data {
      return Data.Builder()
          .putString(PHOTO_URL, photo.urls.raw)
          .build()
    }
  }
}