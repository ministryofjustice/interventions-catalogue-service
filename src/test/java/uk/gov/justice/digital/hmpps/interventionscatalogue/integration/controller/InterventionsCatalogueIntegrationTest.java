package uk.gov.justice.digital.hmpps.interventionscatalogue.integration.controller;

import org.junit.jupiter.api.Test;
import uk.gov.justice.digital.hmpps.interventionscatalogue.integration.IntegrationTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterventionsCatalogueIntegrationTest extends IntegrationTest {
    @Test
    public void whenValidBearerTokenGetInterventionsReturnsInterventionsList()
            throws Exception {

        mvc.perform(get("/intervention").with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void whenNoBearerTokenGetInterventionsReturnsUnauthorized()
            throws Exception {

        mvc.perform(get("/intervention"))
                .andExpect(status().isUnauthorized());
    }
}
