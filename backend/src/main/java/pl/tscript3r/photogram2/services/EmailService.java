package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;

public interface EmailService {

    void sendConfirmationEmail(@NotNull final User user);

}
