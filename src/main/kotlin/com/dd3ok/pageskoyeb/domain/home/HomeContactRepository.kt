package com.dd3ok.pageskoyeb.domain.home

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HomeContactRepository {
    fun save(contact: HomeContact): HomeContact
    fun findById(id: String): HomeContact?
    fun findAll(pageable: Pageable): Page<HomeContact>
    fun findByEmail(email: String): List<HomeContact>
    fun deleteById(id: String)
    fun existsById(id: String): Boolean
}
