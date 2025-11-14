package com.az.umirhackapp.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Warehouse
import com.az.umirhackapp.test.organizations
import com.az.umirhackapp.ui.Screen

@Composable
fun <T> LazyColumnItems(
    items: List<T>,
    onClick: (T) -> Unit,
    route: Screen,
    searchText: String
) {
    var filteredList = filterList(items, route, searchText)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filteredList.size) { item ->
            when (route) {
                Screen.REGISTRATION -> TODO()
                Screen.LOGIN -> TODO()
                Screen.LOADING -> TODO()
                Screen.PROFILE -> TODO()
                Screen.SETTINGS -> TODO()
                Screen.MAIN_ORGANIZATIONS ->
                    MainItemOrganizationCard(
                        filteredList[item] as Organization,
                        onClick as (Organization) -> Unit
                    )

                Screen.MAIN_WAREHOUSES ->
                    MainItemWarehouseCard(
                        filteredList[item] as Warehouse,
                        onClick as (Warehouse) -> Unit
                    )

                Screen.MAIN_DOCUMENTS ->
                    MainItemDocumentsCard(
                        filteredList[item] as Document,
                        onClick as (Document) -> Unit
                    )

                Screen.MAIN_DOCUMENT_ITEMS -> TODO()
                Screen.QR_SCANNER -> TODO()
                Screen.PERMISSION_REQUEST -> TODO()
            }
        }
        if (filteredList.isEmpty())
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        when(route) {
                            Screen.REGISTRATION -> TODO()
                            Screen.LOGIN -> TODO()
                            Screen.LOADING -> TODO()
                            Screen.PROFILE -> TODO()
                            Screen.SETTINGS -> TODO()
                            Screen.MAIN_ORGANIZATIONS ->
                                "Не найдены доступные организации"
                            Screen.MAIN_WAREHOUSES ->
                                "Не найдены доступные склады"
                            Screen.MAIN_DOCUMENTS ->
                                "Не найдены доступные документы"
                            Screen.MAIN_DOCUMENT_ITEMS -> TODO()
                            Screen.QR_SCANNER -> TODO()
                            Screen.PERMISSION_REQUEST -> TODO()
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
    }
}

private fun <T> filterList(items: List<T>, route: Screen, searchText: String): List<T> {
    var filteredList = items
    if (searchText.isNotEmpty())
        when (route) {
            Screen.REGISTRATION -> TODO()
            Screen.LOGIN -> TODO()
            Screen.LOADING -> TODO()
            Screen.PROFILE -> TODO()
            Screen.SETTINGS -> TODO()
            Screen.MAIN_ORGANIZATIONS ->
                filteredList = items.filter { (it as Organization).name.lowercase().contains(searchText) }
            Screen.MAIN_WAREHOUSES ->
                filteredList = items.filter { (it as Warehouse).name.lowercase().contains(searchText) }
            Screen.MAIN_DOCUMENTS ->
                filteredList = items.filter { (it as Document).number.lowercase().contains(searchText) }
            Screen.MAIN_DOCUMENT_ITEMS -> TODO()
            Screen.QR_SCANNER -> TODO()
            Screen.PERMISSION_REQUEST -> TODO()
        }
    return filteredList
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLazyColumnItems() {
    LazyColumnItems(
        organizations,
        {_ ->},
        Screen.MAIN_ORGANIZATIONS,
        ""
    )
}