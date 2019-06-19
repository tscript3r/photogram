package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.domains.EmailConfirmation;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.InternalErrorPhotogramException;
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
    public void createAndSendEmailConfirmation(@NotNull final User user) {
        emailConfirmationRepository.save(getEmailConfirmation(user, false));
        // TODO: push new task to sender
    }

    private EmailConfirmation getEmailConfirmation(final User user, final Boolean confirmed) {
        return new EmailConfirmation(user, getUUID(user.getId()));
    }

    private UUID getUUID(final Long userId) {
        return UUID.fromString(userId.toString() + System.currentTimeMillis());
    }

    @Override
    public void createEmailConfirmation(@NotNull final User user) {
        emailConfirmationRepository.save(getEmailConfirmation(user, true));
    }

    @Override
    public void updateEmailConfirmation(@NotNull final User user) {
        var emailConfirmation = emailConfirmationRepository.findByUser(user)
                .orElseThrow(() ->
                        new InternalErrorPhotogramException(String.format("Email confirmation from userId=%d not found",
                                user.getId())));
        emailConfirmation.setConfirmed(false);
        emailConfirmation.setToken(getUUID(user.getId()));
        emailConfirmationRepository.save(emailConfirmation);
        // TODO: push new task to sender
    }

}
