package no.jonasandersen.admin.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@Import(IoBasedConfiguration.class)
@WithMockUser
public abstract class IoBasedTest extends IntegrationTest {

}
