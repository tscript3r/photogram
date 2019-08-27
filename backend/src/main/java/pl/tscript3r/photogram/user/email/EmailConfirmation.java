package pl.tscript3r.photogram.user.email;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.photogram.infrastructure.AbstractEntity;
import pl.tscript3r.photogram.user.User;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "confirmations")
@Cacheable
public class EmailConfirmation extends AbstractEntity {

    @OneToOne
    private User user;

    @Setter
    private Boolean confirmed = false;

    @Setter
    private UUID token;

    @CreationTimestamp
    private LocalDateTime creationDate;

    public EmailConfirmation() {
    }

    public EmailConfirmation(User user, UUID token, Boolean confirmed) {
        this.user = user;
        this.token = token;
        this.confirmed = confirmed;
    }

}
