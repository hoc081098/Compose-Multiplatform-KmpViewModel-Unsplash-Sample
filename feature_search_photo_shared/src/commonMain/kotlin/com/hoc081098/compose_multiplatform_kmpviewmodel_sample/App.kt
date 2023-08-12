package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoUseCase
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.domainModule
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.Module

val AllModules: List<Module> = listOf(
    dataModule(),
    domainModule(),
)

class DemoViewModel(
    savedStateHandle: SavedStateHandle,
    key: String,
) : ViewModel() {
    init {
        println("DemoViewModel init $key $savedStateHandle")

        viewModelScope.launch {
            delay(1000)
            println("DemoViewModel delay $key $savedStateHandle")
        }

        addCloseable {
            println("DemoViewModel close $key $savedStateHandle")
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(
    viewModel: DemoViewModel = kmpViewModel(
        factory = {
            DemoViewModel(
                savedStateHandle = createSavedStateHandle(),
                key = checkNotNull(this[VIEW_MODEL_KEY]) {
                    "Missing key $VIEW_MODEL_KEY"
                },
            )
        },
    ),
) {
    KoinApplication(
        application = {
            modules(AllModules)
        }
    ) {

        MaterialTheme {
            Text(
                text = "Hello ${getPlatformName()} ${koinInject<SearchPhotoUseCase>()}",
            )
        }
    }
}

expect fun getPlatformName(): String