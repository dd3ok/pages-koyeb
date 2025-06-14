package com.dd3ok.pageskoyeb.infrastructure.persistence.wedding

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface WeddingCommentJpaRepository : JpaRepository<WeddingCommentEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<WeddingCommentEntity>
}
