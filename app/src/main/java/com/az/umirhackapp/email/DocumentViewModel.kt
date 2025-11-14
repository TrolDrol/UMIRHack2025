package com.az.umirhackapp.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.az.umirhackapp.server.Document
import kotlinx.coroutines.launch

class EmailViewModel(
    val reportGenerator: ReportGenerator
) : ViewModel() {
    fun sendReport(document: Document, email: String, includePDF: Boolean, includeCSV: Boolean) {
        viewModelScope.launch {
            try {
                reportGenerator.generateAndSendInventoryReport(document, email, includePDF, includeCSV)
                println("Отчет отправлен на $email")
            } catch (e: Exception) {
                println("Ошибка отправки отчета: ${e.message}")
            }
        }
    }
}