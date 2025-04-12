package com.example.unibus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unibus.ContainerAppState
import com.example.unibus.SplashScreen
import com.example.unibus.navigation.AppDestination.SignInDestination
import com.example.unibus.navigation.AppDestination.SignUpDestination
import com.example.unibus.presentation.driver.DriverScreen
import com.example.unibus.presentation.driver.driverProfileDetails.DriverProfileUserDetails
import com.example.unibus.presentation.driver.notification.NotificationDriverScreen
import com.example.unibus.presentation.driver.studentsApproveList.StudentsListScreen
import com.example.unibus.presentation.driver.uniLocation.UniLocationScreen
import com.example.unibus.presentation.signIn.SignInScreen
import com.example.unibus.presentation.signUp.SignupScreen
import com.example.unibus.presentation.user.UserHomeScreen
import com.example.unibus.presentation.user.availableBuses.AvailableBuses
import com.example.unibus.presentation.user.newTrip.NewTripScreen
import com.example.unibus.presentation.user.profile.editProfile.EditProfile
import com.example.unibus.presentation.user.profile.profileDetails.ProfileUserDetails


@Composable
fun NavGraph(
    appState: ContainerAppState,
    userRole: String?,
    isAccountReady: Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = appState.navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable(route = "splash") {
            SplashScreen(
                isAccountReady = isAccountReady,
                userRole = userRole,
                onSplashFinished = {
                    val destination = when {
                        !isAccountReady || userRole == null -> SignInDestination.route
                        userRole == "driver" -> AppDestination.DriverHomeDestination.route
                        else -> AppDestination.UserHomeDestination.route
                    }
                    appState.navigateSingleTopToAndPopupTo(destination, "splash")
                }
            )
        }
        composable(route = SignInDestination.route) {
            SignInScreen(
                onSignInClick = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.UserHomeDestination.route,
                        popUpToRoute = AppDestination.UserHomeDestination.route
                    )
                },
                onSignUpClickNav = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = SignUpDestination.route,
                        popUpToRoute = SignUpDestination.route
                    )
                },
                onDriverSignIn = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.DriverHomeDestination.route,
                        popUpToRoute = AppDestination.DriverHomeDestination.route
                    )
                }

            )
        }
        composable(
            route = SignUpDestination.route
        ) {
            SignupScreen(
                navController = appState.navController,
            )
        }


        composable(AppDestination.DriverHomeDestination.route) {
            DriverScreen(navController = appState.navController)
        }
        composable(AppDestination.UniLocationDestinationDriverDestination.route) {
            UniLocationScreen(
                navController = appState.navController,
            )
        }
        composable(AppDestination.NotificationDriverDestination.route) {
            NotificationDriverScreen(
                navController = appState.navController,
            )
        }
        composable(AppDestination.StudentsListDestination.route) {
            StudentsListScreen(
                navController = appState.navController,
            )
        }
        composable(AppDestination.ProfileDriverDestination.route) {
            DriverProfileUserDetails(
                navController = appState.navController,
            )
        }

        composable(AppDestination.UserHomeDestination.route) {
            UserHomeScreen(
                navController = appState.navController,
            )
        }
        composable(AppDestination.ProfileUserDestination.route) {
            ProfileUserDetails(
                navController = appState.navController,
            )
        }
        composable(AppDestination.NewTripDestination.route) {
            NewTripScreen(
                navController = appState.navController,
            )
        }
        composable(AppDestination.AvailableBusesDestination.route) {
            AvailableBuses(
                navController = appState.navController,
            )
        }
        composable("Edit_Profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            EditProfile(
                navController = appState.navController,
                userId = userId
            )
        }
    }
}

