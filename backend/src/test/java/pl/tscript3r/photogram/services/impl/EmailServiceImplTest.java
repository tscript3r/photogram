package pl.tscript3r.photogram.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import pl.tscript3r.photogram.configs.EmailConfig;
import pl.tscript3r.photogram.domains.EmailConfirmation;
import pl.tscript3r.photogram.repositories.EmailConfirmationRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;

@DisplayName("Email service")
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final Long ID = 1L;
    private static final UUID DEFAULT_UUID = UUID.randomUUID();

    private static EmailConfirmation getDefaultEmailConfirmation() {
        var emailConfirmation = new EmailConfirmation(getDefaultUser(), DEFAULT_UUID, true);
        emailConfirmation.setId(ID);
        return emailConfirmation;
    }

    @Mock
    ExecutorService executorService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    EmailConfirmationRepository emailConfirmationRepository;

    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        var emailConfig = new EmailConfig();
        emailConfig.setHost(EMAIL_HOST);
        emailConfig.setPassword(EMAIL_PASSWORD);
        emailConfig.setUsername(EMAIL_USERNAME);
        emailConfig.setConfirmationTitle(EMAIL_SUBJECT);
        emailConfig.setConfirmationUrl(EMAIL_CONFIRMATION_URL);
        emailConfig.setPort(666);

        emailService = new EmailServiceImpl(executorService, emailConfig, javaMailSender, emailConfirmationRepository);
    }

    @Test
    @DisplayName("Email confirmation with new instance and sending email")
    void emailConfirmation() {
        happyPathNewUserEmailConfirmation(true);
    }

    private void happyPathNewUserEmailConfirmation(Boolean sendMail) {
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.empty());
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        when(emailConfirmationRepository.existsByToken(any())).thenReturn(false);

        assertEquals(!sendMail, emailService.emailConfirmation(getDefaultUser(), sendMail).getConfirmed());

        verify(emailConfirmationRepository, times(1)).findByUser(any());
        verify(emailConfirmationRepository, times(1)).save(any());
        verify(emailConfirmationRepository, times(1)).existsByToken(any());

        int times = (sendMail ? 1 : 0);
        verify(executorService, times(times)).execute(any());
    }

    @Test
    @DisplayName("Email confirmation with new instance without sending")
    void emailConfirmationWithoutSending() {
        happyPathNewUserEmailConfirmation(false);
    }

    @Test
    @DisplayName("Email confirmation with existing instance")
    void emailConfirmationWithExistingInstance() {
        var existingEmailConfirmation = new EmailConfirmation(getDefaultUser(), UUID.randomUUID(), true);
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.of(existingEmailConfirmation));
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        when(emailConfirmationRepository.existsByToken(any())).thenReturn(false);

        assertEquals(false, emailService.emailConfirmation(getDefaultUser(), true).getConfirmed());

        verify(emailConfirmationRepository, times(1)).findByUser(any());
        verify(emailConfirmationRepository, times(1)).save(any());
        verify(emailConfirmationRepository, times(1)).existsByToken(any());
        verify(executorService, times(1)).execute(any());
    }

    @Test
    @DisplayName("Unique UUID")
    void emailConfirmationUniqueUUID() {
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.empty());
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        doReturn(true).doReturn(false).when(emailConfirmationRepository).existsByToken(any());

        emailService.emailConfirmation(getDefaultUser(), true);

        verify(emailConfirmationRepository, times(2)).existsByToken(any());
    }

    @Test
    @DisplayName("Runnable sender")
    void emailConfirmationRunnableSender() {
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.empty());
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        when(emailConfirmationRepository.existsByToken(any())).thenReturn(false);
        doAnswer((Answer<Void>) invocation -> {
            var runnable = (Runnable) invocation.getArguments()[0];
            runnable.run();
            return null;
        }).when(executorService).execute(any());

        emailService.emailConfirmation(getDefaultUser(), true);

        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }

    @Test
    @DisplayName("Resend email confirmation (with existing confirmation)")
    void resendEmailConfirmation() {
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.of(getDefaultEmailConfirmation()));
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        when(emailConfirmationRepository.existsByToken(any())).thenReturn(false);

        emailService.resendEmailConfirmation(getDefaultUser());
    }

    @Test
    @DisplayName("Resend email confirmation (without existing confirmation)")
    void resendEmailConfirmationWithoutExistingConfirmation() {
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.empty());
        when(emailConfirmationRepository.save(any())).thenAnswer((Answer<EmailConfirmation>) invocation -> {
            Object[] args = invocation.getArguments();
            return (EmailConfirmation) args[0];
        });
        when(emailConfirmationRepository.existsByToken(any())).thenReturn(false);

        emailService.resendEmailConfirmation(getDefaultUser());
    }

    @Test
    @DisplayName("Executor service shutdown")
    void destroy() {
        emailService.destroy();
        verify(executorService, times(1)).shutdown();
    }

    @Test
    @DisplayName("Executor service force shutdown")
    void forceDestroy() {
        emailService.destroy();

        verify(executorService, times(1)).shutdown();
        verify(executorService, times(1)).shutdownNow();
    }

}