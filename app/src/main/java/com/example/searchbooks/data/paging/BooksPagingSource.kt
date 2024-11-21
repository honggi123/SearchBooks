package com.example.searchbooks.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchbooks.data.network.GoogleService
import com.example.searchbooks.data.network.response.NetworkBook


private const val INITIAL_PAGE = 1

class BooksPagingSource(
    private val service: GoogleService,
    private val searchQuery: String,
) : PagingSource<Int, NetworkBook>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkBook> {
        return try {
            val page = params.key ?: INITIAL_PAGE

            val response = service.getBooksByQuery(
                searchQuery = searchQuery,
                start = page,
            )

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkBook>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // 새로고침을 처리할 때, 가장 가까운 이전 페이지(prevKey)를 기준으로 새 데이터를 로드합니다.
            // prevKey가 null인 경우, PagingSource는 기본 초기 키를 사용합니다.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
