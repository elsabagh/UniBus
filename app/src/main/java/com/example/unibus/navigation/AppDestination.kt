package com.example.unibus.navigation

interface AppDestination {
    val route: String

    object SignInDestination : AppDestination {
        override val route = "SignIn"
    }

    object SignUpDestination : AppDestination {
        override val route = "SignUp"
    }

    object SplashDestination : AppDestination {
        override val route = "Splash"
    }

    object UserHomeDestination : AppDestination {
        override val route = "user_home"
    }

    object ProfileUserDestination : AppDestination {
        override val route = "Profile_User"
    }

    object NotificationUserDestination : AppDestination {
        override val route = "Notification_User"
    }

    object NewTripDestination : AppDestination {
        override val route = "New_trip"
    }

    object AvailableBusesDestination : AppDestination {
        override val route = "Available_Buses"
    }

    object DriverHomeDestination : AppDestination {
        override val route = "Driver_Home"
    }
    object StudentsListDestination : AppDestination {
        override val route = "Students_List"
    }

    object UniLocationDestinationDriverDestination : AppDestination {
        override val route = "UniLocationDestination"
    }

    object ProfileDriverDestination : AppDestination {
        override val route = "Profile_Driver"
    }
    object NotificationDriverDestination : AppDestination {
        override val route = "Notification_Driver"
    }
}