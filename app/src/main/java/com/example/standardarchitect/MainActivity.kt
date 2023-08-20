@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.standardarchitect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.example.standardarchitect.navigation.AppNavigator
import com.example.standardarchitect.navigation.HomeNavigation
import com.example.standardarchitect.navigation.RootNavGraph
import com.kz.core.theme.StandardArchitectTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StandardArchitectTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                    "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
                }
                val navigation = AppNavigator(navController = navController)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    bottomBar = { HomeBottomBar(navigation) }
                ) {
                    DestinationsNavHost(
                        modifier = Modifier
                            .padding(it)
                            .semantics {
                                testTagsAsResourceId = true
                            },
                        navController = navController,
                        navGraph = RootNavGraph.root,
                        dependenciesContainerBuilder = {
                            dependency(scaffoldState)
                            dependency(navigation)
                            dependency(viewModelStoreOwner)
                        },
                    )

                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(navigation: HomeNavigation) {
    val selected = remember { mutableIntStateOf(0) }
    BottomNavigation {
        BottomNavigationItem(
            selected = selected.intValue == 0,
            onClick = {
                selected.intValue = 0
                navigation.gotoSearchTab()
            },
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.bottom_search)
                )
            },
            label = { Text(stringResource(R.string.bottom_search)) }
        )
        BottomNavigationItem(
            selected = selected.intValue == 1,
            onClick = {
                selected.intValue = 1
                navigation.gotoFavoriteTab()
            },
            icon = {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = stringResource(R.string.bottom_cart)
                )
            },
            label = { Text(stringResource(R.string.bottom_cart)) }
        )
    }
}
