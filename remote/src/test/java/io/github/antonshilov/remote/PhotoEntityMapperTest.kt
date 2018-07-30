package io.github.antonshilov.remote

import io.github.antonshilov.remote.factory.PhotoFactory.makePhotoModel
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PhotoEntityMapperTest {

  private val mapper = PhotoEntityMapper()

  @Test
  fun `should map model to entity`() {
    val model = makePhotoModel()
    val entity = mapper.mapFromRemote(model)

    assertEquals(model.id, entity.id)
    assertEquals(model.width, entity.width)
    assertEquals(model.height, entity.height)

  }
}