package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.domain.common.IpAddress
import com.dd3ok.pageskoyeb.domain.home.HomeContact
import com.dd3ok.pageskoyeb.domain.home.HomeContactRepository
import com.dd3ok.pageskoyeb.domain.home.vo.ContactEmail
import com.dd3ok.pageskoyeb.domain.home.vo.ContactMessage
import com.dd3ok.pageskoyeb.domain.home.vo.ContactName
import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse
import com.dd3ok.pageskoyeb.service.home.dto.CreateContactRequest
import org.springframework.stereotype.Service

@Service
class HomeContactService(
    private val repository: HomeContactRepository,
    private val contactMailNotifier: ContactMailNotifier
) {

    fun createContact(request: CreateContactRequest, ipAddress: String?): ContactResponse {
        val contact = HomeContact.create(
            name = ContactName(request.name),
            email = ContactEmail(request.email),
            message = ContactMessage(request.message),
            ipAddress = IpAddress(ipAddress)
        )

        val response = ContactResponse.from(repository.save(contact))
        try {
            contactMailNotifier.notify(response)
        } catch (_: RuntimeException) {
            // Contact persistence must not depend on notification delivery.
        }
        return response
    }
}
