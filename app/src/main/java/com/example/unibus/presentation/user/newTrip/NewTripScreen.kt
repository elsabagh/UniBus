package com.example.unibus.presentation.user.newTrip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.R
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.ui.theme.MainColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NewTripScreen(
    navController: NavController
) {
    val viewModel: NewTripViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar("New trip", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SelectDate(
                    selectedDate = state.value.date,
                    onDateSelected = { viewModel.onDateChange(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectTime(
                    selectedTime = state.value.newSelectTime,
                    newTripViewModel = viewModel
                )
                Spacer(modifier = Modifier.height(16.dp))

                SelectLocation(
                    selectedLocation = state.value.selectLocation,
                    onLocationSelected = { viewModel.onLocationChange(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AvailableBusesButton(
                    onClick = {
                        navController.navigate(
                            AppDestination.AvailableBusesDestination.route
                        )
                    }
                )
            }
        }
    )
}

// region SelectDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DateSelectionDialog(
            onDismissRequest = { showDatePicker = false },
            onConfirmClicked = { millis ->
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = formatter.format(Date(millis))
                onDateSelected(date)
                showDatePicker = false
            },
            onDismissButtonClicked = { showDatePicker = false }
        )
    }
    Column {
        Text(
            text = "Select date:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.Gray
        )

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = "Ex: 01/01/2025",
                    color = Color.Gray

                )
            },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionDialog(
    onDismissRequest: () -> Unit,
    onConfirmClicked: (Long) -> Unit,
    onDismissButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onConfirmClicked(datePickerState.selectedDateMillis!!)
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismissButtonClicked) {
                Text(text = "Cancel")
            }
        },
        modifier = modifier
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}
// endregion

// region SelectTime
@Composable
fun SelectTime(
    selectedTime: String,
    newTripViewModel: NewTripViewModel,
) {
    var isSelectedTimeDialogVisible by remember { mutableStateOf(false) }
    Column {
        Text(
            text = "Select time:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.Gray
        )
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            placeholder = {
                Text(
                    text = "Ex: 08:30 AM",
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isSelectedTimeDialogVisible = true }) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Select time")
                }
            }
        )
        if (isSelectedTimeDialogVisible) {
            TimeSelectionDialog(
                onConfirm = { hour, minute ->
                    val selectedTime = "$hour:${minute.toString().padStart(2, '0')}"
                    newTripViewModel.onSelectTimeChange(selectedTime)
                    isSelectedTimeDialogVisible = false
                },
                onDismiss = { isSelectedTimeDialogVisible = false }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionDialog(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )
    TimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(
                timePickerState.hour,
                timePickerState.minute
            )
        },
        modifier = modifier
    ) {
        TimePicker(
            state = timePickerState
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dismissText = stringResource(R.string.cancel)
    val confirmText = stringResource(R.string.confirm)
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        text = { content() },
        modifier = modifier
    )
}
// endregion

// region SelectLocation
@Composable
fun SelectLocation(
    selectedLocation: String,
    onLocationSelected: (String) -> Unit
) {
    var showLocationPicker by remember { mutableStateOf(false) }

    if (showLocationPicker) {
        // Implement your location picker dialog here
        // Call onLocationSelected with the selected location
        showLocationPicker = false
    }
    Column {
        Text(
            text = "Select location:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.Gray
        )
        OutlinedTextField(
            value = selectedLocation,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = "Location",
                    color = Color.Gray
                )
            },
            trailingIcon = {
                IconButton(onClick = { showLocationPicker = true }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Select location")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showLocationPicker = true }
        )
    }
}
// endregion

// region AvailableBusesButton
@Composable
fun AvailableBusesButton(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(MainColor),
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    onClick = onClick
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Available Buses",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,

                )
        }
    }
}
// endregion