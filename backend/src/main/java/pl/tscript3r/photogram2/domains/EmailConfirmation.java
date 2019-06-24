package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "confirmations")
public class EmailConfirmation extends DomainEntity {

    @OneToOne
    private User user;

    @Setter
    private Boolean confirmed = false;

    @Setter
    private UUID token;

    @CreationTimestamp
    private LocalDateTime creationDate;

    EmailConfirmation() {
    }

    public EmailConfirmation(User user, UUID token, Boolean confirmed) {
        this.user = user;
        this.token = token;
        this.confirmed = confirmed;
    }

}
