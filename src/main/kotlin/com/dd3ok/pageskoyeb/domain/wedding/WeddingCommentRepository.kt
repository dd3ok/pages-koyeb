package com.dd3ok.pageskoyeb.domain.wedding

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WeddingCommentRepository {
    fun save(comment: WeddingComment): WeddingComment
    fun findById(id: String): WeddingComment?
    fun findAll(pageable: Pageable): Page<WeddingComment>
    fun deleteById(id: String)
    fun existsById(id: String): Boolean
}
