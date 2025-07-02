package com.loc.inventory_service.controller

import com.loc.inventory_service.application.service.CheckStockService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.loc.inventory_service.controller.mapper.toModel
import com.loc.inventoryservice.model.StockCheckRequest
import org.springframework.web.bind.annotation.ModelAttribute

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
    private val checkStockService: CheckStockService
) {
    @GetMapping("/check-stock")
    fun isInStock(@ModelAttribute stockCheckRequest: StockCheckRequest): Boolean {
        val inventory = stockCheckRequest.toModel()
        return checkStockService.isInStock(inventory)
    }
}