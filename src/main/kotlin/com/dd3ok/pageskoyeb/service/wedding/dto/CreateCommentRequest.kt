package com.dd3ok.pageskoyeb.service.wedding.dto

import com.dd3ok.pageskoyeb.domain.wedding.WeddingComment
import java.time.format.DateTimeFormatter

data class CreateCommentRequest(
    val author: String,
    val password: String,
    val message: String
)

data class UpdateCommentRequest(
    val password: String,
    val message: String
)

data class DeleteCommentRequest(
    val password: String
)

data class CommentResponse(
    val id: String,
    val author: String,
    val message: String,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(comment: WeddingComment): CommentResponse {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return CommentResponse(
                id = comment.id ?: "",
                author = comment.author.getValue(),
                message = comment.message.getValue(),
                createdAt = comment.createdAt.format(formatter),
                updatedAt = comment.updatedAt.format(formatter)
            )
        }
    }
}
