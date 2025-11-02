package com.example.langualink.ui.learn

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A simple dialog used to notify the user that they have completed a unit.
 * @param title The title (e.g., "Chapter Completed!")
 * @param message The message content
 * @param onDismiss Callback triggered when the user clicks "OK"
 */

@Composable
fun CompletionModal(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}