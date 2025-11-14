package com.az.umirhackapp.email

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun sendReportViaIntent(
    context: Context,
    recipientEmail: String,
    subject: String,
    body: String,
    pdfUri: Uri? = null,
    csvUri: Uri? = null
) {
    val files = arrayListOf<Uri>()
    if (pdfUri != null)
        files.add(pdfUri)
    if (csvUri != null)
        files.add(csvUri)

    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)

        putExtra(Intent.EXTRA_STREAM, files)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Отправить отчет через..."))
    } catch (e: Exception) {
        Toast.makeText(context, "Не найдено приложение для отправки email", Toast.LENGTH_SHORT).show()
    }
}
