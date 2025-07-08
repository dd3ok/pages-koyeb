package com.dd3ok.pageskoyeb.controller

import com.dd3ok.pageskoyeb.service.home.HomeContactService
import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse
import com.dd3ok.pageskoyeb.service.home.dto.CreateContactRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/home/contacts")
class HomeContactController(
    private val homeContactService: HomeContactService
) {
    
    @PostMapping
    fun createContact(
        @RequestBody request: CreateContactRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<ContactResponse> {
        val ipAddress = getClientIpAddress(httpRequest)
        val response = homeContactService.createContact(request, ipAddress)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping
    fun getContacts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ContactResponse>> {
        val response = homeContactService.getContacts(page, size)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/{id}")
    fun getContact(@PathVariable id: String): ResponseEntity<ContactResponse> {
        val response = homeContactService.getContact(id)
        return ResponseEntity.ok(response)
    }
    
    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: String): ResponseEntity<Void> {
        homeContactService.deleteContact(id)
        return ResponseEntity.noContent().build()
    }
    
    @GetMapping("/email/{email}")
    fun getContactsByEmail(@PathVariable email: String): ResponseEntity<List<ContactResponse>> {
        val response = homeContactService.getContactsByEmail(email)
        return ResponseEntity.ok(response)
    }
    
    private fun getClientIpAddress(request: HttpServletRequest): String? {
        val xForwardedForHeader = request.getHeader("X-Forwarded-For")
        return when {
            xForwardedForHeader != null && xForwardedForHeader.isNotEmpty() -> 
                xForwardedForHeader.split(",")[0].trim()
            else -> request.remoteAddr
        }
    }
}
