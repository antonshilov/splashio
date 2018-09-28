package io.github.antonshilov.remote

import io.github.antonshilov.remote.mapper.CollectionEntityMapper
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.mapper.PhotoLinksMapper
import io.github.antonshilov.remote.mapper.ProfileImageMapper
import io.github.antonshilov.remote.mapper.UrlMapper
import io.github.antonshilov.remote.mapper.UserEntityMapper
import io.github.antonshilov.remote.mapper.UserLinksMapper
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CollectionEntityMapperTest {

  private val userMapper = UserEntityMapper(ProfileImageMapper(), UserLinksMapper())
  private val photoMapper = PhotoEntityMapper(userMapper, UrlMapper(), PhotoLinksMapper())
  private val collectionMapper = CollectionEntityMapper(photoMapper)
}
