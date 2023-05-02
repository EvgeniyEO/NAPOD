package com.minaev.apod.presentation.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.minaev.apod.R
import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.presentation.feature.Constants
import com.minaev.apod.presentation.feature.Constants.BOTTOM_BAR_HEIGHT
import com.minaev.apod.presentation.feature.Constants.TOP_BAR_HEIGHT
import com.minaev.apod.presentation.feature.ScreenType
import com.minaev.apod.presentation.feature.apod_list.ApodListContract
import com.minaev.apod.presentation.feature.apod_list.ApodListDataModel
import com.minaev.apod.presentation.feature.apod_list.ApodListScreen
import com.minaev.apod.presentation.ui.theme.NASA_ApodTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val tabItems = ScreenType.getScreenList()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    snackBarHostState: SnackbarHostState,
    onPagerContent: @Composable (ScreenType)->(Unit)
){
    HomeScreen(
        homeViewModel.viewState,
        snackBarHostState,
        onPagerContent
    )
}

@ExperimentalFoundationApi
@Composable
private fun HomeScreen(
    stateFlow: StateFlow<HomeContract.State>,
    snackBarHostState: SnackbarHostState,
    onPagerContent: @Composable (ScreenType)->(Unit)
){
    val state by stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        snackbarHostState = snackBarHostState
    )
    val pagerState = rememberPagerState(initialPage = Constants.TODAY_PAGE_INDEX)
    val tabIndex = pagerState.currentPage

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar(tabIndex, pagerState, scope) },
        topBar = { TopBar(state.headerTitle) },
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        snackbarHost = {
            SnackbarHost(
                hostState = it
            ){ snackBarData ->
                Snackbar(
                    snackbarData = snackBarData,
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    actionColor = MaterialTheme.colorScheme.error
                )
            }
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = scaffoldPadding.calculateTopPadding(),
                    bottom = scaffoldPadding.calculateBottomPadding()
                ),
            elevation = 20.dp,
            shape = RoundedCornerShape(20.dp,20.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            HorizontalPager(
                pageCount = tabItems.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.Top
            ){ page ->
                onPagerContent(ScreenType.getScreenTypeByIndex(page))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
private fun BottomBar(
    tabIndex: Int,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
){
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val onBackgroundColor = MaterialTheme.colorScheme.onSurfaceVariant
    val selectColor = MaterialTheme.colorScheme.secondary
    val onSelectColor = MaterialTheme.colorScheme.onSecondary

    TabRow(
        modifier = Modifier.height(BOTTOM_BAR_HEIGHT.dp),
        selectedTabIndex = tabIndex,
        // Отображение серого фона поверх выбранного таба
        indicator = { tabPositions ->
            Box(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .height(BOTTOM_BAR_HEIGHT.dp)
                    // Помещаем серый фон на задний план,
                    // чтобы не затереть содержимое таба
                    .zIndex(1f)
                    .background(selectColor)
            )
        },
        backgroundColor = backgroundColor
    ) {
        val dividerColor = MaterialTheme.colorScheme.secondary
        val dividerPadding = with(LocalDensity.current) { 8.dp.toPx() }
        val strokeWidth = with(LocalDensity.current) { 1.dp.toPx() }
        tabItems.forEachIndexed { index, screen ->
            val isSelected = index == tabIndex
            Tab(
                modifier = Modifier
                    // Помещаем содержимое таба на передний план
                    .zIndex(2f)
                    // Рисуем разделительную линию между табами
                    .drawBehind {
                        if (index < tabItems.size - 1) {
                            drawLine(
                                dividerColor,
                                Offset(size.width, 0f + dividerPadding),
                                Offset(size.width, size.height - dividerPadding),
                                strokeWidth
                            )
                        }
                    } ,
                selected = tabIndex == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        stringResource(id = screen.titleId),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) onSelectColor else onBackgroundColor
                    )
                },
                icon = {
                    Icon(
                        painterResource(screen.iconId),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isSelected) onSelectColor else onBackgroundColor
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    headerTitleId: Int
){
    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(TOP_BAR_HEIGHT.dp),
        title = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = stringResource(id = headerTitleId),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, locale = "RU",
    device = "id:pixel_4",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.NONE
)
@Composable
fun HomeScreenPreview(){
    val homeState = HomeContract.State(
        R.string.apod_list_title
    )
    val apodListState = ApodListContract.State(
        false,
        ApodListDataModel.ApodListData(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map {
                ApodEntity(
                    "Some url",
                    "Title$it",
                    "$it",
                    "explanation"
                )
            }
        )
    )
    val homeStateFlow = MutableStateFlow(homeState)
    val apodListStateStateFlow = MutableStateFlow(apodListState)
    NASA_ApodTheme{
        HomeScreen(
            homeStateFlow.asStateFlow(),
            SnackbarHostState()
        ){page ->
            when(page){
                ScreenType.Today -> {}
                ScreenType.List -> {
                    ApodListScreen(
                        apodListStateStateFlow.asStateFlow(),
                        flow {

                        },
                        {},
                        {}
                    )
                }
            }
        }
    }
}
