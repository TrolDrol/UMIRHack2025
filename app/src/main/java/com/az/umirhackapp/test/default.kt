package com.az.umirhackapp.test

import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Product
import com.az.umirhackapp.server.Warehouse

val organizations = listOf(
    Organization(
        id = 1,
        name = "ООО 'Ромашка'",
        inn = "7712345678",
        address = "г. Москва, ул. Ленина, д. 10",
        phone = "+7 (495) 123-45-67",
        userRole = "admin"
    ),
    Organization(
        id = 2,
        name = "АО 'ТехноПром'",
        inn = "7723456789",
        address = "г. Санкт-Петербург, Невский пр-т, д. 25",
        phone = "+7 (812) 234-56-78",
        userRole = "storekeeper"
    ),
    Organization(
        id = 3,
        name = "ИП Иванов И.И.",
        inn = "7734567890",
        address = "г. Екатеринбург, ул. Мира, д. 15",
        phone = "+7 (343) 345-67-89",
        userRole = "auditor"
    ),
    Organization(
        id = 4,
        name = "ЗАО 'СтройГрад'",
        inn = "7745678901",
        address = "г. Новосибирск, ул. Строителей, д. 5",
        phone = "+7 (383) 456-78-90",
        userRole = "storekeeper"
    ),
    Organization(
        id = 5,
        name = "ООО 'АгроПродукт'",
        inn = "7756789012",
        address = "г. Казань, ул. Полевая, д. 30",
        phone = "+7 (843) 567-89-01",
        userRole = "admin"
    ),
    Organization(
        id = 6,
        name = "ПАО 'МеталлТрейд'",
        inn = "7767890123",
        address = "г. Нижний Новгород, ул. Заводская, д. 8",
        phone = "+7 (831) 678-90-12",
        userRole = "auditor"
    ),
    Organization(
        id = 7,
        name = "ООО 'ФармаМед'",
        inn = "7778901234",
        address = "г. Ростов-на-Дону, ул. Медицинская, д. 12",
        phone = "+7 (863) 789-01-23",
        userRole = "storekeeper"
    ),
    Organization(
        id = 8,
        name = "ИП Петрова С.К.",
        inn = "7789012345",
        address = "г. Краснодар, ул. Красная, д. 100",
        phone = "+7 (861) 890-12-34",
        userRole = "admin"
    ),
    Organization(
        id = 9,
        name = "АО 'ЭлектроСистемы'",
        inn = "7790123456",
        address = "г. Воронеж, ул. Энергетиков, д. 7",
        phone = "+7 (473) 901-23-45",
        userRole = "storekeeper"
    ),
    Organization(
        id = 10,
        name = "ООО 'ЛогистикГрупп'",
        inn = "7701234567",
        address = "г. Самара, ул. Транспортная, д. 20",
        phone = "+7 (846) 012-34-56",
        userRole = "auditor"
    )
)

val warehouses = listOf(
    Warehouse(
        id = 1,
        name = "Основной склад",
        address = "г. Москва, ул. Складская, д. 1",
        organizationId = 1,
        isActive = true
    ),
    Warehouse(
        id = 2,
        name = "Склад №2",
        address = "г. Москва, ул. Заводская, д. 15",
        organizationId = 1,
        isActive = true
    ),
    Warehouse(
        id = 3,
        name = "Центральный склад",
        address = "г. Санкт-Петербург, пр-т Складской, д. 25",
        organizationId = 2,
        isActive = true
    ),
    Warehouse(
        id = 4,
        name = "Резервный склад",
        address = "г. Екатеринбург, ул. Резервная, д. 5",
        organizationId = 3,
        isActive = false
    ),
    Warehouse(
        id = 5,
        name = "Склад готовой продукции",
        address = "г. Казань, ул. Промышленная, д. 10",
        organizationId = 5,
        isActive = true
    ),
    Warehouse(
        id = 6,
        name = "Сырьевой склад",
        address = null, // Адрес не указан
        organizationId = 5,
        isActive = true
    )
)

val products = listOf(
    Product(
        id = 1,
        name = "Смартфон Samsung Galaxy S23",
        barcode = "8801643241234",
        description = "Смартфон 128GB, черный",
        organizationId = 1
    ),
    Product(
        id = 2,
        name = "Ноутбук Lenovo IdeaPad 5",
        barcode = "8881643256789",
        description = "Ноутбук 15.6\", 8GB RAM, 512GB SSD",
        organizationId = 1
    ),
    Product(
        id = 3,
        name = "Наушники Sony WH-1000XM4",
        barcode = "4905524931245",
        description = "Беспроводные наушники с шумоподавлением",
        organizationId = 1
    ),
    Product(
        id = 4,
        name = "Молоко 'Домик в деревне' 3.2%",
        barcode = "4607023232456",
        description = "Молоко пастеризованное, 1л",
        organizationId = 2
    ),
    Product(
        id = 5,
        name = "Хлеб 'Бородинский'",
        barcode = "4601234567890",
        description = "Хлеб ржаной, 500г",
        organizationId = 2
    ),
    Product(
        id = 6,
        name = "Сталь листовая 3мм",
        barcode = "2001234567891",
        description = "Лист стальной 1000x2000x3мм",
        organizationId = 3
    ),
    Product(
        id = 7,
        name = "Парацетамол 500мг",
        barcode = "4601234567892",
        description = "Таблетки 20шт",
        organizationId = 4
    ),
    Product(
        id = 8,
        name = "Кофе Jacobs Monarch",
        barcode = "4001234567893",
        description = "Кофе молотый, 250г",
        organizationId = 5
    ),
    Product(
        id = 9,
        name = "Без штрих-кода товар",
        barcode = null, // Товар без штрих-кода
        description = "Товар без штрихкода для ручного ввода",
        organizationId = 1
    ),
    Product(
        id = 10,
        name = "Мыло хозяйственное",
        barcode = "4601234567894",
        description = null, // Описание отсутствует
        organizationId = 2
    )
)

