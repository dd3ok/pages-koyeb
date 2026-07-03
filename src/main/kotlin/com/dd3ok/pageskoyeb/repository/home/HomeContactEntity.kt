package com.dd3ok.pageskoyeb.repository.home

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "home_contacts")
data class HomeContactEntity(
    @Id
    val id: String? = null,

    @Field("name")
    val name: String,

    @Indexed
    @Field("email")
    val email: String,

    @Field("message")
    val message: String,

    @Field("ip_address")
    val ipAddress: String? = null,

    @CreatedDate
    @Indexed(direction = IndexDirection.DESCENDING)
    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Field("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(null, "", "", "", null)
}
