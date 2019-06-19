package pl.tscript3r.photogram2.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("photogram.email")
public class EmailConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;

}
