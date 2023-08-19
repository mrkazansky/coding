package com.example.standardarchitect.navigation

import com.kz.search_presentation.destinations.CartScreenDestination
import com.kz.search_presentation.destinations.SearchScreenDestination
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object RootNavGraph {


    val search = object : NavGraphSpec {
        override val route = "home"
        override val startRoute = SearchScreenDestination
        override val destinationsByRoute =
            listOf<DestinationSpec<*>>(SearchScreenDestination, CartScreenDestination)
                .associateBy { it.route }
    }

    val root = object : NavGraphSpec {
        override val route = "root"

        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val startRoute = search

        override val nestedNavGraphs: List<NavGraphSpec> = listOf(search)
    }

}

fun DependenciesContainerBuilder<*>.currentNavigator(): AppNavigator {
    return AppNavigator(navController = navController)
}
