package com.example.searchbooks.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.feature.component.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val books = viewModel.books.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(paddingValues)
        ) {
            SearchInputField(
                searchQuery = searchQuery.value,
                onSearchTriggered = viewModel::onSearchTriggered,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                thickness = 2.dp,
            )
            if (searchQuery.value == null) {
                NotYetSearchedContent(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                SearchScreenContent(
                    pagingItems = books,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchInputField(
    searchQuery: String?,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery ?: "",
        onValueChange = onSearchTriggered,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun NotYetSearchedContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "검색어를 입력해주세요.")
    }
}

@Composable
private fun SearchScreenContent(
    pagingItems: LazyPagingItems<Book>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(pagingItems.itemCount) { index ->
            Column {
                pagingItems[index]?.let {
                    BookItem(
                        book = it, modifier = Modifier.fillMaxWidth()
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
private fun NoSearchedContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "검색 결과가 없습니다.")
    }
}

@Composable
private fun BookItem(
    book: Book,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier, verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ImageLoader(
            imageUrl = book.smallImageUrl,
            context = LocalContext.current,
            modifier = Modifier.size(60.dp, 100.dp)
        )
        Text(text = book.title)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            book.authors?.onEach { author ->
                Text(text = author)
            }
        }
        book.publisher?.let { Text(text = it) }
    }
}


