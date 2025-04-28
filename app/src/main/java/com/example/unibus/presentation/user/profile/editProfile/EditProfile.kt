package com.example.unibus.presentation.user.profile.editProfile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor

@Composable
fun EditProfile(
    navController: NavController,
    userId: String
) {
    val editUserViewModel: EditProfileViewModel = hiltViewModel()
    val state by editUserViewModel.editUserState.collectAsStateWithLifecycle()
    val openDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri.value = uri }
    )

    LaunchedEffect(userId) {
        editUserViewModel.fetchUserData(userId)
    }
    val openEmailDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar("Edit", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                EditUserImage(
                    state = state,
                    imageUri = imageUri,
                    pickImageLauncher = pickImageLauncher
                )

                OutlinedTextField(
                    value = state.userName,
                    onValueChange = { editUserViewModel.onUserNameChange(it) },
                    label = { Text("User name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { editUserViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { openEmailDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MainColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Change Email")
                }

                if (openEmailDialog.value) {
                    ChangeEmailDialog(
                        onDismiss = { openEmailDialog.value = false },
                        onChangeEmail = { newEmail, currentPassword ->
                            editUserViewModel.updateUserEmail(
                                currentPassword = currentPassword,
                                newEmail = newEmail,
                                navController = navController,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Email updated successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    openEmailDialog.value = false
                                },
                                onFailure = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }

                OutlinedTextField(
                    value = state.idNumber,
                    onValueChange = { editUserViewModel.onIdNumberChange(it) },
                    label = { Text("Uni ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { editUserViewModel.onMobileChange(it) },
                    label = { Text("Mobile number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { openDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MainColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Change password")
                }

                if (openDialog.value) {
                    ChangePasswordDialog(
                        onDismiss = { openDialog.value = false },
                        onChangePassword = { oldPassword, newPassword ->
                            editUserViewModel.onPasswordChange(oldPassword)
                            editUserViewModel.onNewPasswordChange(newPassword)
                            editUserViewModel.changePassword()
                            openDialog.value = false
                        }
                    )
                }
                Button(
                    onClick = {
                        editUserViewModel.updateUserProfile(
                            navController,
                            imageUri.value,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MainColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                }

                if (state.isLoading) {
                    LoadingOverlay()
                }

            }
        }
    )
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}


@Composable
fun ChangeEmailDialog(
    onDismiss: () -> Unit,
    onChangeEmail: (String, String) -> Unit
) {
    val newEmail = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Email") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = newEmail.value,
                    onValueChange = { newEmail.value = it },
                    label = { Text("New Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = currentPassword.value,
                    onValueChange = { currentPassword.value = it },
                    label = { Text("Current Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangeEmail(newEmail.value, currentPassword.value)
                },
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditUserImage(
    state: EditUserState,
    imageUri: MutableState<Uri?>,
    pickImageLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            val photoToShow = imageUri.value ?: state.userPhoto
            photoToShow?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .size(80.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(
                                8.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
                    .size(80.dp),
            ) {
                IconButton(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White, shape = RoundedCornerShape(
                                8.dp
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
            }

        }
    }
}


@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = oldPassword.value,
                    onValueChange = { oldPassword.value = it },
                    label = { Text("Old Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangePassword(oldPassword.value, newPassword.value)
                },
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChangePasswordDialog() {
    ChangePasswordDialog(
        onDismiss = { /* Do nothing on dismiss */ },
        onChangePassword = { old, new ->
            println("Old Password: $old, New Password: $new")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChangeEmailDialog() {
    ChangeEmailDialog(
        onDismiss = { /* Do nothing on dismiss */ },
        onChangeEmail = { newEmail, currentPassword ->
            println("New Email: $newEmail, Current Password: $currentPassword")
        }
    )
}

