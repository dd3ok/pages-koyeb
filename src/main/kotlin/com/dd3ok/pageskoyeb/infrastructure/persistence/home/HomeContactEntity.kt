package com.dd3ok.pageskoyeb.infrastructure.persistence.home

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "home_contact")
data class HomeContactEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false, length = 100)
    val name: String,
    
    @Column(nullable = false)
    val email: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    val message: String,
    
    @Column(name = "ip_address", length = 45)
    val ipAddress: String? = null,
    
    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(0, "", "", "", null)
}
