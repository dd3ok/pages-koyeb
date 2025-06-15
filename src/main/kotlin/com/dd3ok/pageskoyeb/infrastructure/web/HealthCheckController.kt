package com.dd3ok.pageskoyeb.infrastructure.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/healthcheck")
class HealthCheckController {
    
    @GetMapping
    fun healthCheck(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to "UP",
            "timestamp" to System.currentTimeMillis(),
            "message" to "Application is running"
        )
        return ResponseEntity.ok(response)
    }
}
