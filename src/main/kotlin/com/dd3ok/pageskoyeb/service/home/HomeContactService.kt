package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse
import com.dd3ok.pageskoyeb.service.home.dto.CreateContactRequest
import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.home.HomeContact
import com.dd3ok.pageskoyeb.domain.home.HomeContactRepository
import com.dd3ok.pageskoyeb.domain.home.vo.ContactEmail
import com.dd3ok.pageskoyeb.domain.home.vo.ContactMessage
import com.dd3ok.pageskoyeb.domain.home.vo.ContactName
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HomeContactService(
    private val repository: HomeContactRepository
) {
    
    fun createContact(request: CreateContactRequest, ipAddress: String?): ContactResponse {
        val contact = HomeContact.create(
            name = ContactName(request.name),
            email = ContactEmail(request.email),
            message = ContactMessage(request.message),
            ipAddress = IpAddress(ipAddress)
        )
        
        val savedContact = repository.save(contact)
        return ContactResponse.from(savedContact)
    }
    
    @Transactional(readOnly = true)
    fun getContacts(page: Int = 0, size: Int = 10): Page<ContactResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        return repository.findAll(pageable)
            .map { ContactResponse.from(it) }
    }
    
    @Transactional(readOnly = true)
    fun getContact(id: String): ContactResponse {
        val contact = repository.findById(id)
            ?: throw IllegalArgumentException("문의를 찾을 수 없습니다. ID: $id")
        return ContactResponse.from(contact)
    }
    
    @Transactional(readOnly = true)
    fun getContactsByEmail(email: String): List<ContactResponse> {
        return repository.findByEmail(email)
            .map { ContactResponse.from(it) }
    }
    
    fun deleteContact(id: String) {
        if (!repository.existsById(id)) {
            throw IllegalArgumentException("문의를 찾을 수 없습니다. ID: $id")
        }
        repository.deleteById(id)
    }
}
