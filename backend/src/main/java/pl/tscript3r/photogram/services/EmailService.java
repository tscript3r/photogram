package pl.tscript3r.photogram.services;

import pl.tscript3r.photogram.domains.EmailConfirmation;
import pl.tscript3r.photogram.domains.User;

import javax.validation.constraints.NotNull;

public interface EmailService {

    EmailConfirmation emailConfirmation(@NotNull final User user, @NotNull final Boolean markAsConfirmed,
                                        @NotNull final Boolean sendMail);

    void resendEmailConfirmation(@NotNull final User user);

}
