package pl.tscript3r.photogram2.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.tscript3r.photogram2.Photogram2Application;
import pl.tscript3r.photogram2.configs.EmailConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.tscript3r.photogram2.services.utils.EmailMessageType.EMAIL_CONFIRMATION;

@DisplayName("Email message type")
@SpringBootTest(classes = Photogram2Application.class)
class EmailMessageTypeTest {

    private static final String EMAIL_HOST = "smtp.mail.test";
    private static final String EMAIL_PASSWORD = "password";
    private static final String EMAIL_USERNAME = "username";
    private static final String EMAIL_SEND_TO = "sendTo@mail.com";
    private static final String EMAIL_SUBJECT = "subject";
    private static final Object FIRSTNAME_VARIABLE = "FirstnameVariable";
    private static final Object CONFIRMATION_URL = "https://www.photogram.com/confirm?token=234ASD41293213";

    @Autowired
    TemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        var injector = new EmailMessageType.EmailMessageCreatorInjector();
        injector.setTemplateEngine(templateEngine);
        injector.setEmailConfig(getEmailConfig());
        injector.postConstruct();
    }

    private EmailConfig getEmailConfig() {
        var emailConfig = new EmailConfig();
        emailConfig.setHost(EMAIL_HOST);
        emailConfig.setPassword(EMAIL_PASSWORD);
        emailConfig.setUsername(EMAIL_USERNAME);
        return emailConfig;
    }

    @Test
    @DisplayName("Email confirmation mime message")
    void getPreparedEmailConfirmationMimeMessage() throws Exception {
        var context = new Context();
        context.setVariable("firstname", FIRSTNAME_VARIABLE);
        context.setVariable("confirmationUrl", CONFIRMATION_URL);
        assertNotNull(EMAIL_CONFIRMATION.getPreparedMimeMessage(EMAIL_SEND_TO, EMAIL_SUBJECT, context));
    }

}