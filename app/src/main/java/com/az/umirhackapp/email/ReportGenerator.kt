package com.az.umirhackapp.email

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.ui.screens.document_items.getStatusText
import java.io.File
import java.io.FileOutputStream

class ReportGenerator(private val context: Context) {

    fun generateAndSendInventoryReport(
        document: Document,
        recipientEmail: String,
        includePDF: Boolean,
        includeCSV: Boolean
    ) {
        println("generateAndSendInventoryReport: Start")
        val pdfUri: Uri? = if (includePDF) {
            val pdfFile = generatePdfReport(document)

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )
        } else
            null
        println("generateAndSendInventoryReport: PDF+")
        val csvUri: Uri? = if (includeCSV) {
            val csvFile = generateCsvReport(document)

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                csvFile
            )
        } else
            null
        println("generateAndSendInventoryReport: CSV+")
        val subject = "Отчет по инвентаризации №${document.number}"
        val body = createEmailBody(document)
        println("generateAndSendInventoryReport: PreEnd")
        sendReportViaIntent(context, recipientEmail, subject, body, pdfUri, csvUri)
        println("generateAndSendInventoryReport: End")
    }

    private fun generatePdfReport(document: Document): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 12f
        canvas.drawText("Отчет по инвентаризации", 50f, 50f, paint)
        canvas.drawText("Документ: ${document.number}", 50f, 70f, paint)
        canvas.drawText("Склад: ${document.warehouse?.name}", 50f, 90f, paint)
        canvas.drawText("Дата: ${document.documentDate}", 50f, 110f, paint)

        // Табличка с товарами
        var yPos = 140f
        document.items.forEachIndexed { index, item ->
            canvas.drawText("${index + 1}. ${item.product?.name ?: "Товар ${item.productId}"}", 50f, yPos, paint)
            canvas.drawText("Ожидалось: ${item.quantityExpected}", 300f, yPos, paint)
            canvas.drawText("Фактически: ${item.quantityActual}", 450f, yPos, paint)
            yPos += 20f
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "report_${document.number}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }

    private fun generateCsvReport(document: Document): File {
        val csvContent = StringBuilder()

        csvContent.append("№;Наименование;Штрих-код;Ожидалось;Фактически;Расхождение\n")

        document.items.forEachIndexed { index, item ->
            val product = item.product
            val discrepancy = item.quantityActual - item.quantityExpected

            csvContent.append("${index + 1};")
            csvContent.append("${product?.name ?: "Товар ${item.productId}"};")
            csvContent.append("${product?.barcode ?: "N/A"};")
            csvContent.append("${item.quantityExpected};")
            csvContent.append("${item.quantityActual};")
            csvContent.append("$discrepancy\n")
        }

        csvContent.append("\n")
        csvContent.append("ИТОГО;;;")
        csvContent.append("${document.items.sumOf { it.quantityExpected }};")
        csvContent.append("${document.items.sumOf { it.quantityActual }};")
        csvContent.append("${document.items.sumOf { it.quantityActual - it.quantityExpected }}\n")

        val file = File(context.getExternalFilesDir(null), "report_${document.number}.csv")
        file.writeText(csvContent.toString(), Charsets.UTF_8)

        return file
    }

    private fun createEmailBody(document: Document): String {
        return """
            Отчет по инвентаризации
            
            Документ: ${document.number}
            Склад: ${document.warehouse?.name ?: "Не указан"}
            Дата: ${document.documentDate}
            Статус: ${getStatusText(document.status)}
            
            Всего товаров: ${document.items.size}
            Совпало: ${document.items.count { it.quantityExpected == it.quantityActual }}
            Расхождения: ${document.items.count { it.quantityExpected != it.quantityActual }}
            
            Во вложении подробный отчет в формате PDF.
            
            С уважением,
            Приложение Инвентаризация
        """.trimIndent()
    }
}