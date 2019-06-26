package pl.tscript3r.photogram;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Photogram IT")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}