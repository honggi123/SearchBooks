package com.example.searchbooks.feature.bookdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.searchbooks.R
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.exception.AppException
import com.example.searchbooks.feature.component.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        },
        modifier = modifier
    ) { paddingValues ->
        when (val state = uiState) {
            is BookDetailUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier.fillMaxSize()
                )
            }

            is BookDetailUiState.Success -> {
                DetailScreenContent(
                    book = (uiState as BookDetailUiState.Success).book,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is BookDetailUiState.Error -> {
                when (state.exception) {
                    is AppException.NetworkException -> {
                        ErrorContent(
                            "네트워크 연결아 끊겼습니다.",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    else -> {
                        ErrorContent(
                            "오류가 발생했습니다. 다시시도 해주세요.",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back), // Drawable 아이콘 리소스
            contentDescription = "뒤로가기",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "책 정보",
            maxLines = 1,
            modifier = Modifier.width(100.dp)
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(20.dp), strokeWidth = 3.dp
        )
    }
}

@Composable
private fun ErrorContent(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage)
    }
}

@Composable
private fun DetailScreenContent(
    book: Book,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageLoader(
            imageUrl = book.imageUrl,
            context = LocalContext.current,
            modifier = Modifier.size(200.dp, 300.dp),
        )
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        book.publisher?.let {
            Text(
                text = book.publisher,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}