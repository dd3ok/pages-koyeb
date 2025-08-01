package com.dd3ok.pageskoyeb.repository.wedding

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.wedding.WeddingComment
import com.dd3ok.pageskoyeb.domain.wedding.WeddingCommentRepository
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentAuthor
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class WeddingCommentRepositoryImpl(
    private val mongoRepository: WeddingCommentMongoRepository
) : WeddingCommentRepository {

    override fun save(comment: WeddingComment): WeddingComment {
        val entity = comment.toEntity()
        val savedEntity = mongoRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: String): WeddingComment? {
        return mongoRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAll(pageable: Pageable): Page<WeddingComment> {
        return mongoRepository.findAllByOrderByCreatedAtDesc(pageable)
            .map { it.toDomain() }
    }

    override fun deleteById(id: String) {
        mongoRepository.deleteById(id)
    }

    override fun existsById(id: String): Boolean {
        return mongoRepository.existsById(id)
    }

    private fun WeddingComment.toEntity(): WeddingCommentEntity {
        return WeddingCommentEntity(
            id = this.id,
            name = this.author.getValue(),
            password = this.getHashedPassword(),
            message = this.message.getValue(),
            ipAddress = this.ipAddress.getValue(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun WeddingCommentEntity.toDomain(): WeddingComment {
        return WeddingComment.fromRepository(
            id = this.id,
            author = CommentAuthor(this.name),
            hashedPassword = this.password,
            message = CommentMessage(this.message),
            ipAddress = IpAddress(this.ipAddress),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
