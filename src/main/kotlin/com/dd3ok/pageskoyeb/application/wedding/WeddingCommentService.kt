package com.dd3ok.pageskoyeb.application.wedding

import com.dd3ok.pageskoyeb.application.wedding.dto.CommentResponse
import com.dd3ok.pageskoyeb.application.wedding.dto.CreateCommentRequest
import com.dd3ok.pageskoyeb.application.wedding.dto.DeleteCommentRequest
import com.dd3ok.pageskoyeb.application.wedding.dto.UpdateCommentRequest
import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.wedding.WeddingComment
import com.dd3ok.pageskoyeb.domain.wedding.WeddingCommentRepository
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentAuthor
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentMessage
import com.dd3ok.pageskoyeb.domain.wedding.vo.CommentPassword
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WeddingCommentService(
    private val repository: WeddingCommentRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    
    fun createComment(request: CreateCommentRequest, ipAddress: String?): CommentResponse {
        val comment = WeddingComment.create(
            author = CommentAuthor(request.author),
            password = CommentPassword(request.password),
            message = CommentMessage(request.message),
            ipAddress = IpAddress(ipAddress),
            passwordEncoder = { rawPassword -> passwordEncoder.encode(rawPassword) }
        )
        
        val savedComment = repository.save(comment)
        return CommentResponse.from(savedComment)
    }
    
    @Transactional(readOnly = true)
    fun getComments(page: Int = 0, size: Int = 10): Page<CommentResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        return repository.findAll(pageable)
            .map { CommentResponse.from(it) }
    }
    
    @Transactional(readOnly = true)
    fun getComment(id: String): CommentResponse {
        val comment = repository.findById(id)
            ?: throw IllegalArgumentException("방명록을 찾을 수 없습니다. ID: $id")
        return CommentResponse.from(comment)
    }
    
    fun updateComment(id: String, request: UpdateCommentRequest): CommentResponse {
        val comment = repository.findById(id)
            ?: throw IllegalArgumentException("방명록을 찾을 수 없습니다. ID: $id")
        
        val inputPassword = CommentPassword(request.password)
        if (!comment.verifyPassword(inputPassword) { raw, hashed -> 
            passwordEncoder.matches(raw, hashed) 
        }) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다")
        }
        
        val updatedComment = comment.updateMessage(CommentMessage(request.message))
        val savedComment = repository.save(updatedComment)
        return CommentResponse.from(savedComment)
    }
    
    fun deleteComment(id: String, request: DeleteCommentRequest) {
        val comment = repository.findById(id)
            ?: throw IllegalArgumentException("방명록을 찾을 수 없습니다. ID: $id")
        
        val inputPassword = CommentPassword(request.password)
        if (!comment.verifyPassword(inputPassword) { raw, hashed -> 
            passwordEncoder.matches(raw, hashed) 
        }) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다")
        }
        
        repository.deleteById(id)
    }
}
