package com.loc.inventory_service.controller

import com.loc.inventory_service.application.service.AddInventoryService
import com.loc.inventory_service.application.service.CheckStockService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.loc.inventory_service.controller.mapper.toModel
import com.loc.inventory_service.controller.mapper.toResponse
import com.loc.inventoryservice.model.AddInventoryRequest
import com.loc.inventoryservice.model.StockCheckRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
    private val checkStockService: CheckStockService,
    private val addInventoryService: AddInventoryService
) {
    @GetMapping("/check-stock")
    fun isInStock(@ModelAttribute stockCheckRequest: StockCheckRequest): Boolean {
        val inventory = stockCheckRequest.toModel()
        return checkStockService.isInStock(inventory)
    }

    @PostMapping("/add")
    fun addInventory(@RequestBody addInventoryRequest: AddInventoryRequest): ResponseEntity<Any> {
        val result = addInventoryService.addInventory(addInventoryRequest.toModel())

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toResponse())
    }

}