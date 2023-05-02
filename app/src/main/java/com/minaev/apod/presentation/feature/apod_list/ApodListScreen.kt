package com.minaev.apod.presentation.feature.apod_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.minaev.apod.R
import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.presentation.feature.ShowErrorSnackBar
import com.minaev.apod.presentation.feature.model.UIError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ApodListScreen(
    apodListViewModel: ApodListViewModel,
    onShowSnackBar: (String)->(Unit)
){
    ApodListScreen(
        apodListViewModel.viewState,
        apodListViewModel.effect,
        apodListViewModel::setEvent,
        onShowSnackBar
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ApodListScreen(
    stateFlow: StateFlow<ApodListContract.State>,
    effectFlow: Flow<ApodListContract.Effect>?,
    onEventSent: (event: ApodListContract.Event) -> Unit,
    onShowSnackBar: (String)->(Unit)
){
    val state by stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(context){
        effectFlow?.collectLatest {
            when(it){
                is ApodListContract.Effect.SnackBar -> {
                    when(it.snackBarEvent){
                        is ShowErrorSnackBar -> {
                            val message = when(val error = it.snackBarEvent.uiError){
                                is UIError.ErrorResource -> context.getString(error.resId)
                                is UIError.ErrorString -> error.message
                            }
                            onShowSnackBar(message)
                        }
                    }
                }
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        state.isRefresh,
        { onEventSent(ApodListContract.Event.OnRefreshData) }
    )

    Box(
        Modifier
            .pullRefresh(pullRefreshState)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        ){
            when(val it = state.apodData){
                is ApodListDataModel.ApodListData -> {
                    items(items = it.apodList) { item ->
                        ApodItem(item)
                    }
                }
                ApodListDataModel.ApodListEmpty -> item {  }
                ApodListDataModel.ApodListError -> item { ErrorItem() }
            }
        }

        PullRefreshIndicator(
            state.isRefresh,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun ApodItem(apodItem: ApodEntity){
    Card(
        modifier = Modifier.padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = apodItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                )
            }
            Text(
                text = apodItem.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun ErrorItem(){
    Card(
        modifier = Modifier.padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.empty_list_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Icon(
                    imageVector = Icons.Default.Warning,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.empty_list_message),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}