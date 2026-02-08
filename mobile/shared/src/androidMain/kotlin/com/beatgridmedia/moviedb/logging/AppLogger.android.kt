package com.beatgridmedia.moviedb.logging

import android.util.Log

actual object AppLogger {
    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}
