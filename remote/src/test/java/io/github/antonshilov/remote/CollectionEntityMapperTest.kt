package io.github.antonshilov.remote

import io.github.antonshilov.remote.mapper.CollectionEntityMapper
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.mapper.PhotoLinksMapper
import io.github.antonshilov.remote.mapper.ProfileImageMapper
import io.github.antonshilov.remote.mapper.UrlMapper
import io.github.antonshilov.remote.mapper.UserEntityMapper
import io.github.antonshilov.remote.mapper.UserLinksMapper
import io.github.antonshilov.remote.model.CollectionModel
import io.github.benas.randombeans.api.EnhancedRandom

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CollectionEntityMapperTest {

  private val userMapper = UserEntityMapper(ProfileImageMapper(), UserLinksMapper())
  private val photoMapper = PhotoEntityMapper(userMapper, UrlMapper(), PhotoLinksMapper())
  private val collectionMapper = CollectionEntityMapper(photoMapper)

  @Test
  fun `should map model to entity`() {
    val model = EnhancedRandom.random(CollectionModel::class.java)
    val entity = collectionMapper.mapFromRemote(model)

    Assert.assertEquals(model.id, entity.id)
    Assert.assertEquals(model.description, entity.description)
    Assert.assertEquals(model.previewPhotos?.map{ it.urls.small }, entity.previewPhotosUrls)
    Assert.assertEquals(model.user?.name, entity.userName)
  }
}
