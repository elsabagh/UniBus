package com.example.unibus.presentation.signUp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unibus.R
import com.example.unibus.ui.theme.ColorTextNavDialog
import com.example.unibus.ui.theme.MainColor
import com.example.unibus.ui.theme.UniBusTheme

@Composable
fun VerifyDialog(
    onConfirm: () -> Unit,
    imageResId: Int,
    message: String,
    buttonText: String
) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        containerColor = MainColor,
        onDismissRequest = { /* Handle dismiss if needed */ },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(124.dp)
                )
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .clickable {
                            onConfirm()
                        }
                        .padding(8.dp),
                    text = buttonText,
                    color = ColorTextNavDialog,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun VerifyDialogPreview() {
    UniBusTheme {
        VerifyDialog(
            onConfirm = {},
            imageResId = R.drawable.success_account,
            message = stringResource(R.string.your_account_has_been) + stringResource(R.string.successfully_created),
            buttonText = stringResource(R.string.login_now)
        )
    }
}
