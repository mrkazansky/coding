package com.kz.search_presentation.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import coil.compose.AsyncImage
import com.kz.search_domain.model.Product
import com.kz.search_presentation.R
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CartScreen() {
    val viewModel = koinViewModel<CartViewModel>()
    val products = viewModel.products.collectAsLazyPagingItems()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.title_search_screen))
        }, navigationIcon = {
            IconButton(onClick = { viewModel.showClearAllConfirmationDialog(true) }) {
                Icon(Icons.Default.Refresh, contentDescription = "", tint = Color.White)
            }
        })
    }) {
        Column(
            modifier = Modifier.padding(it), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CartList(
                products = products,
                modifier = Modifier.fillMaxSize(),
                onDecreaseTap = { product -> viewModel.decreaseProductQuantity(product, false) },
                onIncreaseTap = viewModel::increaseProductQuantity
            )
        }
    }

    val showClearAllConfirmationDialog by viewModel.showClearAllConfirmationDialog.collectAsState()
    if (showClearAllConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showClearAllConfirmationDialog(false) },
            title = { Text("Are you sure you want to clear this?") },
            text = { Text("This action cannot be undone") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearAll() }) {
                    Text("Delete it".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showClearAllConfirmationDialog(false) }) {
                    Text("Cancel".uppercase())
                }
            },
        )
    }

    val showClearProductConfirmationDialog by viewModel.showClearProductConfirmationDialog.collectAsState()
    if (showClearProductConfirmationDialog.second) {
        AlertDialog(
            onDismissRequest = {
                viewModel.showClearProductConfirmationDialog(show = false)
            },
            title = { Text("Are you sure you want to clear ${showClearProductConfirmationDialog.first?.title ?: "this"}?") },
            text = { Text("This action cannot be undone") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.decreaseProductQuantity(
                        showClearProductConfirmationDialog.first!!,
                        true
                    )
                }) {
                    Text("Delete it".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.showClearProductConfirmationDialog(show = false)
                }) {
                    Text("Cancel".uppercase())
                }
            },
        )
    }

}

@Composable
fun CartList(
    products: LazyPagingItems<Product>,
    modifier: Modifier,
    onIncreaseTap: (Product) -> Unit,
    onDecreaseTap: (Product) -> Unit
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(products.itemCount, null, products.itemContentType { it::class }) {
            val product = products[it] ?: return@items
            CartItem(
                product = product, modifier = modifier, onDecreaseTap, onIncreaseTap
            )
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    modifier: Modifier,
    onDecreaseTap: (Product) -> Unit,
    onIncreaseTap: (Product) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), elevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = product.title)
                Text(text = "Price:  ${product.price}")
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .border(ButtonDefaults.outlinedBorder)
                        .clip(
                            RoundedCornerShape(4.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDecreaseTap(product) }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "")
                    }
                    Text(text = product.quantity.toString())
                    IconButton(onClick = { onIncreaseTap(product) }) {
                        Icon(Icons.Default.KeyboardArrowRight, "")
                    }
                }
            }
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        }
    }
}