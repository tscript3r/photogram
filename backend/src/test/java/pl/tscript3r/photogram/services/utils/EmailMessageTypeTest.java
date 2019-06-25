package pl.tscript3r.photogram.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import pl.tscript3r.photogram.configs.EmailConfig;

@DisplayName("Email message type")
class EmailMessageTypeTest {

    private static final String EMAIL_HOST = "smtp.mail.test";
    private static final String EMAIL_PASSWORD = "password";
    private static final String EMAIL_USERNAME = "username";
    private static final String EMAIL_SEND_TO = "sendTo@mail.com";
    private static final String EMAIL_SUBJECT = "subject";
    private static final Object FIRSTNAME_VARIABLE = "FirstnameVariable";
    private static final Object CONFIRMATION_URL = "https://www.photogram.com/confirm?token=234ASD41293213";

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
//        var context = new Context();
//        context.setVariable("firstname", FIRSTNAME_VARIABLE);
//        context.setVariable("confirmationUrl", CONFIRMATION_URL);
//        assertNotNull(EMAIL_CONFIRMATION.getPreparedMimeMessage(EMAIL_SEND_TO, EMAIL_SUBJECT, context));
    }

}