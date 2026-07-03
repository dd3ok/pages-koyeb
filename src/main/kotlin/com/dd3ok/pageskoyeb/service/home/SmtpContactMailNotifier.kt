package com.dd3ok.pageskoyeb.service.home

import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse
import org.slf4j.LoggerFactory
import org.springframework.core.task.TaskExecutor
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class SmtpContactMailNotifier(
    private val mailSender: JavaMailSender,
    private val taskExecutor: TaskExecutor,
    private val to: String,
    private val from: String,
    private val username: String
) : ContactMailNotifier {

    override fun notify(contact: ContactResponse) {
        try {
            taskExecutor.execute { send(contact) }
        } catch (ex: RuntimeException) {
            logger.warn("Failed to schedule contact notification email for contact id {}", contact.id, ex)
        }
    }

    private fun send(contact: ContactResponse) {
        try {
            val message = SimpleMailMessage()
            message.setTo(to)
            fromAddress().takeIf { it.isNotBlank() }?.let { message.from = it }
            message.subject = "[dd3ok.github.io] New contact from ${contact.name}"
            message.text = """
                A new contact message was submitted.

                Name: ${contact.name}
                Email: ${contact.email}
                Created At: ${contact.createdAt}

                Message:
                ${contact.message}
            """.trimIndent()

            mailSender.send(message)
        } catch (ex: RuntimeException) {
            logger.warn("Failed to send contact notification email for contact id {}", contact.id, ex)
        }
    }

    private fun fromAddress(): String = from.ifBlank { username }

    private companion object {
        private val logger = LoggerFactory.getLogger(SmtpContactMailNotifier::class.java)
    }
}
