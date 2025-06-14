package com.dd3ok.pageskoyeb.infrastructure.persistence.home

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.home.HomeContact
import com.dd3ok.pageskoyeb.domain.home.HomeContactRepository
import com.dd3ok.pageskoyeb.domain.home.vo.ContactEmail
import com.dd3ok.pageskoyeb.domain.home.vo.ContactMessage
import com.dd3ok.pageskoyeb.domain.home.vo.ContactName
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class HomeContactRepositoryImpl(
    private val jpaRepository: HomeContactJpaRepository
) : HomeContactRepository {
    
    override fun save(contact: HomeContact): HomeContact {
        val entity = contact.toEntity()
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }
    
    override fun findById(id: Long): HomeContact? {
        return jpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }
    
    override fun findAll(pageable: Pageable): Page<HomeContact> {
        return jpaRepository.findAllByOrderByCreatedAtDesc(pageable)
            .map { it.toDomain() }
    }
    
    override fun findByEmail(email: String): List<HomeContact> {
        return jpaRepository.findByEmail(email)
            .map { it.toDomain() }
    }
    
    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }
    
    override fun existsById(id: Long): Boolean {
        return jpaRepository.existsById(id)
    }
    
    private fun HomeContact.toEntity(): HomeContactEntity {
        return HomeContactEntity(
            id = this.id ?: 0,
            name = this.name.getValue(),
            email = this.email.getValue(),
            message = this.message.getValue(),
            ipAddress = this.ipAddress.getValue(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
    
    private fun HomeContactEntity.toDomain(): HomeContact {
        return HomeContact.fromRepository(
            id = this.id,
            name = ContactName(this.name),
            email = ContactEmail(this.email),
            message = ContactMessage(this.message),
            ipAddress = IpAddress(this.ipAddress),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