val documents = listOf(
    Document(
        id = 1,
        type = "inventory",
        number = "ИНВ-2024-0001",
        status = "draft",
        dateCreated = "2024-01-15T10:30:00",
        documentDate = "2024-01-15",
        organizationId = 1,
        warehouseId = 1,
        createdById = 1,
        warehouse = warehouses[0],
        items = mutableListOf(
            DocumentItem(
                id = 1,
                documentId = 1,
                productId = 1,
                quantityExpected = 50.0,
                quantityActual = 48.0,
                product = products[0]
            ),
            DocumentItem(
                id = 2,
                documentId = 1,
                productId = 2,
                quantityExpected = 25.0,
                quantityActual = 25.0,
                product = products[1]
            )
        )
    ),
    Document(
        id = 2,
        type = "inventory",
        number = "ИНВ-2024-0002",
        status = "in_progress",
        dateCreated = "2024-01-16T09:15:00",
        documentDate = "2024-01-16",
        organizationId = 1,
        warehouseId = 2,
        createdById = 1,
        warehouse = warehouses[1],
        items = mutableListOf(
            DocumentItem(
                id = 3,
                documentId = 2,
                productId = 3,
                quantityExpected = 100.0,
                quantityActual = 95.0,
                product = products[2]
            )
        )
    ),
    Document(
        id = 3,
        type = "inventory",
        number = "ИНВ-2024-0003",
        status = "completed",
        dateCreated = "2024-01-14T14:20:00",
        documentDate = "2024-01-14",
        organizationId = 2,
        warehouseId = 3,
        createdById = 2,
        warehouse = warehouses[2],
        items = mutableListOf(
            DocumentItem(
                id = 4,
                documentId = 3,
                productId = 4,
                quantityExpected = 200.0,
                quantityActual = 200.0,
                product = products[3]
            ),
            DocumentItem(
                id = 5,
                documentId = 3,
                productId = 5,
                quantityExpected = 150.0,
                quantityActual = 148.0,
                product = products[4]
            )
        )
    ),
    Document(
        id = 4,
        type = "waybill",
        number = "НК-2024-0001",
        status = "draft",
        dateCreated = "2024-01-17T11:45:00",
        documentDate = "2024-01-17",
        organizationId = 3,
        warehouseId = 4,
        createdById = 3,
        warehouse = warehouses[3],
        items = mutableListOf() // Пустой документ
    ),
    Document(
        id = 5,
        type = "inventory",
        number = "ИНВ-2024-0004",
        status = "cancelled",
        dateCreated = "2024-01-10T16:30:00",
        documentDate = "2024-01-10",
        organizationId = 5,
        warehouseId = 5,
        createdById = 5,
        warehouse = warehouses[4],
        items = mutableListOf(
            DocumentItem(
                id = 6,
                documentId = 5,
                productId = 8,
                quantityExpected = 80.0,
                quantityActual = 0.0,
                product = products[7]
            )
        )
    )
)

val documentItems = listOf(
    DocumentItem(
        id = 1,
        documentId = 1,
        productId = 1,
        quantityExpected = 50.0,
        quantityActual = 48.0,
        product = products[0]
    ),
    DocumentItem(
        id = 2,
        documentId = 1,
        productId = 2,
        quantityExpected = 25.0,
        quantityActual = 25.0,
        product = products[1]
    ),
    DocumentItem(
        id = 3,
        documentId = 2,
        productId = 3,
        quantityExpected = 100.0,
        quantityActual = 95.0,
        product = products[2]
    ),
    DocumentItem(
        id = 4,
        documentId = 3,
        productId = 4,
        quantityExpected = 200.0,
        quantityActual = 200.0,
        product = products[3]
    ),
    DocumentItem(
        id = 5,
        documentId = 3,
        productId = 5,
        quantityExpected = 150.0,
        quantityActual = 148.0,
        product = products[4]
    ),
    DocumentItem(
        id = 6,
        documentId = 5,
        productId = 8,
        quantityExpected = 80.0,
        quantityActual = 0.0,
        product = products[7]
    )
)