package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProviderController.class)
class ProviderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    InterventionService interventionService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void getProviders() throws Exception {
        when(interventionService.getAllProviders())
                .thenReturn(List.of(Provider.builder().id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")).name("North West CRC").build(),
                        Provider.builder().id(UUID.fromString("8b7894d5-1c0d-453e-a831-a1e55e868c2e")).name("Wales CRC").build()));

        mockMvc.perform(get("/provider").with(jwt()))
                .andExpect(content().json("[{\"id\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", \"name\": \"North West CRC\" }," +
                                          "{\"id\": \"8b7894d5-1c0d-453e-a831-a1e55e868c2e\", \"name\": \"Wales CRC\" }]"));
    }

    @Test
    void createProvider() throws Exception {
        when(interventionService.createProvider(any(CreateProviderRequest.class))).thenReturn(new DataEvent<Provider>(Provider.builder()
                .id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                .name("North West CRC")
                .deliusCode("NWCRC")
                .active(true)
                .build(), DataEventType.CREATED));

        mockMvc.perform(post("/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"North West CRC\", \"deliusCode\":\"NWCRC\", \"active\":true}")
                .with(jwt()))
                .andExpect(content().json("    { \"id\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", \"name\": \"North West CRC\", \"deliusCode\":\"NWCRC\", \"active\":true}"));

        verify(interventionService, times(1))
                .createProvider(CreateProviderRequest.builder()
                        .name("North West CRC")
                        .deliusCode("NWCRC")
                        .active(true)
                        .build()
                );
    }
}
