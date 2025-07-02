package com.loc.inventory_service.exception.handler

import com.loc.inventory_service.exception.infrastructure.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(ex: InfrastructureException): ResponseEntity<ErrorResponse> {
        val status = when (ex) {
            is DatabaseConnectionException -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status)
            .body(ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = status.value(),
                error = "Infrastructure Error",
                message = ex.message ?: "An infrastructure error occurred",
                path = "/api/inventory"
            ))
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(ex: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        logger.error { "Missing parameter: ${ex.parameterName}" }
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = "Required parameter '${ex.parameterName}' is missing",
                path = "/api/inventory/check-stock"
            )
        )
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        logger.error { "Type mismatch: ${ex.name} - ${ex.value}" }
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request", 
                message = "Invalid value for parameter '${ex.name}': ${ex.value}",
                path = "/api/inventory/check-stock"
            )
        )
    }

    data class ErrorResponse(
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
    )
} 