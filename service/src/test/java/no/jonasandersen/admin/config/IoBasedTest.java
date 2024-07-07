package no.jonasandersen.admin.config;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@Import(IoBasedConfiguration.class)
@ActiveProfiles("integration")
@Tag("integration")
@WithMockUser
public abstract class IoBasedTest {

}
