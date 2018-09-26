package io.github.antonshilov.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.photos.GetPhotos
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.benas.randombeans.api.EnhancedRandom.randomListOf

import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetPhotosTest {
  private lateinit var getPhotos: GetPhotos
  @Mock
  private lateinit var repo: PhotoRepo
  @Mock
  private lateinit var postExecutionThread: PostExecutionThread

  private val defaultParams = PaginationParams()

  @Before
  fun setup() {
    getPhotos = GetPhotos(repo, postExecutionThread)
    stubRepo(Observable.just(randomListOf(10, Photo::class.java)))
  }

  @Test
  fun `get photos completes`() {
    val observer = getPhotos.buildObservable(defaultParams).test()
    observer.assertComplete()
  }

  @Test
  fun `get photos calls repo`() {
    getPhotos.buildObservable(defaultParams).test()
    verify(repo).getLatestPhotos(defaultParams.page, defaultParams.pageSize)
  }

  @Test
  fun `get photos returns photos`() {
    val photos = randomListOf(10, Photo::class.java)
    stubRepo(Observable.just(photos))
    val observer = getPhotos.buildObservable(defaultParams).test()
    observer.assertValue(photos)
  }

  private fun stubRepo(observable: Observable<List<Photo>>) {
    whenever(repo.getLatestPhotos(any(), any()))
      .thenReturn(observable)
  }
}
