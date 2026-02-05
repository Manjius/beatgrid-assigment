package com.beatgridmedia.moviedb.android

import android.app.Application
import com.beatgridmedia.moviedb.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieDbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieDbApplication)
            modules(sharedModule)
        }
    }
}
