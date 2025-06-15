package com.dd3ok.pageskoyeb.infrastructure.scheduler

import com.dd3ok.pageskoyeb.application.home.HomeContactService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class KeepAliveScheduler(
    private val service: HomeContactService
) {
    // 30분마다 실행 (30 * 60 * 1000 = 1800000ms)
    @Scheduled(fixedRate = 1800000)
    fun keepAlive() {
        service.getContact(1)
    }
}
