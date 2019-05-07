package pl.tscript3r.photogram2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Photogram IT")
@Disabled("Photogram IT temporary disabled to save time")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Photogram2Application.class)
class Photogram2ApplicationTests {

    @Test
    void contextLoads() {
    }

}