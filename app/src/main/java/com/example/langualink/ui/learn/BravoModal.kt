package com.example.langualink.ui.learn

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun BravoModal(
    points: Int,
    badgeName: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Bravo!") },
        text = {
            if (badgeName != null) {
                Text(text = "You earned $points points and the $badgeName badge!")
            } else {
                Text(text = "You earned $points points!")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
