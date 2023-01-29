package com.ph.nasaimagesearch.ui.composables.util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.clearFocusOnBackPressed(): Modifier = composed {
    val focusManager = LocalFocusManager.current

    onKeyEvent {
        if (it.key == Key.Back) {
            focusManager.clearFocus()
            true
        } else false
    }
}