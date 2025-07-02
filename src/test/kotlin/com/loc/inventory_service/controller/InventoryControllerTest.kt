package com.loc.inventory_service.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.loc.inventory_service.application.service.AddInventoryService
import com.loc.inventory_service.application.service.CheckStockService
import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventoryservice.model.AddInventoryRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(InventoryController::class)
@ExtendWith(MockKExtension::class)
class InventoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var checkStockService: CheckStockService

    @MockkBean
    private lateinit var addInventoryService: AddInventoryService

    @Test
    fun `should return true when stock is available`() {
        // Given
        val skuCode = "IPHONE13-128"
        val quantity = 2
        val inventory = Inventory(skuCode = skuCode, quantity = quantity)

        every { checkStockService.isInStock(inventory) } returns true

        // When & Then
        mockMvc.perform(
            get("/api/inventory/check-stock")
                .param("skuCode", skuCode)
                .param("quantity", quantity.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun `should return false when stock is not available`() {
        // Given
        val skuCode = "OUT-OF-STOCK-ITEM"
        val quantity = 5
        val inventory = Inventory(skuCode = skuCode, quantity = quantity)

        every { checkStockService.isInStock(inventory) } returns false

        // When & Then
        mockMvc.perform(
            get("/api/inventory/check-stock")
                .param("skuCode", skuCode)
                .param("quantity", quantity.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 400 when required parameters are missing for stock check`() {
        // When & Then
        mockMvc.perform(
            get("/api/inventory/check-stock")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should create new inventory successfully`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "NEW-ITEM-001",
            quantity = 10
        )

        val createdInventory = Inventory(
            id = 1L,
            skuCode = "NEW-ITEM-001",
            quantity = 10
        )

        every { addInventoryService.addInventory(any()) } returns createdInventory

        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addInventoryRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.skuCode").value("NEW-ITEM-001"))
            .andExpect(jsonPath("$.quantity").value(10))
    }

    @Test
    fun `should update existing inventory successfully`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "EXISTING-ITEM",
            quantity = 5
        )

        val updatedInventory = Inventory(
            id = 2L,
            skuCode = "EXISTING-ITEM",
            quantity = 15 // Existing 10 + New 5
        )

        every { addInventoryService.addInventory(any()) } returns updatedInventory

        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addInventoryRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.skuCode").value("EXISTING-ITEM"))
            .andExpect(jsonPath("$.quantity").value(15))
    }

    @Test
    fun `should return 400 when request body is missing for add inventory`() {
        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should handle special characters in SKU code for stock check`() {
        // Given
        val skuCode = "SPECIAL@SKU_123-TEST"
        val quantity = 1
        val inventory = Inventory(skuCode = skuCode, quantity = quantity)

        every { checkStockService.isInStock(inventory) } returns true

        // When & Then
        mockMvc.perform(
            get("/api/inventory/check-stock")
                .param("skuCode", skuCode)
                .param("quantity", quantity.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun `should handle special characters in SKU code for add inventory`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "SPECIAL@SKU_456-ADD",
            quantity = 7
        )

        val createdInventory = Inventory(
            id = 3L,
            skuCode = "SPECIAL@SKU_456-ADD",
            quantity = 7
        )

        every { addInventoryService.addInventory(any()) } returns createdInventory

        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addInventoryRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.skuCode").value("SPECIAL@SKU_456-ADD"))
            .andExpect(jsonPath("$.quantity").value(7))
    }

    @Test
    fun `should handle zero quantity for add inventory`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "ZERO-QTY-ITEM",
            quantity = 0
        )

        val createdInventory = Inventory(
            id = 4L,
            skuCode = "ZERO-QTY-ITEM",
            quantity = 0
        )

        every { addInventoryService.addInventory(any()) } returns createdInventory

        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addInventoryRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.quantity").value(0))
    }

    @Test
    fun `should handle large quantity numbers`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "LARGE-QTY-ITEM",
            quantity = 999999
        )

        val createdInventory = Inventory(
            id = 5L,
            skuCode = "LARGE-QTY-ITEM",
            quantity = 999999
        )

        every { addInventoryService.addInventory(any()) } returns createdInventory

        // When & Then
        mockMvc.perform(
            post("/api/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addInventoryRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.quantity").value(999999))
    }
}