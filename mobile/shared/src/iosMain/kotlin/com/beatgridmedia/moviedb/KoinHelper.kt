package com.beatgridmedia.moviedb

import com.beatgridmedia.moviedb.di.sharedModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModule)
    }
}
