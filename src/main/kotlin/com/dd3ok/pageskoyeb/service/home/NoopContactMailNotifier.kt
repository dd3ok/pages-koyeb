package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse
import org.slf4j.LoggerFactory

class NoopContactMailNotifier @JvmOverloads constructor(
    private val reason: String = "SMTP is not configured"
) : ContactMailNotifier {

    override fun notify(contact: ContactResponse) {
        logger.info(
            "Contact notification email skipped for contact id {} because {}",
            contact.id.ifBlank { "unknown" },
            reason
        )
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(NoopContactMailNotifier::class.java)
    }
}
