package cz.mendelu.xmusil5.plantdiscoverer

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.NavGraph
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.TAG_CAMERA_FAB
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.TAG_NAVIGATION_SCREEN_NAME
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.TAG_PLANT_GRID_LIST_ITEM
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.TAG_PLANT_IMAGE_GRID_LIST_ITEM
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.screens.TAG_NO_DATA_SCREEN_MESSAGE
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.templates.TAG_DELETE_DIALOG_OK
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.camera_screen.TAG_CAMERA_GO_BACK
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.camera_screen.TAG_CAMERA_TAKE_PHOTO
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen.TAG_LAST_DISCOVERY_CARD
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen.TAG_NAVIGATION_SETTINGS
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen.TAG_IMAGE_POPUP
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen.TAG_SETTINGS_LANGUAGE_CZECH
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen.TAG_SETTINGS_LANGUAGE_DROPDOWN
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen.TAG_SETTINGS_LANGUAGE_ENGLISH
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.PlantDiscovererTheme
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.*
import kotlin.concurrent.schedule


/**
 * Test that handles basic workflow through application
 *
 * PREREQUISITES:
 *      - Give the app permissions for camera and location (since they can't be tested here)
 *      - Make sure location servides and wifi are enabled
 */

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BaseScenarioUiTest {

    private val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule()
    val composeRule = createAndroidComposeRule<MainActivity>()

    //  Launch application
    private fun launchApplication(startDestination: String) {
        composeRule.setContent {
            PlantDiscovererTheme {
                NavGraph(startDestination = startDestination)
            }
        }
    }

    @Before
    fun setUp() {

    }


    @Test
    fun A_addNewPlantTest() {
        val plantName = "Aloe vera"
        val plantImageQuery = "Aloe"
        val plantDescription = "Huge aloe vera plant found near the town."

        launchApplication(startDestination = Destination.NewPlantScreen.route )
        with(composeRule) {
            waitForIdle()
            // Now on new plant screen
            onNodeWithTag(TAG_NEW_PLANT_IMAGE).assertIsDisplayed()

            onNodeWithTag(TAG_NEW_PLANT_NAME_TEXT_FIELD).performTextInput(plantName)
            onNodeWithTag(TAG_NEW_PLANT_EMPTY_SPOT).performClick()

            onNodeWithTag(TAG_NEW_PLANT_QUERY_TEXT_FIELD).performTextInput(plantImageQuery)
            onNodeWithTag(TAG_NEW_PLANT_EMPTY_SPOT).performClick().performTouchInput { swipeUp() }

            onNodeWithTag(TAG_NEW_PLANT_DESCRIPTION_TEXT_FIELD).performTextInput(plantDescription)
            onNodeWithTag(TAG_NEW_PLANT_EMPTY_SPOT).performClick().performTouchInput { swipeUp() }

            onNodeWithTag(TAG_NEW_PLANT_SAVE_BUTTON).performClick()
            waitForIdle()

            // Now on plant detail screen
            onNodeWithTag(TAG_PLANT_DETAIL_PLANT_IMAGE).assertIsDisplayed().performTouchInput { swipeUp() }
            onNodeWithTag(TAG_PLANT_DETAIL_NAME).assertTextEquals(plantName)
            onNodeWithTag(TAG_PLANT_DETAIL_QUERY).assertTextEquals(plantImageQuery)
            onNodeWithTag(TAG_PLANT_DETAIL_DESCRIPTION).assertTextEquals(plantDescription)
        }
    }

    @Test
    fun B_plantImagesTest() {
        launchApplication(startDestination = Destination.PlantsListScreen.route)
        with(composeRule) {
            waitForIdle()
            // Starting on plants list screen with one previously created plant
            onAllNodesWithTag(TAG_PLANT_GRID_LIST_ITEM).assertAll(hasTestTag(
                TAG_PLANT_GRID_LIST_ITEM)).onFirst().performClick()
            waitForIdle()

            // Now in plant detail screen
            onNodeWithTag(TAG_PLANT_DETAIL_PLANT_IMAGE).performTouchInput { swipeUp() }
            onNodeWithTag(TAG_PLANT_DETAIL_SEARCH_BUTTON).performClick()
            waitForIdle()

            // Now in plant images screen (shold see image results from query "Aloe" from previous tests)
            onAllNodesWithTag(TAG_PLANT_IMAGE_GRID_LIST_ITEM).assertAll(hasTestTag(
                TAG_PLANT_IMAGE_GRID_LIST_ITEM)).onFirst().performClick()
            waitForIdle()
            onNodeWithTag(TAG_IMAGE_POPUP).assertIsDisplayed()


        }
    }

    @Test
    fun C_plantEditTest() {
        val plantName = "Aloe vera 1"
        val plantImageQuery = "Aloe vera"
        val plantDescription = ""

        launchApplication(startDestination = Destination.PlantsListScreen.route)
        with(composeRule) {
            waitForIdle()
            // Starting on plants list screen with one previously created plant
            onAllNodesWithTag(TAG_PLANT_GRID_LIST_ITEM).onFirst().performClick()
            waitForIdle()

            // Now in plant detail screen
            onNodeWithTag(TAG_PLANT_DETAIL_PLANT_IMAGE).performTouchInput { swipeUp() }
            onNodeWithTag(TAG_PLANT_DETAIL_EDIT_BUTTON).performClick()
            waitForIdle()

            // Now in plant edit screen
            onNodeWithTag(TAG_PLANT_EDIT_NAME_TEXT_FIELD).performTextClearance()
            onNodeWithTag(TAG_PLANT_EDIT_NAME_TEXT_FIELD).performTextInput(plantName)
            onNodeWithTag(TAG_PLANT_EDIT_EMPTY_SPOT).performClick()

            onNodeWithTag(TAG_PLANT_EDIT_QUERY_TEXT_FIELD).performTextClearance()
            onNodeWithTag(TAG_PLANT_EDIT_QUERY_TEXT_FIELD).performTextInput(plantImageQuery)
            onNodeWithTag(TAG_PLANT_EDIT_EMPTY_SPOT).performClick().performTouchInput { swipeUp() }

            onNodeWithTag(TAG_PLANT_EDIT_DESCRIPTION_TEXT_FIELD).performTextClearance()
            onNodeWithTag(TAG_PLANT_EDIT_DESCRIPTION_TEXT_FIELD).performTextInput(plantDescription)
            onNodeWithTag(TAG_PLANT_EDIT_EMPTY_SPOT).performClick().performTouchInput { swipeUp() }

            onNodeWithTag(TAG_PLANT_EDIT_SAVE_BUTTON).performClick()
            waitForIdle()

            // Now back on plant detail screen - check if changes have been saved
            onNodeWithTag(TAG_PLANT_DETAIL_PLANT_IMAGE).assertIsDisplayed().performTouchInput { swipeUp() }
            onNodeWithTag(TAG_PLANT_DETAIL_NAME).assertTextEquals(plantName)
            onNodeWithTag(TAG_PLANT_DETAIL_QUERY).assertTextEquals(plantImageQuery)
            onNodeWithTag(TAG_PLANT_DETAIL_DESCRIPTION).assertTextEquals(plantDescription)
        }
    }

    @Test
    fun D_homeScreenTest() {
        val expectedLastDate = DateUtils.getDateString(DateUtils.getCurrentUnixTime())
        val expectedNumberOfPlantsTitle = targetContext.getString(R.string.totalNumberOfPlants)

        launchApplication(startDestination = Destination.HomeScreen.route)
        with(composeRule) {
            waitForIdle()
            // Home screen with statistics
            onNodeWithText(expectedLastDate).assertIsDisplayed().performClick()
            waitForIdle()

            // Now on detail screen of plant
            onNodeWithTag(TAG_PLANT_DETAIL_PLANT_IMAGE).assertIsDisplayed()
        }
    }

    @Test
    fun E_settingsTest() {
        val config = targetContext.resources.configuration
        config.setLocale(Locale("en"))
        val settingsScreenNameEN = targetContext.createConfigurationContext(config).getString(R.string.settings)
        config.setLocale(Locale("cs"))
        val settingsScreenNameCZ = targetContext.createConfigurationContext(config).getString(R.string.settings)

        launchApplication(startDestination = Destination.HomeScreen.route)
        with(composeRule) {
            waitForIdle()
            // Home screen with statistics
            onNodeWithTag(TAG_NAVIGATION_SETTINGS).assertIsDisplayed().performClick()
            waitForIdle()

            // Now on settings screen
            onNodeWithTag(TAG_SETTINGS_LANGUAGE_DROPDOWN).assertIsDisplayed().performClick()
            onNodeWithTag(TAG_SETTINGS_LANGUAGE_CZECH).assertIsDisplayed().performClick()
            waitForIdle() // Leaving some time for recomposition of app language
            onNodeWithTag(TAG_NAVIGATION_SCREEN_NAME).assertTextEquals(settingsScreenNameCZ)

            onNodeWithTag(TAG_SETTINGS_LANGUAGE_DROPDOWN).assertIsDisplayed().performClick()
            onNodeWithTag(TAG_SETTINGS_LANGUAGE_ENGLISH).assertIsDisplayed().performClick()
            waitForIdle() // Leaving some time for recomposition of app language
            onNodeWithTag(TAG_NAVIGATION_SCREEN_NAME).assertTextEquals(settingsScreenNameEN)
        }
    }

    @Test
    fun F_plantDeleteTest() {

        launchApplication(startDestination = Destination.PlantsListScreen.route)
        with(composeRule) {
            waitForIdle()
            // Plant list screen with one previously added plant
            onAllNodesWithTag(TAG_PLANT_GRID_LIST_ITEM).onFirst().performClick()
            waitForIdle()

            // Plant detail screen
            onNodeWithTag(TAG_PLANT_DETAIL_DELETE_BUTTON).assertIsDisplayed().performClick()
            onNodeWithTag(TAG_DELETE_DIALOG_OK).assertIsDisplayed().performClick()
            waitForIdle()
        }
    }



}