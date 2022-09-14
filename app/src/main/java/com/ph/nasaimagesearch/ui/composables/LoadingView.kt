package com.ph.nasaimagesearch.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
fun LoadingView(
    modifier: Modifier = Modifier.fillMaxSize(),
    loadingInfo: String = stringResource(id = R.string.loading_view_generic_loading_info)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.tiny_spacing),
            alignment = Alignment.CenterVertically
        )
    ) {
        CircularProgressIndicator()

        Text(text = loadingInfo, style = Typography.labelSmall)
    }
}

@Preview
@Composable
private fun previewLoadingView() {
    LoadingView()
}