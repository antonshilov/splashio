package io.github.antonshilov.domain.feed.collections

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.benas.randombeans.api.EnhancedRandom
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCollectionsTest {
  private lateinit var getCollections: GetCollections
  @Mock
  private lateinit var repo: CollectionsRepo
  @Mock
  private lateinit var postExecutionThread: PostExecutionThread

  private val defaultParams = PaginationParams()

  @Before
  fun setup() {
    getCollections = GetCollections(repo, postExecutionThread)
    stubRepo(Single.just(EnhancedRandom.randomListOf(10, Collection::class.java)))
  }

  @Test
  fun completes() {
    val observer = getCollections.buildObservable(defaultParams).test()
    observer.assertComplete()
  }

  @Test
  fun `calls repo`() {
    getCollections.buildObservable(defaultParams).test()
    verify(repo).getCollections(defaultParams.page, defaultParams.pageSize)
  }

  @Test
  fun `returns collections`() {
    val collections = EnhancedRandom.randomListOf(10, Collection::class.java)
    stubRepo(Single.just(collections))
    val observer = getCollections.buildObservable(defaultParams).test()
    observer.assertValue(collections)
  }

  private fun stubRepo(single: Single<List<Collection>>) {
    whenever(repo.getCollections(any(), any()))
      .thenReturn(single)
  }
}
