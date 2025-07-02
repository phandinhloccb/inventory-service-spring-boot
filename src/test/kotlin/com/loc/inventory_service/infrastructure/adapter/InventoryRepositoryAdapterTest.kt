package com.loc.inventory_service.infrastructure.adapter

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventory_service.infrastructure.entity.InventoryEntity
import com.loc.inventory_service.infrastructure.repository.JpaInventoryRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class InventoryRepositoryAdapterTest {
    
    private lateinit var jpaInventoryRepository: JpaInventoryRepository
    private lateinit var inventoryRepositoryAdapter: InventoryRepositoryAdapter
    
    @BeforeEach
    fun setUp() {
        jpaInventoryRepository = mockk()
        inventoryRepositoryAdapter = InventoryRepositoryAdapter(jpaInventoryRepository)
    }

    @Test
    fun `should save inventory and return inventory model`() {
        // Given
        val inventory = Inventory(
            skuCode = "SKU-123",
            quantity = 10
        )

        val savedEntity = InventoryEntity(
            id = 1L,
            skuCode = "SKU-123",
            quantity = 10,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { jpaInventoryRepository.save(any<InventoryEntity>()) } returns savedEntity

        // When
        val result = inventoryRepositoryAdapter.addInventory(inventory)

        // Then
        verify { jpaInventoryRepository.save(any<InventoryEntity>()) }
        
        assertEquals(1L, result.id)
        assertEquals("SKU-123", result.skuCode)
        assertEquals(10, result.quantity)
    }

    @Test
    fun `should return true when inventory exists with sufficient quantity`() {
        // Given
        val skuCode = "SKU-123"
        val requiredQuantity = 5

        every {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)
        } returns true

        // When
        val result = inventoryRepositoryAdapter.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)

        // Then
        assertTrue { result }
        verify {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)
        }
    }

    @Test
    fun `should return false when inventory exists but insufficient quantity`() {
        // Given
        val skuCode = "SKU-123"
        val requiredQuantity = 100

        every {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)
        } returns false

        // When
        val result = inventoryRepositoryAdapter.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)


        // Then
        assertFalse { result }
        verify {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual( skuCode, requiredQuantity)
        }
    }

    @Test
    fun `should return false when inventory does not exist`() {
        // Given
        val skuCode = "NON-EXISTENT-SKU"
        val requiredQuantity = 1
        every {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)
        } returns false

        // When
        val result = inventoryRepositoryAdapter.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)

        // Then
        assertFalse(result)
        verify {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, requiredQuantity)
        }
    }

    @Test
    fun `should find inventory entity by sku code when exists`() {
        // Given
        val inventory = Inventory(
            skuCode = "SKU-123",
            quantity = 10
        )
        val expectedEntity = InventoryEntity(
            id = 1L,
            skuCode = inventory.skuCode,
            quantity = 25,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { jpaInventoryRepository.findBySkuCode(inventory.skuCode) } returns expectedEntity

        // When
        val result = inventoryRepositoryAdapter.findBySkuCode(inventory)

        // Then
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("SKU-123", result?.skuCode)
        assertEquals(25, result?.quantity)
        verify { jpaInventoryRepository.findBySkuCode(inventory.skuCode) }
    }

    @Test
    fun `should return null when inventory entity not found by sku code`() {
        // Given
        val inventory = Inventory(
            skuCode = "NON-EXISTENT-SKU",
            quantity = 10
        )

        every { jpaInventoryRepository.findBySkuCode(inventory.skuCode) } returns null

        // When
        val result = inventoryRepositoryAdapter.findBySkuCode(inventory)

        // Then
        assertNull(result)
        verify { jpaInventoryRepository.findBySkuCode(inventory.skuCode) }
    }

    @Test
    fun `should find inventory with exact quantity match`() {
        // Given
        val inventory = Inventory(
            skuCode = "SKU-EXACT",
            quantity = 10
        )

        val entity = InventoryEntity(
            id = 2L,
            skuCode = inventory.skuCode,
            quantity = inventory.quantity,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { jpaInventoryRepository.findBySkuCode(inventory.skuCode) } returns entity

        // When
        val result = inventoryRepositoryAdapter.findBySkuCode(inventory)

        // Then
        assertNotNull(result)
        assertEquals(inventory.quantity, result?.quantity)
        verify { jpaInventoryRepository.findBySkuCode(inventory.skuCode) }
    }

    @Test
    fun `should check stock with edge case quantity zero`() {
        // Given
        val skuCode = "SKU-ZERO"
        val zeroQuantity = 0
        every {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, zeroQuantity)
        } returns true

        // When
        val result = inventoryRepositoryAdapter.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, zeroQuantity)

        // Then
        assertTrue(result)
        verify {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, zeroQuantity)
        }
    }

    
}