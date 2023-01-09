package cz.mendelu.xmusil5.plantdiscoverer

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.NavGraph
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITests {

    private val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var navController: NavHostController

    @get:Rule()
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {

    }









    //  Launch application
    private fun launchApplication() {
        composeRule.setContent {
            MaterialTheme {
                NavGraph(startDestination = Destination.PlantsListScreen.route)
            }
        }
    }
}