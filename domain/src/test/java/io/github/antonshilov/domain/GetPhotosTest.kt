package io.github.antonshilov.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.photos.GetPhotos
import io.github.antonshilov.domain.feed.photos.model.Links
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.domain.feed.photos.model.ProfileImage
import io.github.antonshilov.domain.feed.photos.model.Urls
import io.github.antonshilov.domain.feed.photos.model.User
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random
import java.util.UUID

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
    stubRepo(Observable.just(PhotoDataFactory.createPhotoList(10)))
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
    val photos = PhotoDataFactory.createPhotoList(10)
    stubRepo(Observable.just(photos))
    val observer = getPhotos.buildObservable(defaultParams).test()
    observer.assertValue(photos)
  }

  private fun stubRepo(observable: Observable<List<Photo>>) {
    whenever(repo.getLatestPhotos(any(), any()))
      .thenReturn(observable)
  }
}

abstract class BaseDataFactory {
  private val random = Random()
  private val intRange = Int.MAX_VALUE
  fun randomString() = UUID.randomUUID().toString()
  fun randomInt() = random.nextInt(intRange)
  fun randomBool() = random.nextBoolean()
}

object PhotoDataFactory : BaseDataFactory() {
  fun createPhoto() =
    Photo(
      randomString(),
      randomInt(),
      randomInt(),
      randomString(),
      randomInt(),
      randomBool(),
      randomString(),
      UserDataFactory.createUser(),
      createUrls(),
      UserDataFactory.createLinks()
    )

  fun createPhotoList(count: Int): List<Photo> {
    val photos = mutableListOf<Photo>()
    repeat(count) {
      photos.add(createPhoto())
    }
    return photos
  }

  private fun createUrls() = Urls(randomString(), randomString(), randomString(), randomString(), randomString())
}

object UserDataFactory : BaseDataFactory() {
  fun createUser() = User(
    randomString(),
    randomString(),
    randomString(),
    randomString(),
    randomString(),
    randomString(),
    randomInt(),
    randomInt(),
    randomInt(),
    createProfileImage(),
    createLinks()
  )

  private fun createProfileImage() = ProfileImage(randomString(), randomString(), randomString())
  fun createLinks() = Links(randomString(), randomString(), randomString(), randomString(), randomString())
}
