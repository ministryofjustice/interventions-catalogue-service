package uk.gov.justice.digital.hmpps.interventionscatalogue.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class PingEndpointTest extends MvcIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPingReturnsPong() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(content().string("pong"));
    }
}
