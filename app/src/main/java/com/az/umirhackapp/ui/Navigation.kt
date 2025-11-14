package com.az.umirhackapp.ui

enum class Screen(
    val route: String,
    val title: String = "" // Опционально, для отображения в UI
) {
    REGISTRATION(
        route = "registration",
        title = "Регистрация"
    ),
    LOGIN(
        route = "login",
        title = "Вход"
    ),
    LOADING(
        route = "loading",
        title = "Загрузка"
    ),
    PROFILE(
        route = "profile",
        title = "Профиль"
    ),
    SETTINGS(
        route = "settings",
        title = "Настройки"
    ),
    MAIN_ORGANIZATIONS(
        route = "main/organizations",
        title = "Организации"
    ),
    MAIN_WAREHOUSES(
        route = "main/warehouses",
        title = "Склады"
    ),
    MAIN_DOCUMENTS(
        route = "main/documents",
        title = "Документы"
    ),
    MAIN_DOCUMENT_ITEMS(
        route = "main/document",
        title = "Документ"
    ),
    QR_SCANNER(
        route = "scanner_qr",
        title = "Сканер QR-кодов"
    ),
    PERMISSION_REQUEST(
        route = "permission_request",
        title = "Запрос разрешений"
    )
}