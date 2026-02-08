package com.beatgridmedia.moviedb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beatgridmedia.moviedb.presentation.FrontPageState
import com.beatgridmedia.moviedb.presentation.FrontPageStateHolder

@Composable
fun FrontPageScreen(modifier: Modifier = Modifier) {
    val stateHolder = remember { FrontPageStateHolder() }
    var uiState by remember { mutableStateOf(FrontPageState()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = uiState.logoTitle,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.query,
            onValueChange = { query ->
                uiState = stateHolder.updateQuery(query)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search movies") },
            singleLine = true
        )

        if (uiState.isDropdownVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    uiState.suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            text = { Text(suggestion) },
                            onClick = { uiState = stateHolder.selectSuggestion(uiState, suggestion) }
                        )
                    }
                }
            }
        }
    }
}
