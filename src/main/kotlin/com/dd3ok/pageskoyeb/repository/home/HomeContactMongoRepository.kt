package com.dd3ok.pageskoyeb.repository.home

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface HomeContactMongoRepository : MongoRepository<HomeContactEntity, String> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<HomeContactEntity>
    fun findByEmail(email: String): List<HomeContactEntity>
}