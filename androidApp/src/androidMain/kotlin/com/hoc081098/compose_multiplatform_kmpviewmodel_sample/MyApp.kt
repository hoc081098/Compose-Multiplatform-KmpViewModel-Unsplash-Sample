package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import android.app.Application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.CommonModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger(level = Level.DEBUG)

      androidContext(this@MyApp)

      modules(
        NavigationModule,
        CommonModule,
      )
    }

    Napier.base(DebugAntilog())
  }
}
