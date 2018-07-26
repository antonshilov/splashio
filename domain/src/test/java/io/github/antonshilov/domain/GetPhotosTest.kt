package io.github.antonshilov.domain

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.model.Photo
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class GetPhotosTest {
  private lateinit var getPhotos: GetPhotos
  @Mock private lateinit var repo: PhotoRepo
  @Mock private lateinit var postExecutionThread: PostExecutionThread

  @Before
  fun setup() {
    getPhotos = GetPhotos(repo, postExecutionThread)
    stubRepo(Observable.just(PhotoDataFactory.createPhotoList(10)))

  }

  @Test
  fun `get photos completes`() {
    val observer = getPhotos.buildObservable().test()
    observer.assertComplete()
  }

  @Test
  fun `get photos calls repo`() {
    getPhotos.buildObservable().test()
    verify(repo).getLatestPhotos()
  }

  @Test
  fun `get photos returns photos`() {
    val photos = PhotoDataFactory.createPhotoList(10)
    stubRepo(Observable.just(photos))
    val observer = getPhotos.buildObservable().test()
    observer.assertValue(photos)
  }

  private fun stubRepo(observable: Observable<List<Photo>>) {
    whenever(repo.getLatestPhotos())
      .thenReturn(observable)
  }

}

object PhotoDataFactory {
  private val random = Random()
  private val intRange = Int.MAX_VALUE
  private fun randomUuid() = UUID.randomUUID().toString()
  private fun randomInt() = random.nextInt(intRange)
  fun createPhoto() = Photo(randomUuid(), randomInt(), randomInt())
  fun createPhotoList(count: Int): List<Photo> {
    val photos = mutableListOf<Photo>()
    repeat(count) {
      photos.add(createPhoto())
    }
    return photos
  }
}