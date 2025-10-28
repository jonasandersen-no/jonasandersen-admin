package no.jonasandersen.admin.config;

import no.jonasandersen.admin.infrastructure.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@Import({IoBasedConfiguration.class, TestSecurityConfiguration.class})
public abstract class IoBasedTest extends IntegrationTest {

}
