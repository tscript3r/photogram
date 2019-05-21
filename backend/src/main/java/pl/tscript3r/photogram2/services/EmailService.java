package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domains.User;

public interface EmailService {

    void sendConfirmationEmail(User user);

}
