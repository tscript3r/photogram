package pl.tscript3r.photogram.services;

import pl.tscript3r.photogram.domains.EmailConfirmation;
import pl.tscript3r.photogram.domains.User;

import javax.validation.constraints.NotNull;

public interface EmailService {

    /**
     * When send mail is set as false the email will be set as confirmed
     */
    EmailConfirmation createEmailConfirmation(@NotNull final User user, @NotNull final Boolean sendMail);

    void resendEmailConfirmation(@NotNull final User user);

    void setEmailConfirmed(@NotNull final String token);

    void sendNewPassword(@NotNull final User user, @NotNull final String newPassword);

}
