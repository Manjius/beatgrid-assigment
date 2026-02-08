package com.beatgridmedia.moviedb

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.window.ComposeUIViewController
import com.beatgridmedia.moviedb.ui.FrontPageScreen

fun FrontPageViewController() = ComposeUIViewController {
    MaterialTheme {
        Surface {
            FrontPageScreen()
        }
    }
}
