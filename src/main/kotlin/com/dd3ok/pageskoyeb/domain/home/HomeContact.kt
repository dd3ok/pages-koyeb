package com.dd3ok.pageskoyeb.domain.home

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.home.vo.ContactEmail
import com.dd3ok.pageskoyeb.domain.home.vo.ContactMessage
import com.dd3ok.pageskoyeb.domain.home.vo.ContactName
import java.time.LocalDateTime

data class HomeContact private constructor(
    val id: Long? = null,
    val name: ContactName,
    val email: ContactEmail,
    val message: ContactMessage,
    val ipAddress: IpAddress,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    companion object {
        fun create(
            name: ContactName,
            email: ContactEmail,
            message: ContactMessage,
            ipAddress: IpAddress
        ): HomeContact {
            return HomeContact(
                name = name,
                email = email,
                message = message,
                ipAddress = ipAddress
            )
        }
        
        fun fromRepository(
            id: Long,
            name: ContactName,
            email: ContactEmail,
            message: ContactMessage,
            ipAddress: IpAddress,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): HomeContact {
            return HomeContact(
                id = id,
                name = name,
                email = email,
                message = message,
                ipAddress = ipAddress,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
