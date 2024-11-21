package com.example.searchbooks.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.feature.component.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBookClick: (Book) -> Unit,
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
                    onBookClick = onBookClick,
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
    onBookClick: (Book) -> Unit,
    pagingItems: LazyPagingItems<Book>,
    modifier: Modifier = Modifier
) {
    val isEmpty =
        pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading

    if (isEmpty) {
        NoSearchedContent(
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(pagingItems.itemCount) { index ->
                Column {
                    pagingItems[index]?.let {
                        BookItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onBookClick(it) }
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
    val authorsString = book.authors?.joinToString(separator = ", ")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ImageLoader(
            imageUrl = book.smallImageUrl,
            context = LocalContext.current,
            modifier = Modifier.size(60.dp, 100.dp)
        )
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        authorsString?.let {
            Text(
                text = "저자 : ${authorsString}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        book.publisher?.let { Text(text = "출판사 : ${it}") }
    }
}



