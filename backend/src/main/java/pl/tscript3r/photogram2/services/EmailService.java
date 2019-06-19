package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;

public interface EmailService {

    void createAndSendEmailConfirmation(@NotNull final User user);

    void createEmailConfirmation(@NotNull final User user);

    void updateEmailConfirmation(@NotNull final User user);

}
