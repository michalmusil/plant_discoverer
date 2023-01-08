package cz.mendelu.xmusil5.plantdiscoverer.di

import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen.HomeViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen.MapViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen.NewPlantViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen.PlantDetailViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen.PlantEditViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen.PlantImagesViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen.PlantsListViewModel
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        PlantsListViewModel(get())
    }

    viewModel{
        HomeViewModel(get())
    }

    viewModel{
        PlantDetailViewModel(get())
    }

    viewModel{
        PlantEditViewModel(get())
    }

    viewModel{
        PlantImagesViewModel(get())
    }

    viewModel{
        MapViewModel(get())
    }

    viewModel{
        NewPlantViewModel(get(), get())
    }

    viewModel{
        SettingsViewModel(get(), get())
    }
}