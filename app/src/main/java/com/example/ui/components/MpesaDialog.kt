package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.SoftBlush
import com.example.ui.viewmodel.PaymentStep

@Composable
fun MpesaStkPushDialog(
    step: PaymentStep,
    depositAmountKes: Int,
    phoneNumber: String,
    serviceTitle: String,
    onDismiss: () -> Unit,
    onConfirmPin: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("mpesa_stk_dialog"),
        shape = RoundedCornerShape(20.dp),
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MpesaGreen)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "M-PESA EXPRESS",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    PaymentStep.STK_PROMPT_WAITING -> {
                        CircularProgressIndicator(
                            color = MpesaGreen,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sending STK Push to $phoneNumber...",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please check your phone screen for the Safaricom SIM prompt to enter your PIN.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    PaymentStep.PIN_ENTRY -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F4)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Pay KES %,d".format(depositAmountKes),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF104E2B)
                                )
                                Text(
                                    text = "To: STEPH'S NAIL STUDIO",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF267345)
                                )
                                Text(
                                    text = "Deposit for $serviceTitle",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Enter M-PESA PIN",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = pin,
                            onValueChange = { if (it.length <= 4) pin = it },
                            placeholder = { Text("••••") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(180.dp)
                                .testTag("mpesa_pin_input"),
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MpesaGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "128-bit Encrypted Mobile Gateway",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    PaymentStep.CONFIRMED -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MpesaGreen,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Deposit Payment Confirmed!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF104E2B)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your booking is locked in Steph's schedule. SMS confirmation sent to $phoneNumber.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    else -> {}
                }
            }
        },
        confirmButton = {
            if (step == PaymentStep.PIN_ENTRY) {
                Button(
                    onClick = {
                        if (pin.length >= 4) {
                            onConfirmPin(pin)
                        }
                    },
                    enabled = pin.length >= 4,
                    colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                    modifier = Modifier.testTag("mpesa_confirm_pin_button")
                ) {
                    Text("Confirm & Pay")
                }
            } else if (step == PaymentStep.CONFIRMED) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary),
                    modifier = Modifier.testTag("mpesa_done_button")
                ) {
                    Text("Done")
                }
            }
        },
        dismissButton = {
            if (step != PaymentStep.CONFIRMED) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("mpesa_cancel_button")
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}
