package io.github.antonshilov.remote

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.antonshilov.domain.model.Photo
import io.github.antonshilov.remote.factory.PhotoFactory
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.model.PhotoModel
import io.github.antonshilov.remote.service.UnsplashApi
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PhotoRemoteImplTest {
  private val mapper = mock<PhotoEntityMapper>()
  private val api = mock<UnsplashApi>()
  private val remote = PhotoRemoteImpl(api, mapper)

  @Test
  fun `get photos completes`() {
    stubPhotoList(Observable.just(PhotoFactory.makePhotoList()))
    stubMapper(any(), PhotoFactory.makePhotoEntity())

    val testObserver = remote.getLatestPhotos().test()
    testObserver.assertComplete()
  }

  @Test
  fun `get photos calls server`() {
    stubPhotoList(Observable.just(PhotoFactory.makePhotoList()))
    stubMapper(any(), PhotoFactory.makePhotoEntity())

    remote.getLatestPhotos().test()
    verify(api).getLatestPhotos()
  }

  private fun stubPhotoList(observable: Observable<List<PhotoModel>>) {
    whenever(api.getLatestPhotos())
      .thenReturn(observable)
  }

  private fun stubMapper(model: PhotoModel, entity: Photo) {
    whenever(mapper.mapFromRemote(model))
      .thenReturn(entity)
  }
}
