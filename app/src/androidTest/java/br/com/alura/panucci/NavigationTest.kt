package br.com.alura.panucci

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.checkoutRoute
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsListRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.navigation.navigateToDrinks
import br.com.alura.panucci.navigation.navigateToHighlightsList
import br.com.alura.panucci.navigation.navigateToMenu
import br.com.alura.panucci.navigation.navigateToProductDetails
import br.com.alura.panucci.navigation.productDetailsRoute
import br.com.alura.panucci.navigation.productIdArgument
import br.com.alura.panucci.sampledata.sampleProducts
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PanucciApp(navController = navController)
        }
    }

    @Test
    fun appNavHost_verifyIfMenuScreenDisplayed() {
        composeTestRule.onRoot().printToLog("NAV_TEST")

        composeTestRule.onNodeWithText("Menu")
            .performClick()

        composeTestRule.onAllNodesWithText("Menu")
            .assertCountEquals(2)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, menuRoute)
    }

    @Test
    fun appNavHost_verifyIfDrinkScreenDisplayed() {
        composeTestRule.onRoot().printToLog("NAV_TEST")

        composeTestRule.onNodeWithText("Bebidas")
            .performClick()

        composeTestRule.onAllNodesWithText("Bebidas")
            .assertCountEquals(2)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, drinksRoute)
    }

    @Test
    fun appNavHost_verifyIfHighlightScreenDisplayed() {
        composeTestRule.onRoot().printToLog("NAV_TEST")


        composeTestRule.onAllNodesWithText("Destaques")
            .assertCountEquals(1)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, highlightsListRoute)
    }

    @Test
    fun appNavHost_verifyIfProductDetailsScreenIsDisplayedFromHighlightsListScreen() {
        composeTestRule.onRoot().printToLog("NAV_TEST")
        composeTestRule.onAllNodesWithContentDescription("highlight product card item")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{productId}")
    }

    @Test
    fun appNavHost_verifyFlowErrorWhenYouSelectedOneProductWithoutIdInDataBase(){
        composeTestRule.onRoot().printToLog("NAV_TEST")


        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails("anything")
        }

        composeTestRule.waitUntil(3000){

            //validamos se vai apresentar o codigo de erro
            composeTestRule.onAllNodesWithText("Falha ao buscar o produto").fetchSemanticsNodes().size == 1
        }
        //verificamos se apresentou a tela
        composeTestRule.onNodeWithText("Falha ao buscar o produto").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{productId}")
    }

    @Test
    fun appNavHost_verifyIfProductDetailsScreenIsDisplayedFromMenuScreen(){
        composeTestRule.onRoot().printToLog("NAV_TEST")
        composeTestRule.onNodeWithText("Menu").performClick()
        composeTestRule.onAllNodesWithContentDescription("menu item content")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{productId}")
    }

    @Test
    fun appNavHost_verifyIfProductDetailsScreenIsDisplayedFromDrinksScreen(){
        composeTestRule.onRoot().printToLog("NAV_TEST")
        composeTestRule.onNodeWithText("Bebidas").performClick()
        composeTestRule.onAllNodesWithContentDescription("drinks item content")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{productId}")
    }

    @Test
    fun appNavHost_verifyIfCheckoutScreenIsDisplayedFromHighlightsListScreen(){
        composeTestRule.onRoot().printToLog("panucci app")

        composeTestRule.onAllNodesWithText("Pedir")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()


        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$checkoutRoute")

    }

    @Test
    fun appNavHost_verifyIfCheckoutScreenIsDisplayedFromMenuScreen(){
        composeTestRule.onRoot().printToLog("panucci app")

        composeTestRule.onAllNodesWithText("Menu")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Floating Action Button")
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()


        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$checkoutRoute")

    }

    @Test
    fun appNavHost_verifyIfCheckoutScreenIsDisplayedFromDrinksScreen(){
        composeTestRule.onRoot().printToLog("panucci app")

        composeTestRule.onAllNodesWithText("Bebidas")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Floating Action Button")
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()


        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$checkoutRoute")

    }

    @Test
    fun appNavHost_verifyIfCheckoutScreenIsDisplayedFromProductDetails(){
        composeTestRule.onRoot().printToLog("panucci app")

        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.first().id)
        }

        composeTestRule.waitUntil(2000){
            composeTestRule.onAllNodesWithText("Pedir").fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Pedir").performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()


        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$checkoutRoute")

    }

    // Unit test
    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule.onRoot(true).printToLog("NAV_TEST")
        composeTestRule
            .onNodeWithText("Destaques do dia")
            .assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyIfSnackbarIsDisplayedWhenFinishTheOrder() {
        composeTestRule.runOnUiThread {
            navController.navigateToCheckout()
        }

        composeTestRule.onNodeWithText("Pedir")
            .performClick()

        // Com o semantics podemos utilizar tags para identificar um componente durante o test.
        composeTestRule.onNodeWithTag("PanucciSnackbar")
            .assertIsDisplayed()
    }

    /*
    * Esse teste faz as verificações necessárias e como podemos ver é grande!
    * Nos próximos começo a aplicar algumas técnicas para simplificar e reutilizar */
    @Test
    fun appNavHost_verifyIfBottomAppBarIsDisplayedOnlyInHomeGraphNavigation() {
        composeTestRule.onRoot().printToLog("panucci app")

        val bottomAppBarTag = "PanucciBottomAppBar"

        composeTestRule.runOnUiThread {
            navController.navigateToHighlightsList()
        }
        composeTestRule.onNodeWithTag(bottomAppBarTag)
            .assertIsDisplayed()

        composeTestRule.runOnUiThread {
            navController.navigateToMenu()
        }
        composeTestRule.onNodeWithTag(bottomAppBarTag)
            .assertIsDisplayed()

        composeTestRule.runOnUiThread {
            navController.navigateToDrinks()
        }
        composeTestRule.onNodeWithTag(bottomAppBarTag)
            .assertIsDisplayed()

        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.random().id)
        }
        // Foi usado o `assertDoesNotExist()`, pois o `assertIsNotDisplayed()` só funciona se achar o nó
        composeTestRule.onNodeWithTag(bottomAppBarTag)
            .assertDoesNotExist()

        composeTestRule.runOnUiThread {
            navController.navigateToCheckout()
        }
        composeTestRule.onNodeWithTag(bottomAppBarTag)
            .assertDoesNotExist()
    }


    /* Essa versão do teste já extrai a busca dos nós a partir de funções.
    * Dessa forma, podemos fazer a busca novamente do elemento esperado apenas executando a variável */
    @Test
    fun appNavHost_verifyIfFabIsDisplayedOnlyInMenuOrDrinksDestination() {
        composeTestRule.onRoot().printToLog("panucci app")

        val fabContentDescription = "Floating Action Button"

        val assertThatFabIsDisplayed = fun ComposeTestRule.() {
            onNodeWithContentDescription(fabContentDescription)
                .assertIsDisplayed()
        }
        composeTestRule.runOnUiThread {
            navController.navigateToMenu()
        }
        composeTestRule.assertThatFabIsDisplayed()

        composeTestRule.runOnUiThread {
            navController.navigateToDrinks()
        }
        composeTestRule.assertThatFabIsDisplayed()

        val assertThatFabDoesNotExist = fun ComposeTestRule.() {
            onNodeWithContentDescription(fabContentDescription)
                .assertDoesNotExist()
        }
        composeTestRule.runOnUiThread {
            navController.navigateToHighlightsList()
        }
        composeTestRule.assertThatFabDoesNotExist()

        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.random().id)
        }
        composeTestRule.assertThatFabDoesNotExist()

        composeTestRule.runOnUiThread {
            navController.navigateToCheckout()
        }
        composeTestRule.assertThatFabDoesNotExist()
    }

    /* Nessa versão já aplico as técnicas para simplificar o código mesmo sendo grande */
    @Test
    fun appNavHost_verifyIfTopAppBarIsDisplayedOnlyInHomeGraphNavigation() =
        with(composeTestRule) {
            onRoot().printToLog("panucci app")
            val topAppBarTag = "PanucciTopAppBar"
            val assertThatTopAppBarIsDisplayed = fun ComposeTestRule.() {
                onNodeWithTag(topAppBarTag)
                    .assertIsDisplayed()
            }
            runOnUiThread {
                navController.navigateToHighlightsList()
            }
            assertThatTopAppBarIsDisplayed()

            runOnUiThread {
                navController.navigateToMenu()
            }
            assertThatTopAppBarIsDisplayed()

            runOnUiThread {
                navController.navigateToDrinks()
            }
            assertThatTopAppBarIsDisplayed()

            val assertThatTopAppBarDoesNotExist = fun ComposeTestRule.() {
                onNodeWithTag(topAppBarTag)
                    .assertDoesNotExist()
            }
            runOnUiThread {
                navController.navigateToProductDetails(sampleProducts.random().id)
            }
            assertThatTopAppBarDoesNotExist()

            runOnUiThread {
                navController.navigateToCheckout()
            }
            assertThatTopAppBarDoesNotExist()
        }


    /*
    printToLog:
Printing with useUnmergedTree = 'true'
Node #1 at (l=0.0, t=66.0, r=1080.0, b=2154.0)px
 |-Node #5 at (l=0.0, t=66.0, r=1080.0, b=222.0)px
 |  |-Node #6 at (l=0.0, t=88.0, r=1080.0, b=200.0)px
 |    Text = '[Destaques do dia]'
 |    Actions = [GetTextLayoutResult]
 |-Node #7 at (l=0.0, t=222.0, r=1080.0, b=2154.0)px
   VerticalScrollAxisRange = 'ScrollAxisRange(value=0.0, maxValue=11.0, reverseScrolling=false)'
   CollectionInfo = 'androidx.compose.ui.semantics.CollectionInfo@e8424d0'
   Actions = [IndexForKey, ScrollBy, ScrollToIndex]
    |-Node #9 at (l=44.0, t=266.0, r=1036.0, b=1302.0)px
    | Focused = 'false'
    | Actions = [OnClick, RequestFocus]
    | MergeDescendants = 'true'
    |  |-Node #14 at (l=88.0, t=607.0, r=992.0, b=704.0)px
    |  | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #15 at (l=88.0, t=704.0, r=203.0, b=756.0)px
    |  | Text = '[299.90]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #17 at (l=88.0, t=800.0, r=992.0, b=1032.0)px
    |  | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales
laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu
elit ut nunc convallis laoreet]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #19 at (l=769.0, t=1104.0, r=992.0, b=1236.0)px
    |    Focused = 'false'
    |    Role = 'Button'
    |    Actions = [OnClick, RequestFocus]
    |    MergeDescendants = 'true'
    |     |-Node #21 at (l=835.0, t=1144.0, r=926.0, b=1196.0)px
    |       Text = '[Pedir]'
    |       Actions = [GetTextLayoutResult]
    |-Node #23 at (l=44.0, t=1346.0, r=1036.0, b=2063.0)px
    | Focused = 'false'
    | Actions = [OnClick, RequestFocus]
    | MergeDescendants = 'true'
    |  |-Node #27 at (l=88.0, t=1368.0, r=992.0, b=1465.0)px
    |  | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #28 at (l=88.0, t=1465.0, r=182.0, b=1517.0)px
    |  | Text = '[10.00]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #30 at (l=88.0, t=1561.0, r=992.0, b=1793.0)px
    |  | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales
laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu
elit ut nunc convallis laoreet]'
    |  | Actions = [GetTextLayoutResult]
    |  |-Node #32 at (l=769.0, t=1865.0, r=992.0, b=1997.0)px
    |    Focused = 'false'
    |    Role = 'Button'
    |    Actions = [OnClick, RequestFocus]
    |    MergeDescendants = 'true'
    |     |-Node #34 at (l=835.0, t=1905.0, r=926.0, b=1957.0)px
    |       Text = '[Pedir]'
    |       Actions = [GetTextLayoutResult]
    |-Node #36 at (l=44.0, t=2107.0, r=1036.0, b=3143.0)px
      Focused = 'false'
      Actions = [OnClick, RequestFocus]
      MergeDescendants = 'true'
       |-Node #41 at (l=88.0, t=2448.0, r=992.0, b=2545.0)px
       | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer]'
       | Actions = [GetTextLayoutResult]
       |-Node #42 at (l=88.0, t=2545.0, r=182.0, b=2597.0)px
       | Text = '[10.00]'
       | Actions = [GetTextLayoutResult]
       |-Node #44 at (l=88.0, t=2641.0, r=992.0, b=2873.0)px
       | Text = '[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales
laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu
elit ut nunc convallis laoreet]'
       | Actions = [GetTextLayoutResult]
       |-Node #46 at (l=769.0, t=2945.0, r=992.0, b=3077.0)px
         Focused = 'false'
         Role = 'Button'
         Actions = [OnClick, RequestFocus]
         MergeDescendants = 'true'
          |-Node #48 at (l=835.0, t=2985.0, r=926.0, b=3037.0)px
            Text = '[Pedir]'
            Actions = [GetTextLayoutResult]
     */
}