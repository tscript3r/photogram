package pl.tscript3r.photogram.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import pl.tscript3r.photogram.configs.EmailConfig;

import static pl.tscript3r.photogram.Consts.*;

@DisplayName("Email message type")
class EmailMessageTypeTest {

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