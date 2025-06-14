package com.dd3ok.pageskoyeb.domain.home

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HomeContactRepository {
    fun save(contact: HomeContact): HomeContact
    fun findById(id: Long): HomeContact?
    fun findAll(pageable: Pageable): Page<HomeContact>
    fun findByEmail(email: String): List<HomeContact>
    fun deleteById(id: Long)
    fun existsById(id: Long): Boolean
}
