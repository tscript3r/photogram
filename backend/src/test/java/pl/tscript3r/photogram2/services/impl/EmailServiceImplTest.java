package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.domains.EmailConfirmation;
import pl.tscript3r.photogram2.repositories.EmailConfirmationRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;

@DisplayName("Email service")
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final Long ID = 1L;
    private static final UUID DEFAULT_UUID = UUID.randomUUID();

    private static EmailConfirmation getDefaultEmailConfirmation() {
        var emailConfirmation = new EmailConfirmation(getDefaultUser(), DEFAULT_UUID, true);
        emailConfirmation.setId(ID);
        return emailConfirmation;
    }

    @Mock
    EmailConfirmationRepository emailConfirmationRepository;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    @DisplayName("New email confirmation")
    void emailConfirmation() {
        var emailConfirmation = getDefaultEmailConfirmation();
        when(emailConfirmationRepository.findByUser(any())).thenReturn(Optional.of(emailConfirmation));
        assertEquals(emailConfirmation, emailService.emailConfirmation(getDefaultUser(), false, true));

    }

    @Test
    void resendEmailConfirmation() {
    }

    @Test
    void updateEmailConfirmation() {
    }
}