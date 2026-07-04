package com.dd3ok.pageskoyeb.domain.home

interface HomeContactRepository {
    fun save(contact: HomeContact): HomeContact
}
