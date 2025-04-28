package com.example.unibus.presentation.user.payment


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.navigation.AppDestination
import com.example.unibus.presentation.common.TopAppBar
import com.example.unibus.presentation.signUp.components.VerifyDialog
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.UniBusTheme

@Composable
fun PaymentScreen(
    navController: NavController,
) {
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val user by paymentViewModel.user.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar("Knet", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                user?.let { user ->
                    HeaderSection(
                        user = user
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                BankAndCardDetails()
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtons(
                    onSubmit = {
                        showDialog = true
                    },
                    onCancel = {
                        navController.popBackStack(
                            AppDestination.UserHomeDestination.route,
                            inclusive = false
                        )
                        navController.navigate(AppDestination.UserHomeDestination.route) {
                            popUpTo(AppDestination.UserHomeDestination.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    )
    if (showDialog) {
        VerifyDialog(
            onConfirm = {
                showDialog = false
                navController.popBackStack(
                    AppDestination.UserHomeDestination.route,
                    inclusive = false
                )
                navController.navigate(AppDestination.UserHomeDestination.route) {
                    popUpTo(AppDestination.UserHomeDestination.route) { inclusive = true }
                }
            },
            imageResId = R.drawable.success_pay,
            message = "You have successfully completed the payment",
            buttonText = "Home"
        )
    }
}

@Composable
fun HeaderSection(
    user: User
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.knet_logo),
                contentDescription = "Knet Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Merchant:", fontWeight = FontWeight.Bold)
                Text("payments Company")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount:", fontWeight = FontWeight.Bold)
                Text("${user.busPrice} KD")
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun BankAndCardDetails() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text("Card Number:", fontWeight = FontWeight.SemiBold)
            var cardNumber by remember { mutableStateOf("") }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = cardNumber,
                    onValueChange = {
                        cardNumber = it
                    },
                    label = { "Card Number" },
                    modifier = Modifier
                        .weight(2f)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            val listOdMonth = listOf(
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"
            )
            var selectedMonth by remember { mutableStateOf("MM") }
            val listOfYear = listOf(
                "2025", "2026", "2027", "2028",
                "2029", "2030", "2031", "2032",
                "2033", "2034", "2035", "2036",
            )
            var selectedYear by remember { mutableStateOf("YY") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Expiration Date:", fontWeight = FontWeight.SemiBold)
                DropdownMenuField(
                    options = listOdMonth,
                    defaultText = selectedMonth,
                    onOptionSelected = { selectedMonth = it },
                    modifier = Modifier.weight(1f)
                )

                DropdownMenuField(
                    options = listOfYear,
                    defaultText = selectedYear,
                    onOptionSelected = { selectedYear = it },
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))
            var pinValue by remember { mutableStateOf("") }

            Row(
                horizontalArrangement = Arrangement.spacedBy(106.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("PIN:", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value =pinValue,
                    onValueChange = {
                        pinValue = it
                    },
                    label = { Text("Pin") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun ActionButtons(
    onSubmit: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)

        ) {
            Button(
                onClick = {
                    onSubmit()
                },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainColor)
            ) {
                Text("Submit", color = Color.White)
            }

            Button(
                onClick = {
                    onCancel()
                },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),

                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    options: List<String>,
    defaultText: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Card(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = defaultText)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Icon"
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    UniBusTheme {
        HeaderSection(
            user = User(
                userName = "John Doe",
                busPrice = "5.0",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BankAndCardDetailsPreview() {
    UniBusTheme {
        BankAndCardDetails()
    }
}
@Preview(showBackground = true)
@Composable
fun ActionButtonsPreview() {
    UniBusTheme {
        ActionButtons(
            onSubmit = { /* Handle submit */ },
            onCancel = { /* Handle cancel */ }
        )
    }
}