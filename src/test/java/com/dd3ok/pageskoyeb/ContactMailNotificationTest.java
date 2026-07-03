package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import com.dd3ok.pageskoyeb.domain.home.HomeContact;
import com.dd3ok.pageskoyeb.domain.home.HomeContactRepository;
import com.dd3ok.pageskoyeb.service.home.ContactMailNotifier;
import com.dd3ok.pageskoyeb.service.home.HomeContactService;
import com.dd3ok.pageskoyeb.service.home.NoopContactMailNotifier;
import com.dd3ok.pageskoyeb.service.home.SmtpContactMailNotifier;
import com.dd3ok.pageskoyeb.service.home.dto.ContactResponse;
import com.dd3ok.pageskoyeb.service.home.dto.CreateContactRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(OutputCaptureExtension.class)
class ContactMailNotificationTest {

    @Test
    void createContactNotifiesAfterSaving() {
        SavingHomeRepository repository = new SavingHomeRepository();
        CapturingContactMailNotifier notifier = new CapturingContactMailNotifier();
        HomeContactService service = new HomeContactService(repository, notifier);

        ContactResponse response = service.createContact(
            new CreateContactRequest("Kim", "kim@example.com", "Need help"),
            "127.0.0.1"
        );

        assertThat(response.getEmail()).isEqualTo("kim@example.com");
        assertThat(notifier.contact.getEmail()).isEqualTo("kim@example.com");
    }

    @Test
    void createContactKeepsSavedResponseWhenNotificationFails() {
        SavingHomeRepository repository = new SavingHomeRepository();
        HomeContactService service = new HomeContactService(
            repository,
            contact -> {
                throw new IllegalStateException("smtp unavailable");
            }
        );

        ContactResponse response = service.createContact(
            new CreateContactRequest("Kim", "kim@example.com", "Need help"),
            "127.0.0.1"
        );

        assertThat(response.getEmail()).isEqualTo("kim@example.com");
        assertThat(repository.saved).isNotNull();
    }

    @Test
    void smtpNotifierSendsCompactContactEmail() {
        CapturingMailSender mailSender = new CapturingMailSender();
        SmtpContactMailNotifier notifier = new SmtpContactMailNotifier(
            mailSender,
            new SyncTaskExecutor(),
            "hwick@kakao.com",
            "no-reply@example.com",
            ""
        );

        notifier.notify(new ContactResponse(
            "contact-1",
            "Kim",
            "kim@example.com",
            "Need help",
            "2026-07-03 12:00:00",
            "2026-07-03 12:00:00"
        ));

        assertThat(mailSender.message.getTo()).containsExactly("hwick@kakao.com");
        assertThat(mailSender.message.getFrom()).isEqualTo("no-reply@example.com");
        assertThat(mailSender.message.getSubject()).isEqualTo("[dd3ok.github.io] New contact from Kim");
        assertThat(mailSender.message.getText())
            .contains("Name: Kim")
            .contains("Email: kim@example.com")
            .contains("Message:\nNeed help");
    }

    @Test
    void smtpNotifierLogsScheduleAndSuccessWithoutMessageBody(CapturedOutput output) {
        CapturingMailSender mailSender = new CapturingMailSender();
        SmtpContactMailNotifier notifier = new SmtpContactMailNotifier(
            mailSender,
            new SyncTaskExecutor(),
            "hwick@kakao.com",
            "no-reply@example.com",
            ""
        );

        notifier.notify(new ContactResponse(
            "contact-1",
            "Kim",
            "kim@example.com",
            "Secret message body",
            "2026-07-03 12:00:00",
            "2026-07-03 12:00:00"
        ));

        assertThat(output)
            .contains("Contact notification email scheduled for contact id contact-1")
            .contains("Contact notification email sent for contact id contact-1")
            .doesNotContain("Secret message body")
            .doesNotContain("kim@example.com");
    }

    @Test
    void noopNotifierLogsSkippedNotification(CapturedOutput output) {
        ContactMailNotifier notifier = new NoopContactMailNotifier("spring.mail.host is not configured");

        notifier.notify(new ContactResponse(
            "contact-1",
            "Kim",
            "kim@example.com",
            "Secret message body",
            "2026-07-03 12:00:00",
            "2026-07-03 12:00:00"
        ));

        assertThat(output)
            .contains("Contact notification email skipped for contact id contact-1")
            .contains("spring.mail.host is not configured")
            .doesNotContain("Secret message body")
            .doesNotContain("kim@example.com");
    }

    private static class CapturingContactMailNotifier implements ContactMailNotifier {
        private ContactResponse contact;

        @Override
        public void notify(ContactResponse contact) {
            this.contact = contact;
        }
    }

    private static class CapturingMailSender implements JavaMailSender {
        private SimpleMailMessage message;

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            this.message = simpleMessage;
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            this.message = simpleMessages[0];
        }

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage(java.io.InputStream contentStream) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator mimeMessagePreparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator... mimeMessagePreparators) {
            throw new UnsupportedOperationException();
        }
    }

    private static class SavingHomeRepository implements HomeContactRepository {
        private HomeContact saved;

        @Override
        public HomeContact save(HomeContact contact) {
            this.saved = contact;
            return this.saved;
        }

        @Override
        public HomeContact findById(String id) {
            return null;
        }

        @Override
        public Page<HomeContact> findAll(Pageable pageable) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        @Override
        public List<HomeContact> findByEmail(String email) {
            return List.of();
        }

        @Override
        public void deleteById(String id) {
        }

        @Override
        public boolean existsById(String id) {
            return false;
        }
    }
}
