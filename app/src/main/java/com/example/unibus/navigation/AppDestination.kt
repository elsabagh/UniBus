package com.example.unibus.navigation

interface AppDestination {
    val route: String

    object SignInDestination : AppDestination {
        override val route = "SignIn"
    }

    object SignUpDestination : AppDestination {
        override val route = "SignUp"
    }

    object SplashDestination: AppDestination {
        override val route = "Splash"
    }
    object UserHomeDestination : AppDestination {
        override val route = "user_home"
    }
    object DriverHomeDestination: AppDestination {
        override val route = "Driver_Home"
    }
}