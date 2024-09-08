package com.ph.nasaimagesearch.ui.composables.util

import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

@Composable
fun isKeyboardOpen(): State<Boolean> {
    val view = LocalView.current
    val isOpen = {
        val rootWindowInsets = ViewCompat.getRootWindowInsets(view)
        rootWindowInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true
    }

    return produceState(initialValue = isOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = isOpen() }

        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}

fun Modifier.restoreKeyboardAfterRotation(
    focusRequester: FocusRequester = FocusRequester()
): Modifier = composed {
    var hasFocus by remember { mutableStateOf(false) }
    val isKeyboardOpen by isKeyboardOpen()
    var restoreKeyboardSavable by rememberSaveable { mutableStateOf(false) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // onDestroy is called before the focus has been cleared
    // onDispose is called after the focus has been cleared and would therefore always be false
    DisposableEffect(key1 = lifecycle) {
        val onDestroyListener = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                restoreKeyboardSavable = hasFocus && isKeyboardOpen
            }
        }

        lifecycle.addObserver(onDestroyListener)
        onDispose { lifecycle.removeObserver(onDestroyListener) }
    }

    LaunchedEffect(key1 = Unit) {
        delay(200)
        if (restoreKeyboardSavable) {
            restoreKeyboardSavable = false
            focusRequester.requestFocus()
        }
    }

    onFocusChanged { hasFocus = it.hasFocus }
        .focusRequester(focusRequester)
}

fun Modifier.clearFocusOnKeyboardClosed(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by isKeyboardOpen()
    var hasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isKeyboardOpen) {
        if (hasFocus && !isKeyboardOpen) focusManager.clearFocus()
    }

    onFocusChanged { hasFocus = it.hasFocus }
}