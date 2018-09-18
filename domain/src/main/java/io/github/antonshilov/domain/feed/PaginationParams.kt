package io.github.antonshilov.domain.feed

data class PaginationParams(val page: Int = 1, val pageSize: Int = 50) {
  val nextPage: Int
    get() = page + 1
}
