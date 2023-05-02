package com.minaev.apod.presentation.feature.apod

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.minaev.apod.R
import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.presentation.feature.model.UIError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ApodScreen(
    apodViewModel: ApodViewModel
){
    ApodScreen(
        apodViewModel.viewState,
        apodViewModel::setEvent
    )
}

@Composable
fun ApodScreen(
    stateFlow: StateFlow<ApodContract.State>,
    onEventSent: (event: ApodContract.Event) -> Unit
){
    val state by stateFlow.collectAsStateWithLifecycle()

    when(val dataModel = state.apodDataModel){
        is ApodDataModel.ApodData -> ApodSuccess(
            dataModel.apodEntity
        )
        is ApodDataModel.Error -> {
            val message = when(val uiError = dataModel.uiError){
                is UIError.ErrorResource -> stringResource(id = uiError.resId)
                is UIError.ErrorString -> uiError.message
            }
            ApodError(message) {
                onEventSent(ApodContract.Event.OnRetryButtonClick)
            }
        }
        ApodDataModel.Loading -> ApodLoading()
    }
}

@Composable
private fun ApodError(
    message: String,
    onButtonClick: () -> (Unit)
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(start = 50.dp, end = 50.dp, top = 10.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 8.dp,
                disabledElevation = 2.dp,
            ),
            contentPadding = PaddingValues(
                top = 0.dp, bottom = 0.dp, start = 15.dp, end = 15.dp
            )
        ) {
            Text(
                stringResource(id = R.string.button_retry_message),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ApodLoading(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        LinearProgressIndicator(
            modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 10.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            trackColor = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ApodSuccess(
    apodEntity: ApodEntity
){
    val bottomScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetElevation = 20.dp,
        sheetShape = RoundedCornerShape(20.dp,20.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetPeekHeight = 100.dp,
        sheetGesturesEnabled = true,
        scaffoldState = bottomScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = apodEntity.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = apodEntity.date,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,

                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = apodEntity.explanation,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Justify
                )
            }
        }
    ) {
        AsyncImage(
            model = apodEntity.imgUrl,
            contentDescription = apodEntity.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
@Preview(showBackground = true, locale = "RU",
    device = "id:pixel_4",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.NONE
)
fun ApodScreenPreview(){
    ApodScreen(
        MutableStateFlow(
            ApodContract.State(
                ApodDataModel.ApodData(apodState)
                //ApodDataModel.Error.ErrorString("Some error")
                //ApodDataModel.Loading
            )
        ),
        {}
    )
}

private val apodState= ApodEntity(
        imgUrl = "https://apod.nasa.gov/apod/image/0008/coma_noao_big.jpg",
        title = "The Coma Cluster of Galaxies",
        date = "2003-10-12",
        explanation = "Almost every object in the above photograph is a galaxy.  The Coma Cluster of Galaxies pictured above is one of the densest clusters known - it contains thousands of galaxies.  Each of these galaxies houses billions of stars - just as our own Milky Way Galaxy does.  Although nearby when compared to most other clusters, light from the Coma Cluster still takes hundreds of millions of years to reach us.  In fact, the Coma Cluster is so big it takes light millions of years just to go from one side to the other!  Most galaxies in Coma and other clusters are  ellipticals, while most galaxies outside of clusters are spirals.  The nature of Coma's X-ray emission is still being investigated.",
    )