package com.dd3ok.pageskoyeb.repository.home

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.home.HomeContact
import com.dd3ok.pageskoyeb.domain.home.HomeContactRepository
import com.dd3ok.pageskoyeb.domain.home.vo.ContactEmail
import com.dd3ok.pageskoyeb.domain.home.vo.ContactMessage
import com.dd3ok.pageskoyeb.domain.home.vo.ContactName
import org.springframework.stereotype.Repository

@Repository
class HomeContactRepositoryImpl(
    private val mongoRepository: HomeContactMongoRepository
) : HomeContactRepository {

    override fun save(contact: HomeContact): HomeContact {
        val entity = contact.toEntity()
        val savedEntity = mongoRepository.save(entity)
        return savedEntity.toDomain()
    }

    private fun HomeContact.toEntity(): HomeContactEntity {
        return HomeContactEntity(
            id = this.id,
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
