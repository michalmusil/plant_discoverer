package cz.mendelu.xmusil5.plantdiscoverer.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.NavGraph
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.PlantDiscovererTheme
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val languageUtils: LanguageUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppLanguage()

        setContent {
            PlantDiscovererTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(startDestination = Destination.PlantsListScreen.route)
                }
            }
        }
    }

    private fun setAppLanguage(){
        lifecycleScope.launch{
            val currentAppLanguage = languageUtils.getAppLanguage()
            languageUtils.updateAppLanguageInUi(currentAppLanguage)
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlantDiscovererTheme {
        Greeting("Android")
    }
}