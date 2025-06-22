package com.dd3ok.pageskoyeb.infrastructure.web

import com.dd3ok.pageskoyeb.application.home.HomeContactService
import com.dd3ok.pageskoyeb.application.home.dto.ContactResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/healthcheck")
class HealthCheckController(
    private val homeContactService: HomeContactService
) {

    @GetMapping
    fun healthCheck(): ResponseEntity<List<ContactResponse>> {
        val response = homeContactService.getContactsByEmail("healthcheck@dd3ok.com")
        return ResponseEntity.ok(response)
    }
}
