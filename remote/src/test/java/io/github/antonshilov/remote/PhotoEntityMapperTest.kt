package io.github.antonshilov.remote

import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.mapper.PhotoLinksMapper
import io.github.antonshilov.remote.mapper.ProfileImageMapper
import io.github.antonshilov.remote.mapper.UrlMapper
import io.github.antonshilov.remote.mapper.UserEntityMapper
import io.github.antonshilov.remote.mapper.UserLinksMapper
import io.github.antonshilov.remote.model.PhotoModel
import io.github.benas.randombeans.api.EnhancedRandom.random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PhotoEntityMapperTest {

  private val mapper = PhotoEntityMapper(
    UserEntityMapper(ProfileImageMapper(), UserLinksMapper()), UrlMapper(),
    PhotoLinksMapper()
  )

  @Test
  fun `should map model to entity`() {
    val model = random(PhotoModel::class.java)
    val entity = mapper.mapFromRemote(model)

    assertEquals(model.id, entity.id)
    assertEquals(model.width, entity.width)
    assertEquals(model.height, entity.height)
  }
}
