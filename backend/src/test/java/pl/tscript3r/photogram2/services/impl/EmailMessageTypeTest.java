package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static pl.tscript3r.photogram2.services.utils.EmailMessageType.EMAIL_CONFIRMATION;

class EmailMessageTypeTest {

    @Test
    void get() throws Exception {
        MimeMessagePreparator messagePreparator = EMAIL_CONFIRMATION.getPreparedMimeMessage("", "", null);
        System.out.println(messagePreparator);
    }

}