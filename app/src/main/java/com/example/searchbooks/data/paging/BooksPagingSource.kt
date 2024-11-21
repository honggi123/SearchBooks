package com.example.searchbooks.data.paging

import android.util.Log
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
            Log.e("exception", e.message.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkBook>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // 새로고침할 때 이전 페이지를 보여줌
            // TODO 주석
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
