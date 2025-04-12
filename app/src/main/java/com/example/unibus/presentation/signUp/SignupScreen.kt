package com.example.unibus.presentation.signUp

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.R
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.signUp.components.VerifyDialog
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.utils.snackbar.SnackBarManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isAccountCreated by viewModel.isAccountCreated.collectAsStateWithLifecycle()

    var userPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val userPhotoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            userPhotoUri = uri
        }

    LaunchedEffect(isAccountCreated) {
        if (isAccountCreated) {
            SnackBarManager.showMessage(R.string.created_account)
            viewModel.resetIsAccountCreated()
            showDialog = true
        }
    }

    if (showDialog) {
        VerifyDialog(
            onConfirm = {
                showDialog = false
                navController.popBackStack(
                    AppDestination.SignInDestination.route,
                    inclusive = false
                )
                navController.navigate(AppDestination.SignInDestination.route) {
                    popUpTo(AppDestination.SignUpDestination.route) { inclusive = true }
                }
            },
            imageResId = R.drawable.success_account,
            message = stringResource(R.string.your_account_has_been) + stringResource(R.string.successfully_created),
            buttonText = stringResource(R.string.login_now)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.sign_up),
                        color = MainColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = MainColor
                        )

                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserPhotoSection(
                userPhotoUri = userPhotoUri,
                userPhotoPicker = userPhotoPicker
            )
            Spacer(modifier = Modifier.height(16.dp))

            UserDetailsSection(
                uiState = uiState,
                viewModel = viewModel
            )

            PasswordSection(
                uiState = uiState,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpButton(
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.createAccount(
                        userPhotoUri = userPhotoUri
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                buildAnnotatedString {
                    append("Already have an account,")
                    withStyle(
                        style = SpanStyle(color = MainColor),
                    )
                    {
                        append(" Sign in")
                    }
                },
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                        navController.navigate(AppDestination.SignInDestination.route)
                    }
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun UserPhotoSection(
    userPhotoUri: Uri?,
    userPhotoPicker: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp)
            .clickable { userPhotoPicker.launch("image/*") },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "User photo (Optional):",
            modifier = Modifier,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (userPhotoUri != null) {
            Image(
                painter = rememberImagePainter(userPhotoUri),
                contentDescription = "User Photo",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.add_user_photo),
                contentDescription = "Upload Photo",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun UserDetailsSection(uiState: SignUpState, viewModel: SignUpViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = uiState.userName,
            onValueChange = viewModel::onUserNameChange,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = viewModel::onMobileChange,
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()

        )
        OutlinedTextField(
            value = uiState.idNumber,
            onValueChange = viewModel::onIdNumberChange,
            label = { Text("University ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()

        )
    }
}

@Composable
fun PasswordSection(uiState: SignUpState, viewModel: SignUpViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()

        )
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()

        )
    }
}

@Composable
fun SignUpButton(isLoading: Boolean, onClick: () -> Unit) {
    if (!isLoading) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(MainColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Sign Up")
        }
    }
}