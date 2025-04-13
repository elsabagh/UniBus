package com.example.unibus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.unibus.ContainerAppState
import com.example.unibus.SplashScreen
import com.example.unibus.navigation.AppDestination.SignInDestination
import com.example.unibus.navigation.AppDestination.SignUpDestination
import com.example.unibus.presentation.driver.DriverScreen
import com.example.unibus.presentation.driver.driverProfileDetails.DriverProfileUserDetails
import com.example.unibus.presentation.driver.notification.NotificationDriverScreen
import com.example.unibus.presentation.driver.studentsApproveList.StudentsListScreen
import com.example.unibus.presentation.driver.uniLocation.UniLocationScreen
import com.example.unibus.presentation.driver.userLocation.UserLocationScreen
import com.example.unibus.presentation.signIn.SignInScreen
import com.example.unibus.presentation.signUp.SignupScreen
import com.example.unibus.presentation.user.UserHomeScreen
import com.example.unibus.presentation.user.availableBuses.AvailableBuses
import com.example.unibus.presentation.user.newTrip.NewTripScreen
import com.example.unibus.presentation.user.notifications.UserNotificationScreen
import com.example.unibus.presentation.user.payment.PaymentScreen
import com.example.unibus.presentation.user.profile.editProfile.EditProfile
import com.example.unibus.presentation.user.profile.profileDetails.ProfileUserDetails


@Composable
fun NavGraph(
    appState: ContainerAppState,
    userRole: String?,
    isAccountReady: Boolean,
    modifier: Modifier = Modifier,
    navigateTo: String // Add navigateTo parameter

) {
    NavHost(
        navController = appState.navController,
        startDestination = if (navigateTo.isNotEmpty()) navigateTo else "splash", // Use navigateTo if provided
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
        composable(
            route = "${AppDestination.UserLocationDestination.route}?lat={lat}&lng={lng}&userId={userId}&userName={userName}",
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType },
                navArgument("lng") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull() ?: 0.0
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val userName = backStackEntry.arguments?.getString("userName").orEmpty()

            UserLocationScreen(
                navController = appState.navController,
                lat = lat,
                lng = lng,
                userId = userId,
                userName = userName
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
        composable(AppDestination.NotificationUserDestination.route) {
            UserNotificationScreen(
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
        composable(AppDestination.PaymentDestination.route) {
            PaymentScreen(
                navController = appState.navController,
            )
        }
    }
}

