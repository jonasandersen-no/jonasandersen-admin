package no.jonasandersen.admin.config;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration")
@Tag("integration")
public abstract class IntegrationTest {

}
