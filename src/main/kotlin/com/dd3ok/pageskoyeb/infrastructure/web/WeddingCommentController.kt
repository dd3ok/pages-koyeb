package com.dd3ok.pageskoyeb.infrastructure.web

import com.dd3ok.pageskoyeb.application.wedding.WeddingCommentService
import com.dd3ok.pageskoyeb.application.wedding.dto.CommentResponse
import com.dd3ok.pageskoyeb.application.wedding.dto.CreateCommentRequest
import com.dd3ok.pageskoyeb.application.wedding.dto.DeleteCommentRequest
import com.dd3ok.pageskoyeb.application.wedding.dto.UpdateCommentRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wedding/comments")
class WeddingCommentController(
    private val weddingCommentService: WeddingCommentService
) {
    
    @PostMapping
    fun createComment(
        @RequestBody request: CreateCommentRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<CommentResponse> {
        val ipAddress = getClientIpAddress(httpRequest)
        val response = weddingCommentService.createComment(request, ipAddress)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping
    fun getComments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<CommentResponse>> {
        val response = weddingCommentService.getComments(page, size)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/{id}")
    fun getComment(@PathVariable id: String): ResponseEntity<CommentResponse> {
        val response = weddingCommentService.getComment(id)
        return ResponseEntity.ok(response)
    }
    
    @PutMapping("/{id}")
    fun updateComment(
        @PathVariable id: String,
        @RequestBody request: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val response = weddingCommentService.updateComment(id, request)
        return ResponseEntity.ok(response)
    }
    
    @DeleteMapping("/{id}")
    fun deleteComment(
        @PathVariable id: String,
        @RequestBody request: DeleteCommentRequest
    ): ResponseEntity<Void> {
        weddingCommentService.deleteComment(id, request)
        return ResponseEntity.noContent().build()
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
