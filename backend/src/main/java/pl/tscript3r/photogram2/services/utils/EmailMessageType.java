package pl.tscript3r.photogram2.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.tscript3r.photogram2.configs.EmailConfig;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;

public enum EmailMessageType {

    EMAIL_CONFIRMATION("EmailConfirmationTemplate"),
    PASSWORD_RESET("PasswordResetTemplate");

    private EmailConfig emailConfig;
    private TemplateEngine templateEngine;
    private String templateFile;

    EmailMessageType(String templateFile) {
        this.templateFile = templateFile;
    }

    private void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    private void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Component
    public static class EmailMessageCreatorInjector {

        private EmailConfig emailConfig;
        private TemplateEngine templateEngine;

        @Autowired
        public void setEmailConfig(EmailConfig emailConfig) {
            this.emailConfig = emailConfig;
        }

        @Autowired
        public void setTemplateEngine(TemplateEngine templateEngine) {
            this.templateEngine = templateEngine;
        }

        @PostConstruct
        public void postConstruct() {
            for (EmailMessageType emailEnumType : EnumSet.allOf(EmailMessageType.class)) {
                emailEnumType.setEmailConfig(emailConfig);
                emailEnumType.setTemplateEngine(templateEngine);
            }
        }

    }

    public MimeMessagePreparator getPreparedMimeMessage(@NotNull final String sendToEmail,
                                                        @NotNull final String subject,
                                                        @NotNull final Context context) {
        return mimeMessage -> {
            final MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
            mailMessage.setPriority(1);
            mailMessage.setTo(sendToEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(templateEngine.process(templateFile, context), true);
            mailMessage.setFrom(new InternetAddress(emailConfig.getUsername()));
        };
    }

}
