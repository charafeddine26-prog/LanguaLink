package com.example.langualink.ui.profile

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph

@Composable
fun CertificateModal(
    userName: String,
    language: String,
    level: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            generatePdf(context, userName, language, level)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Certificate of Achievement") },
        text = {
            Column {
                Text(text = "This certifies that")
                Text(text = userName)
                Text(text = "has successfully completed the $level level of the $language course.")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        generatePdf(context, userName, language, level)
                    }
                }
            ) {
                Text("Download")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

private fun generatePdf(context: Context, userName: String, language: String, level: String) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "certificate.pdf")
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/LanguaLink")
        }
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        resolver.openOutputStream(uri)?.use { outputStream ->
            val writer = PdfWriter(outputStream)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            document.add(Paragraph("Certificate of Achievement"))
            document.add(Paragraph("This certifies that"))
            document.add(Paragraph(userName))
            document.add(Paragraph("has successfully completed the $level level of the $language course."))

            document.close()
            Toast.makeText(context, "Certificate saved to Downloads folder", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Failed to create PDF", Toast.LENGTH_LONG).show()
    }
}