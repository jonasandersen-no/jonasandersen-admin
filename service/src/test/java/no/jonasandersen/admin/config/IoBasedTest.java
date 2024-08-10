package no.jonasandersen.admin.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@Import(IoBasedConfiguration.class)
public abstract class IoBasedTest extends IntegrationTest {

}
