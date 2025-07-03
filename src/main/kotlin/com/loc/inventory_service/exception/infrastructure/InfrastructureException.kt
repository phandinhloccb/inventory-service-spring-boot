package com.loc.inventory_service.exception.infrastructure

abstract class InfrastructureException(
    message: String,
    cause: Throwable?
    ) : RuntimeException(message, cause)

// database exception
class DatabaseConnectionException(
    message: String = "Failed to connect to the database",
    cause: Throwable? = null
) : InfrastructureException(message, cause)