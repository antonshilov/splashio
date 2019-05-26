package io.github.antonshilov.splashio.ui.fullscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.work.State
import androidx.work.WorkManager
import io.github.antonshilov.splashio.api.ImageDownloadWorker
import io.github.antonshilov.splashio.api.model.Photo
import timber.log.Timber

class FullscreenImageViewModel : ViewModel() {
  lateinit var wallpaperLoadStatus: LiveData<State>
  fun setWallpaper(photo: Photo) {
    val workManager = WorkManager.getInstance()
    Timber.d("Set %s as wallpaper", photo.urls.raw)

    val work = ImageDownloadWorker.createWork(photo)

    wallpaperLoadStatus = Transformations.map(workManager.getStatusById(work.id)) {
      it.state
    }
    workManager.enqueue(work)
  }
}
