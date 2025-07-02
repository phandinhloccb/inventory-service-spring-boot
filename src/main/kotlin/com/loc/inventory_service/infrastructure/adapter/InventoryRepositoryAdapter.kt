package com.loc.inventory_service.infrastructure.adapter

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.exception.infrastructure.DatabaseConnectionException
import com.loc.inventory_service.infrastructure.repository.JpaInventoryRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Component
import java.sql.SQLException

@Component
class InventoryRepositoryAdapter(
    private val jpaInventoryRepository: JpaInventoryRepository
) : InventoryRepositoryPort {
    override fun existsBySkuCodeAndQuantityGreaterThanEqual(skuCode: String, quantity: Int): Boolean {
        return try {
            jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity)
        } catch (ex: DataAccessException) {
            throw DatabaseConnectionException("Failed to check stock for SKU: $skuCode", ex)
        } catch (ex: SQLException) {
            throw DatabaseConnectionException("SQL error while checking stock", ex)
        } catch (ex: Exception) {
            throw DatabaseConnectionException("Unexpected database error", ex)
        }
    }
}