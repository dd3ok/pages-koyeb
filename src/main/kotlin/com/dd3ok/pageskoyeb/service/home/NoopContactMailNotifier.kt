package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse

class NoopContactMailNotifier : ContactMailNotifier {
    override fun notify(contact: ContactResponse) = Unit
}
