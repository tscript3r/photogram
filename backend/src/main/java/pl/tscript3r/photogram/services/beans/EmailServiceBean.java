package pl.tscript3r.photogram.services.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import pl.tscript3r.photogram.configs.EmailConfig;
import pl.tscript3r.photogram.domains.EmailConfirmation;
import pl.tscript3r.photogram.domains.User;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.repositories.EmailConfirmationRepository;
import pl.tscript3r.photogram.services.EmailService;
import pl.tscript3r.photogram.services.utils.EmailMessageType;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static pl.tscript3r.photogram.services.utils.EmailMessageType.EMAIL_CONFIRMATION;
import static pl.tscript3r.photogram.services.utils.EmailMessageType.PASSWORD_RESET;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceBean implements EmailService, DisposableBean {

    private final ExecutorService emailSenderExecutor;
    private final EmailConfig emailConfig;
    private final JavaMailSender emailSender;
    private final EmailConfirmationRepository emailConfirmationRepository;

    @Override
    public EmailConfirmation createEmailConfirmation(@NotNull final User user, @NotNull final Boolean sendMail) {
        var emailConfirmationOptional = emailConfirmationRepository.findByUser(user);
        emailConfirmationOptional.ifPresent(confirmation -> confirmation.setConfirmed(!sendMail));
        var emailConfirmation = emailConfirmationOptional.orElse(new EmailConfirmation(user, getUUID(), !sendMail));
        var savedEmailConfirmation = emailConfirmationRepository.save(emailConfirmation);
        if (sendMail)
            send(savedEmailConfirmation);
        return savedEmailConfirmation;
    }

    private UUID getUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (emailConfirmationRepository.existsByToken(uuid));
        return uuid;
    }

    private void send(final EmailConfirmation emailConfirmation) {
        var email = emailConfirmation.getUser().getEmail();
        var runnable = getRunnableSender(EMAIL_CONFIRMATION, email, emailConfig.getConfirmationTitle(),
                getEmailConfirmationContext(emailConfirmation));
        emailSenderExecutor.execute(runnable);
    }

    private Context getEmailConfirmationContext(final EmailConfirmation emailConfirmation) {
        User user = emailConfirmation.getUser();
        Context context = new Context();
        context.setVariable("firstname", user.getFirstname());
        context.setVariable("confirmationUrl",
                String.format(emailConfig.getConfirmationUrl(),
                        emailConfirmation.getToken()));
        return context;
    }

    private Runnable getRunnableSender(final EmailMessageType emailMessageType, final String sendTo, final String title,
                                       final Context context) {
        return () -> emailSender.send(emailMessageType.getPreparedMimeMessage(sendTo, title, context));
    }

    @Override
    public void resendEmailConfirmation(@NotNull final User user) {
        var emailConfirmation = emailConfirmationRepository.findByUser(user);
        send(emailConfirmation.orElse(createEmailConfirmation(user, true)));
    }

    @Override
    public void setEmailConfirmed(@NotNull final String token) {
        var emailConfirmation = emailConfirmationRepository.findByToken(new UUID(
                new BigInteger(token.substring(0, 16), 16).longValue(),
                new BigInteger(token.substring(16), 16).longValue()))
                .orElseThrow(() ->
                    new NotFoundPhotogramException(String.format("Token=%s not found", token)));
        emailConfirmation.setConfirmed(true);
        emailConfirmationRepository.save(emailConfirmation);
    }

    @Override
    public void sendNewPassword(@NotNull final User user, @NotNull final String newPassword) {
        var email = user.getEmail();
        var runnable = getRunnableSender(PASSWORD_RESET, email, emailConfig.getPasswordResetTitle(),
                getNewPasswordContext(user.getFirstname(), newPassword));
        emailSenderExecutor.execute(runnable);
    }

    private Context getNewPasswordContext(final String firstname, final String newPassword) {
        var context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("password", newPassword);
        return context;
    }

    @Override
    public void destroy() {
        emailSenderExecutor.shutdown();
        try {
            if (!emailSenderExecutor.awaitTermination(800, TimeUnit.MILLISECONDS))
                emailSenderExecutor.shutdownNow();
        } catch (InterruptedException e) {
            emailSenderExecutor.shutdownNow();
        }
    }

}
