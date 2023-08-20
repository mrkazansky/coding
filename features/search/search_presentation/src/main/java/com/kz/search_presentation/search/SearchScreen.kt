package com.kz.search_presentation.search

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import coil.compose.AsyncImage
import com.kz.core.compose.rememberFlowWithLifecycle
import com.kz.search_domain.model.Product
import com.kz.search_presentation.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SearchScreen(viewModelStore: ViewModelStoreOwner? = null) {
    val viewModel =
        if (viewModelStore != null) koinViewModel<SearchViewModel>(viewModelStoreOwner = viewModelStore) else koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val products = remember {
        viewModel.state.map { it.products }
    }.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.sideEffect.collect {
            when (it){
                is SearchSideEffect.NotifyProductAdded -> {
                    Toast.makeText(context, "Added ${it.product.title} to cart", LENGTH_SHORT).show()
                }
            }
        }
    })

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.title_search_screen))
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("txtSearch"),
                maxLines = 1,
                placeholder = {
                    Text(text = stringResource(id = R.string.title_search_screen))
                }
            )
            SearchList(
                products = products,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("item_list")
            ) { product ->
                viewModel.addToCart(product)
            }
        }
    }

}

@Composable
fun SearchList(
    products: LazyPagingItems<Product>,
    modifier: Modifier,
    onAddToCart: (Product) -> Unit
) {
    val state = rememberLazyListState()
    LazyColumn(modifier = modifier) {
        items(products.itemCount, null, products.itemContentType { it::class }) {
            val product = products[it] ?: return@items
            SearchItem(
                product = product,
                modifier = Modifier,
                onAddToCartClick = { onAddToCart(product) }
            )
        }
    }
    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            state.firstVisibleItemIndex
            state.firstVisibleItemScrollOffset
        }
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchItem(product: Product, modifier: Modifier, onAddToCartClick: () -> Unit) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = product.title)
                Text(text = "Price:  ${product.price}")
            }
            val pagerState = rememberPagerState {
                product.images.size
            }
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .padding(8.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                ) { page ->
                    Card(
                        Modifier
                            .fillMaxSize()
                    ) {
                        val image = product.images.getOrNull(page) ?: return@Card
                        AsyncImage(
                            model = image,
                            contentDescription = product.title,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.White, CircleShape),
                    onClick = { onAddToCartClick() }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "")
                }
            }
            var pageKey by remember { mutableIntStateOf(0) }

            val effectFlow = rememberFlowWithLifecycle(pagerState.interactionSource.interactions)

            LaunchedEffect(effectFlow) {
                effectFlow.collectLatest {
                    if (it is DragInteraction.Stop) pageKey++
                }
            }

            LaunchedEffect(pageKey) {
                delay(2500)
                val newPage = (pagerState.currentPage + 1) % product.images.size
                pagerState.animateScrollToPage(newPage)
                pageKey++
            }
        }
    }
}