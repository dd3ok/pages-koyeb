package com.dd3ok.pageskoyeb.repository.wedding

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.mongodb.repository.MongoRepository

interface WeddingCommentMongoRepository : MongoRepository<WeddingCommentEntity, String> {
    fun findAllByOrderByCreatedAtDescIdDesc(pageable: Pageable): Slice<WeddingCommentEntity>
}
