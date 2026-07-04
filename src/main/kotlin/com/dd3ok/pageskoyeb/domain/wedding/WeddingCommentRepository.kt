package com.dd3ok.pageskoyeb.domain.wedding

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface WeddingCommentRepository {
    fun save(comment: WeddingComment): WeddingComment
    fun findById(id: String): WeddingComment?
    fun findAll(pageable: Pageable): Slice<WeddingComment>
    fun deleteById(id: String)
    fun existsById(id: String): Boolean
}
