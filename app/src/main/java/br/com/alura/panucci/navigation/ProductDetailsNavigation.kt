package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel

private const val productDetailsRoute = "productDetails"
const val productIdArgument = "productId"
const val promoCodeArgument = "promoCode"

//alura://panucci.com.br/productDetails/9adccd9a-3918-4996-8c96-2f5b9143cef2?promoCode=PANUCCI10
fun NavGraphBuilder.productDetailsScreen(
    onNavigateToCheckout: () -> Unit,
    onPopBackStack: () -> Unit
) {
    composable(
        "$productDetailsRoute/{$productIdArgument}",
        deepLinks = listOf(navDeepLink { uriPattern = "$uri$productDetailsRoute/{$productIdArgument}?$promoCodeArgument={$promoCodeArgument}" })
    ) { backStackEntry ->
        backStackEntry.arguments?.getString(productIdArgument)?.let { id ->

            //add factory to build view model with this pattern
            val viewModel = viewModel<ProductDetailsViewModel>(factory = ProductDetailsViewModel.Factory)
            val uiState by viewModel.uiState.collectAsState()

//            LaunchedEffect(Unit) {
//                viewModel.findProductById(id)
//            }

            ProductDetailsScreen(
                uiState = uiState,
                onOrderClick = onNavigateToCheckout,
                onTryFindProductAgainClick = {
                    viewModel.findProductById(id)
                },
                onBackClick = onPopBackStack
            )
        } ?: LaunchedEffect(Unit) {
            onPopBackStack()
        }
    }
}

fun NavController.navigateToProductDetails(id: String){
    navigate("$productDetailsRoute/$id")
}