package com.loc.inventory_service.application

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.application.service.AddInventoryService
import com.loc.inventory_service.domain.model.Inventory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddInventoryServiceTest {

    private lateinit var inventoryRepositoryPort: InventoryRepositoryPort
    private lateinit var addInventoryService: AddInventoryService

    @BeforeEach
    fun setUp() {
        inventoryRepositoryPort = mockk()
        addInventoryService = AddInventoryService(inventoryRepositoryPort)
    }

    @Test
    fun `should create new inventory when SKU does not exist`() {
        // Given
        val newInventory = Inventory(
            skuCode = "NEW-SKU-001",
            quantity = 10
        )
        every { inventoryRepositoryPort.findBySkuCode(newInventory) } returns null
        every { inventoryRepositoryPort.addInventory(newInventory) } returns newInventory.copy(id = 1L)

        // When
        val result = addInventoryService.addInventory(newInventory)

        // Then
        assertEquals(1L, result.id)
        assertEquals("NEW-SKU-001", result.skuCode)
        assertEquals(10, result.quantity)

        verify { inventoryRepositoryPort.findBySkuCode(newInventory) }
        verify { inventoryRepositoryPort.addInventory(newInventory) }
    }

    @Test
    fun `should update existing inventory when SKU already exists`() {
        // Given
        val existingInventory = Inventory(
            id = 1L,
            skuCode = "EXISTING-SKU",
            quantity = 15
        )
        val newInventory = Inventory(
            skuCode = "EXISTING-SKU",
            quantity = 5
        )
        val expectedUpdatedInventory = existingInventory.copy(quantity = 20) // 15 + 5

        every { inventoryRepositoryPort.findBySkuCode(newInventory) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedUpdatedInventory) } returns expectedUpdatedInventory

        // When
        val result = addInventoryService.addInventory(newInventory)

        // Then
        assertEquals(1L, result.id)
        assertEquals("EXISTING-SKU", result.skuCode)
        assertEquals(20, result.quantity)

        verify { inventoryRepositoryPort.findBySkuCode(newInventory) }
        verify { inventoryRepositoryPort.addInventory(expectedUpdatedInventory) }
    }

    @Test
    fun `should add quantity correctly when updating existing inventory`() {
        // Given
        val existingInventory = Inventory(
            id = 2L,
            skuCode = "TEST-SKU",
            quantity = 100
        )
        val additionalInventory = Inventory(
            skuCode = "TEST-SKU",
            quantity = 50
        )
        val expectedResult = existingInventory.copy(quantity = 150)

        every { inventoryRepositoryPort.findBySkuCode(additionalInventory) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedResult) } returns expectedResult

        // When
        val result = addInventoryService.addInventory(additionalInventory)

        // Then
        assertEquals(150, result.quantity)
        verify { inventoryRepositoryPort.findBySkuCode(additionalInventory) }
        verify { inventoryRepositoryPort.addInventory(expectedResult) }
    }

    @Test
    fun `should handle zero quantity addition to existing inventory`() {
        // Given
        val existingInventory = Inventory(
            id = 3L,
            skuCode = "ZERO-ADD-SKU",
            quantity = 25
        )
        val zeroAddition = Inventory(
            skuCode = "ZERO-ADD-SKU",
            quantity = 0
        )
        val expectedResult = existingInventory.copy(quantity = 25) // 25 + 0 = 25

        every { inventoryRepositoryPort.findBySkuCode(zeroAddition) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedResult) } returns expectedResult

        // When
        val result = addInventoryService.addInventory(zeroAddition)

        // Then
        assertEquals(25, result.quantity)
        verify { inventoryRepositoryPort.findBySkuCode(zeroAddition) }
        verify { inventoryRepositoryPort.addInventory(expectedResult) }
    }

    @Test
    fun `should create new inventory with zero quantity`() {
        // Given
        val zeroQuantityInventory = Inventory(
            skuCode = "ZERO-QTY-SKU",
            quantity = 0
        )
        every { inventoryRepositoryPort.findBySkuCode(zeroQuantityInventory) } returns null
        every { inventoryRepositoryPort.addInventory(zeroQuantityInventory) } returns zeroQuantityInventory.copy(id = 4L)

        // When
        val result = addInventoryService.addInventory(zeroQuantityInventory)

        // Then
        assertEquals(4L, result.id)
        assertEquals("ZERO-QTY-SKU", result.skuCode)
        assertEquals(0, result.quantity)

        verify { inventoryRepositoryPort.findBySkuCode(zeroQuantityInventory) }
        verify { inventoryRepositoryPort.addInventory(zeroQuantityInventory) }
    }

    @Test
    fun `should handle large quantity numbers`() {
        // Given
        val existingInventory = Inventory(
            id = 5L,
            skuCode = "LARGE-QTY-SKU",
            quantity = 999999
        )
        val largeAddition = Inventory(
            skuCode = "LARGE-QTY-SKU",
            quantity = 1000000
        )
        val expectedResult = existingInventory.copy(quantity = 1999999)

        every { inventoryRepositoryPort.findBySkuCode(largeAddition) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedResult) } returns expectedResult

        // When
        val result = addInventoryService.addInventory(largeAddition)

        // Then
        assertEquals(1999999, result.quantity)
        verify { inventoryRepositoryPort.findBySkuCode(largeAddition) }
        verify { inventoryRepositoryPort.addInventory(expectedResult) }
    }

    @Test
    fun `should handle special characters in SKU code for new inventory`() {
        // Given
        val specialSkuInventory = Inventory(
            skuCode = "SPECIAL@SKU_123-TEST",
            quantity = 7
        )
        every { inventoryRepositoryPort.findBySkuCode(specialSkuInventory) } returns null
        every { inventoryRepositoryPort.addInventory(specialSkuInventory) } returns specialSkuInventory.copy(id = 6L)

        // When
        val result = addInventoryService.addInventory(specialSkuInventory)

        // Then
        assertEquals(6L, result.id)
        assertEquals("SPECIAL@SKU_123-TEST", result.skuCode)
        assertEquals(7, result.quantity)

        verify { inventoryRepositoryPort.findBySkuCode(specialSkuInventory) }
        verify { inventoryRepositoryPort.addInventory(specialSkuInventory) }
    }

    @Test
    fun `should handle special characters in SKU code for existing inventory`() {
        // Given
        val existingInventory = Inventory(
            id = 7L,
            skuCode = "SPECIAL@SKU_456-UPDATE",
            quantity = 12
        )
        val updateInventory = Inventory(
            skuCode = "SPECIAL@SKU_456-UPDATE",
            quantity = 3
        )
        val expectedResult = existingInventory.copy(quantity = 15)

        every { inventoryRepositoryPort.findBySkuCode(updateInventory) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedResult) } returns expectedResult

        // When
        val result = addInventoryService.addInventory(updateInventory)

        // Then
        assertEquals(15, result.quantity)
        verify { inventoryRepositoryPort.findBySkuCode(updateInventory) }
        verify { inventoryRepositoryPort.addInventory(expectedResult) }
    }

    @Test
    fun `should verify repository methods are called exactly once for new inventory`() {
        // Given
        val inventory = Inventory(
            skuCode = "VERIFY-NEW-SKU",
            quantity = 1
        )
        every { inventoryRepositoryPort.findBySkuCode(inventory) } returns null
        every { inventoryRepositoryPort.addInventory(inventory) } returns inventory.copy(id = 8L)

        // When
        addInventoryService.addInventory(inventory)

        // Then
        verify(exactly = 1) { inventoryRepositoryPort.findBySkuCode(inventory) }
        verify(exactly = 1) { inventoryRepositoryPort.addInventory(inventory) }
    }

    @Test
    fun `should verify repository methods are called exactly once for existing inventory`() {
        // Given
        val existingInventory = Inventory(
            id = 9L,
            skuCode = "VERIFY-EXISTING-SKU",
            quantity = 8
        )
        val updateInventory = Inventory(
            skuCode = "VERIFY-EXISTING-SKU",
            quantity = 2
        )
        val expectedUpdate = existingInventory.copy(quantity = 10)

        every { inventoryRepositoryPort.findBySkuCode(updateInventory) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedUpdate) } returns expectedUpdate

        // When
        addInventoryService.addInventory(updateInventory)

        // Then
        verify(exactly = 1) { inventoryRepositoryPort.findBySkuCode(updateInventory) }
        verify(exactly = 1) { inventoryRepositoryPort.addInventory(expectedUpdate) }
    }

    @Test
    fun `should preserve existing inventory ID when updating`() {
        // Given
        val existingInventory = Inventory(
            id = 999L,
            skuCode = "PRESERVE-ID-SKU",
            quantity = 5
        )
        val additionInventory = Inventory(
            skuCode = "PRESERVE-ID-SKU",
            quantity = 3
        )
        val expectedResult = existingInventory.copy(quantity = 8)

        every { inventoryRepositoryPort.findBySkuCode(additionInventory) } returns existingInventory
        every { inventoryRepositoryPort.addInventory(expectedResult) } returns expectedResult

        // When
        val result = addInventoryService.addInventory(additionInventory)

        // Then
        assertEquals(999L, result.id) // ID should be preserved
        assertEquals(8, result.quantity)
        verify { inventoryRepositoryPort.addInventory(expectedResult) }
    }
}