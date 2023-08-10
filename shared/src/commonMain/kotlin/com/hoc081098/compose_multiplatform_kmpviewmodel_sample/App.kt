package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

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
    MaterialTheme {

    }
}

expect fun getPlatformName(): String