package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

//deeplink external
internal const val uri = "alura://panucci.com.br/"

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = homeGraphRoute
    ) {
        homeGraph(
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
            onNavigateToProductDetails = { product ->
                navController.navigateToProductDetails(product.id)
            },
        )
        productDetailsScreen(
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
            onPopBackStack = {
                navController.navigateUp()
            },
        )
        checkoutScreen(
            onPopBackStack = {
                //if you change position the call, all time you enter in this flow where you maked you order
                //the snackbar is presentation because the message don't removed after read
                //despite the order had send the message is show when you navigate on app
                navController.navigateUp()
                //using navCntroller to set savedStateHandle and add message
                //this message referred to send order success
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("order_done","Pedido feito com sucesso")
            },
        )
    }

}
