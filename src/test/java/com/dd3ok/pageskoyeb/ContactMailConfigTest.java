package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.dd3ok.pageskoyeb.config.ContactMailConfig;
import com.dd3ok.pageskoyeb.service.home.ContactMailNotifier;
import com.dd3ok.pageskoyeb.service.home.NoopContactMailNotifier;
import com.dd3ok.pageskoyeb.service.home.SmtpContactMailNotifier;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class ContactMailConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(ContactMailConfig.class);

    @Test
    void usesNoopNotifierWithoutSmtpHost() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ContactMailNotifier.class);
            assertThat(context.getBean(ContactMailNotifier.class)).isInstanceOf(NoopContactMailNotifier.class);
            assertThat(context).doesNotHaveBean("contactMailTaskExecutor");
        });
    }

    @Test
    void usesBoundedSmtpNotifierWhenSmtpHostExists() {
        contextRunner
            .withBean(JavaMailSender.class, () -> mock(JavaMailSender.class))
            .withPropertyValues("spring.mail.host=smtp.example.com")
            .run(context -> {
                assertThat(context).hasSingleBean(ContactMailNotifier.class);
                assertThat(context.getBean(ContactMailNotifier.class)).isInstanceOf(SmtpContactMailNotifier.class);

                ThreadPoolTaskExecutor executor = context.getBean(
                    "contactMailTaskExecutor",
                    ThreadPoolTaskExecutor.class
                );
                assertThat(executor.getCorePoolSize()).isEqualTo(1);
                assertThat(executor.getMaxPoolSize()).isEqualTo(1);
            });
    }

    @Test
    void keepsNoopNotifierWhenMailIsExplicitlyDisabled() {
        contextRunner
            .withBean(JavaMailSender.class, () -> mock(JavaMailSender.class))
            .withPropertyValues(
                "spring.mail.host=smtp.example.com",
                "contact.mail.enabled=false"
            )
            .run(context -> {
                assertThat(context).hasSingleBean(ContactMailNotifier.class);
                assertThat(context.getBean(ContactMailNotifier.class)).isInstanceOf(NoopContactMailNotifier.class);
                assertThat(context).doesNotHaveBean("contactMailTaskExecutor");
            });
    }
}
