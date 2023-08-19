package com.example.standardarchitect.navigation

import androidx.navigation.NavController
import com.kz.search_presentation.destinations.CartScreenDestination
import com.kz.search_presentation.destinations.SearchScreenDestination
import com.kz.search_presentation.navigation.SearchNavigation
import com.ramcosta.composedestinations.navigation.navigate

class AppNavigator(
    private val navController: NavController
) : HomeNavigation , SearchNavigation {

    override fun gotoSearchTab() {
        navController.navigate(SearchScreenDestination.invoke())
    }

    override fun gotoFavoriteTab() {
        navController.navigate(CartScreenDestination)
    }

    override fun openConfirmationDialog() {
    }

}