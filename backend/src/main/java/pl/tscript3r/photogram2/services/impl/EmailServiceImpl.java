package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.domains.EmailConfirmation;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.repositories.EmailConfirmationRepository;
import pl.tscript3r.photogram2.services.EmailService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailConfirmationRepository emailConfirmationRepository;

    @Override
    public EmailConfirmation emailConfirmation(@NotNull final User user, @NotNull final Boolean markAsConfirmed,
                                               @NotNull final Boolean sendMail) {
        var emailConfirmationOptional = emailConfirmationRepository.findByUser(user);
        emailConfirmationOptional.ifPresent(confirmation -> confirmation.setConfirmed(markAsConfirmed));
        var emailConfirmation = emailConfirmationOptional.orElse(new EmailConfirmation(user, getUUID(), markAsConfirmed));
        if (sendMail)
            send(emailConfirmation);
        return emailConfirmation;
    }

    private UUID getUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (emailConfirmationRepository.existsByToken(uuid));
        return UUID.randomUUID();
    }

    private void send(EmailConfirmation emailConfirmation) {
        // TODO: implement
    }

    @Override
    public void resendEmailConfirmation(@NotNull final User user) {
        var emailConfirmation = emailConfirmationRepository.findByUser(user);
        send(emailConfirmation.orElse(emailConfirmation(user, false, true)));
    }

}
