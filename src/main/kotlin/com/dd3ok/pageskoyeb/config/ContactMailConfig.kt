package com.dd3ok.pageskoyeb.config

import com.dd3ok.pageskoyeb.service.home.ContactMailNotifier
import com.dd3ok.pageskoyeb.service.home.NoopContactMailNotifier
import com.dd3ok.pageskoyeb.service.home.SmtpContactMailNotifier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class ContactMailConfig {

    @Bean(name = ["contactMailTaskExecutor"])
    @ConditionalOnProperty(prefix = "spring.mail", name = ["host"])
    @ConditionalOnProperty(prefix = "contact.mail", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun contactMailTaskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 1
            maxPoolSize = 1
            queueCapacity = 20
            keepAliveSeconds = 30
            setAllowCoreThreadTimeOut(true)
            setThreadNamePrefix("contact-mail-")
            setWaitForTasksToCompleteOnShutdown(false)
        }
    }

    @Bean
    @ConditionalOnBean(JavaMailSender::class)
    @ConditionalOnProperty(prefix = "spring.mail", name = ["host"])
    @ConditionalOnProperty(prefix = "contact.mail", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun smtpContactMailNotifier(
        mailSender: JavaMailSender,
        @Qualifier("contactMailTaskExecutor") taskExecutor: TaskExecutor,
        @Value("\${contact.mail.to:hwick@kakao.com}") to: String,
        @Value("\${contact.mail.from:}") from: String,
        @Value("\${spring.mail.username:}") username: String
    ): ContactMailNotifier {
        return SmtpContactMailNotifier(mailSender, taskExecutor, to, from, username)
    }

    @Bean
    @ConditionalOnMissingBean(ContactMailNotifier::class)
    fun noopContactMailNotifier(): ContactMailNotifier = NoopContactMailNotifier()
}
