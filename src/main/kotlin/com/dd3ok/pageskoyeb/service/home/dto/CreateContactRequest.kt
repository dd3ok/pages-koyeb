package com.dd3ok.pageskoyeb.service.home.dto

import com.dd3ok.pageskoyeb.domain.home.HomeContact
import java.time.format.DateTimeFormatter

data class CreateContactRequest(
    val name: String,
    val email: String,
    val message: String
)

data class ContactResponse(
    val id: String,
    val name: String,
    val email: String,
    val message: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(contact: HomeContact): ContactResponse {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return ContactResponse(
                id = contact.id ?: "",
                name = contact.name.getValue(),
                email = contact.email.getValue(),
                message = contact.message.getValue(),
                createdAt = contact.createdAt.format(formatter),
                updatedAt = contact.updatedAt.format(formatter)
            )
        }
    }
}
