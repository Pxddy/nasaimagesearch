package com.ph.nasaimagesearch.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ph.nasaimagesearch.R
import com.ph.nasaimagesearch.ui.theme.Typography

@Composable
fun ErrorView(
    modifier: Modifier = Modifier.fillMaxSize(),
    errorInfo: String = stringResource(id = R.string.error_view_generic_error_info),
    errorButtonText: String = stringResource(id = R.string.error_view_generic_error_button_text),
    errorButtonAction: ErrorButtonAction
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.normal_spacing),
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(text = errorInfo, style = Typography.titleLarge)

        OutlinedButton(onClick = errorButtonAction) {
            Text(text = errorButtonText)
        }
    }
}

typealias ErrorButtonAction = () -> Unit

@Preview
@Composable
private fun previewErrorView() {
    ErrorView {

    }
}