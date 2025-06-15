package com.dd3ok.pageskoyeb.domain.wedding

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentAuthor
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentMessage
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentPassword
import java.time.LocalDateTime

data class WeddingComment private constructor(
    val id: String? = null,
    val author: CommentAuthor,
    private val hashedPassword: String,
    val message: CommentMessage,
    val ipAddress: IpAddress,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    fun updateMessage(newMessage: CommentMessage): WeddingComment {
        return copy(
            message = newMessage,
            updatedAt = LocalDateTime.now()
        )
    }
    
    fun verifyPassword(rawPassword: CommentPassword, passwordMatcher: (String, String) -> Boolean): Boolean {
        return passwordMatcher(rawPassword.getValue(), hashedPassword)
    }
    
    fun getHashedPassword(): String = hashedPassword
    
    companion object {
        fun create(
            author: CommentAuthor,
            password: CommentPassword,
            message: CommentMessage,
            ipAddress: IpAddress,
            passwordEncoder: (String) -> String
        ): WeddingComment {
            return WeddingComment(
                author = author,
                hashedPassword = passwordEncoder(password.getValue()),
                message = message,
                ipAddress = ipAddress
            )
        }
        
        fun fromRepository(
            id: String? = null,
            author: CommentAuthor,
            hashedPassword: String,
            message: CommentMessage,
            ipAddress: IpAddress,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): WeddingComment {
            return WeddingComment(
                id = id,
                author = author,
                hashedPassword = hashedPassword,
                message = message,
                ipAddress = ipAddress,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
