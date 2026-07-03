package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse

fun interface ContactMailNotifier {
    fun notify(contact: ContactResponse)
}
