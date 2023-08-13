package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchPhotoScreen(
                navigateToPhotoDetail = {
                    // TODO
                }
            )
        }
    }
}