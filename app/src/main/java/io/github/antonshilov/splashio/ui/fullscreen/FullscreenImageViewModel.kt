package io.github.antonshilov.splashio.ui.fullscreen

import android.arch.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.github.antonshilov.splashio.api.ImageDownloadWorker
import io.github.antonshilov.splashio.api.model.Photo
import timber.log.Timber

class FullscreenImageViewModel : ViewModel() {
  fun setWallpaper(photo: Photo) {
    Timber.d("Set %s as wallpaper", photo.urls.raw)
    val data = ImageDownloadWorker.bundleInput(photo)
    val imageWork = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
        .setInputData(data)
        .build()
    WorkManager.getInstance().enqueue(imageWork)
  }
}