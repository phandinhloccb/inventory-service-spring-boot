package com.loc.inventory_service.application

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.application.service.CheckStockService
import com.loc.inventory_service.domain.model.Inventory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckStockServiceTest {

    private lateinit var inventoryRepositoryPort: InventoryRepositoryPort
    private lateinit var checkStockService: CheckStockService

    @BeforeEach
    fun setUp() {
        inventoryRepositoryPort = mockk()
        checkStockService = CheckStockService(inventoryRepositoryPort)
    }

    @BeforeEach
    fun clearMocks() {
        clearAllMocks(answers = false)
    }

    private val inventory = Inventory (
        skuCode = "SKU-TEST",
        quantity = 10
    )

    @Test
    fun `should return true when inventory has sufficient stock`() {
        // Given
        val inventory = Inventory(
            skuCode = "IPHONE13-128",
            quantity = 5
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("IPHONE13-128", 5)
        } returns true

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertTrue(result)
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("IPHONE13-128", 5)
        }
    }

    @Test
    fun `should return false when inventory has insufficient stock`() {
        // Given
        val inventory = Inventory(
            skuCode = "IPHONE13-128",
            quantity = 100
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("IPHONE13-128", 100)
        } returns false

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertFalse { result }
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("IPHONE13-128", 100)
        }
    }

    @Test
    fun `should return false when inventory does not exist`() {
        // Given
        val inventory = Inventory(
            skuCode = "NON-EXISTENT-SKU",
            quantity = 1
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("NON-EXISTENT-SKU", 1)
        } returns false

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertFalse{result}
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("NON-EXISTENT-SKU", 1)
        }
    }

    @Test
    fun `should return true when checking for zero quantity`() {
        // Given
        val inventory = Inventory(
            skuCode = "SAMSUNG-S23",
            quantity = 0
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("SAMSUNG-S23", 0)
        } returns true

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertTrue(result)
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("SAMSUNG-S23", 0)
        }
    }

    @Test
    fun `should return true when inventory has exact required quantity`() {
        // Given
        val inventory = Inventory(
            skuCode = "MACBOOK-PRO",
            quantity = 10
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("MACBOOK-PRO", 10)
        } returns true

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertTrue(result)
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("MACBOOK-PRO", 10)
        }
    }

    @Test
    fun `should return true when inventory has more than required quantity`() {
        // Given
        val inventory = Inventory(
            skuCode = "AIRPODS-PRO",
            quantity = 3
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("AIRPODS-PRO", 3)
        } returns true

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertTrue(result)
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("AIRPODS-PRO", 3)
        }
    }

    @Test
    fun `should handle special characters in sku code`() {
        // Given
        val inventory = Inventory(
            skuCode = "SPECIAL-SKU_123@TEST",
            quantity = 1
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("SPECIAL-SKU_123@TEST", 1)
        } returns true

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertTrue(result)
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("SPECIAL-SKU_123@TEST", 1)
        }
    }

    @Test
    fun `should handle large quantity numbers`() {
        // Given
        val inventory = Inventory(
            skuCode = "BULK-ITEM",
            quantity = 999999
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("BULK-ITEM", 999999)
        } returns false

        // When
        val result = checkStockService.isInStock(inventory)

        // Then
        assertFalse{result}
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("BULK-ITEM", 999999)
        }
    }

    @Test
    fun `should verify repository method is called exactly once`() {
        // Given
        val inventory = Inventory(
            skuCode = "TEST-SKU",
            quantity = 5
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("TEST-SKU", 5)
        } returns true

        // When
        checkStockService.isInStock(inventory)

        // Then
        verify(exactly = 1) {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual("TEST-SKU", 5)
        }
    }

    @Test
    fun `should pass correct parameters to repository`() {
        // Given
        val inventory = Inventory(
            id = 123L,
            skuCode = "VERIFY-PARAMS",
            quantity = 42
        )
        every {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual(any(), any())
        } returns true

        // When
        checkStockService.isInStock(inventory)

        // Then
        verify {
            inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual(
                eq("VERIFY-PARAMS"),
                eq(42)
            )
        }
    }




}