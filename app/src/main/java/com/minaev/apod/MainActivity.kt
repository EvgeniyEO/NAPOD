package com.minaev.apod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minaev.apod.domain.repository.INasaRepository
import com.minaev.apod.presentation.feature.ScreenType
import com.minaev.apod.presentation.feature.apod.ApodScreen
import com.minaev.apod.presentation.feature.apod.ApodViewModel
import com.minaev.apod.presentation.feature.home.HomeScreen
import com.minaev.apod.presentation.feature.apod_list.ApodListViewModel
import com.minaev.apod.presentation.feature.apod_list.ApodListScreen
import com.minaev.apod.presentation.feature.home.HomeViewModel
import com.minaev.apod.presentation.navigation.NavTarget
import com.minaev.apod.presentation.navigation.NavigationManager
import com.minaev.apod.presentation.ui.theme.NASA_ApodTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var apodRepository: INasaRepository
    @Inject lateinit var navigationManager: NavigationManager

    private val apodListViewModel: ApodListViewModel by viewModels()
    private val apodViewModel: ApodViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NASA_ApodTheme {
                val navController = rememberNavController()
                LaunchedEffect(key1 = null ){
                    navigationManager.sharedFlow.onEach {
                        when(it){
                            NavTarget.Back -> navController.navigateUp()
                            else -> navController.navigate(it.label)
                        }
                    }
                }
                val snackBarHostState = remember { SnackbarHostState() }
                val snackBarScope = rememberCoroutineScope()
                val snackBarCloseButton = stringResource(id = R.string.snack_bar_error_close_button)
                val showErrorSnackBar: (message: String) -> (Unit) = { message ->
                    snackBarScope.launch {
                        snackBarHostState.showSnackbar(message, snackBarCloseButton, SnackbarDuration.Indefinite)
                    }
                }

                NavHost(navController = navController, startDestination = NavTarget.Home.label) {
                    composable(NavTarget.Home.label) {
                        HomeScreen(homeViewModel, snackBarHostState){ page ->
                            when(page){
                                ScreenType.Today -> {
                                    ApodScreen(apodViewModel)
                                }
                                ScreenType.List -> {
                                    ApodListScreen(apodListViewModel){
                                        showErrorSnackBar(it)
                                    }
                                }
                            }
                        }
                    }
                    composable(NavTarget.Detail.label) {
                        //ApodDetailScreen()
                    }
                }
            }
        }
    }
}