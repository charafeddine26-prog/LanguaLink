package com.example.langualink.ui.learn

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * 一个简单的对话框，用于通知用户他们已完成一个单元
 * @param title 标题 (例如 "Chapitre Terminé!")
 * @param message 消息
 * @param onDismiss 当用户点击 "OK" 时的回调
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