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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionTypeId;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InterventionController.class)
class InterventionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InterventionService interventionService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void getInterventionsReturnsArrayOfInterventions() throws Exception {
        when(interventionService.getAllInterventionTypes())
                .thenReturn(List.of(InterventionType.builder().id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")).name("Skills for Life - Literacy").build(),
                        InterventionType.builder().id(UUID.fromString("8b7894d5-1c0d-453e-a831-a1e55e868c2e")).name("Violence Booster").build()));

        mockMvc.perform(get("/interventiontype").with(jwt()))
                .andExpect(content().json("[{\"id\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", \"name\": \"Skills for Life - Literacy\" }," +
                                          "{\"id\": \"8b7894d5-1c0d-453e-a831-a1e55e868c2e\", \"name\": \"Violence Booster\" }]"));
    }

    @Test
    void createInterventionCallsSaveAndReturnsIntervention() throws Exception {
        when(interventionService.createInterventionType(any(CreateInterventionTypeRequest.class))).thenReturn(new DataEvent<>(InterventionType.builder()
                .id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                .name("Skills for Life - Literacy")
                .deliusCode("SFL")
                .active(true)
                .build(), DataEventType.CREATED));

        mockMvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Skills for Life - Literacy\", \"deliusCode\":\"SFL\", \"active\":true }")
                .with(jwt()))
                .andExpect(content().json("    { \"id\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", \"name\": \"Skills for Life - Literacy\", \"deliusCode\":\"SFL\", \"active\":true }"));

        verify(interventionService, times(1))
                .createInterventionType(CreateInterventionTypeRequest.builder()
                        .name("Skills for Life - Literacy")
                        .deliusCode("SFL")
                        .active(true)
                        .build()
                );
    }

    @Test
    void getInterventionSubTypes() throws Exception {
        when(interventionService.getInterventionType(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")))
                .thenReturn(InterventionType.builder()
                        .id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                        .name("Skills for Life - Literacy")
                        .interventionSubTypes(Set.of(InterventionSubType.builder()
                                        .name("Test subtype 1")
                                        .id(UUID.fromString("1a72fd6c-a845-46f9-b6d1-b07596a33019"))
                                        .build(),
                                InterventionSubType.builder()
                                        .name("Test subtype 2")
                                        .id(UUID.fromString("3cfd2a07-8c01-4645-81ac-c107348179a8"))
                                        .build()))
                        .build());

        mockMvc.perform(get("/interventiontype/2e18d2f6-2a38-4cdf-a798-00b0a2e6994d/subtype").with(jwt()))
                .andExpect(content().json("[{\"id\": \"1a72fd6c-a845-46f9-b6d1-b07596a33019\", \"name\": \"Test subtype 1\" }," +
                                          "{\"id\": \"3cfd2a07-8c01-4645-81ac-c107348179a8\", \"name\": \"Test subtype 2\" }]"));
    }

    @Test
    void getInterventionProviders() throws Exception {
        when(interventionService.getInterventionType(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")))
                .thenReturn(InterventionType.builder()
                        .id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                        .name("Skills for Life - Literacy")
                        .providerInterventionTypes(Set.of(ProviderInterventionType.builder().provider(Provider.builder()
                                        .name("Provider 1")
                                        .id(UUID.fromString("1a72fd6c-a845-46f9-b6d1-b07596a33019"))
                                        .build()).build(),
                                ProviderInterventionType.builder().provider(Provider.builder()
                                        .name("Provider 2")
                                        .id(UUID.fromString("3cfd2a07-8c01-4645-81ac-c107348179a8"))
                                        .build()).build()))
                        .build());

        mockMvc.perform(get("/interventiontype/2e18d2f6-2a38-4cdf-a798-00b0a2e6994d/provider").with(jwt()))
                .andExpect(content().json("[{\"id\": \"1a72fd6c-a845-46f9-b6d1-b07596a33019\", \"name\": \"Provider 1\" }," +
                                          "{\"id\": \"3cfd2a07-8c01-4645-81ac-c107348179a8\", \"name\": \"Provider 2\" }]"));
    }

    @Test
    void createSubType() throws Exception {
        when(interventionService.createInterventionSubType(any(CreateInterventionSubTypeRequest.class))).thenReturn(new DataEvent<>(InterventionSubType.builder()
                .id(UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f"))
                .name("Test subtype 1")
                .deliusCode("TEST1")
                .active(true)
                .build(), DataEventType.CREATED));

        mockMvc.perform(post("/interventiontype/2e18d2f6-2a38-4cdf-a798-00b0a2e6994d/subtype")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test subtype 1\", \"deliusCode\": \"TEST1\", \"active\":true }")
                .with(jwt()))
                .andExpect(content().json("    { \"id\": \"4b2f8eed-e426-4555-82b5-55ad103c235f\", \"name\": \"Test subtype 1\", \"deliusCode\": \"TEST1\", \"active\":true }"));

        verify(interventionService, times(1))
                .createInterventionSubType(CreateInterventionSubTypeRequest.builder()
                        .name("Test subtype 1")
                        .interventionTypeId(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                        .deliusCode("TEST1")
                        .active(true)
                        .build()
                );
    }

    @Test
    void linkProviderToType() throws Exception {
        when(interventionService.createProviderTypeLink(any(CreateProviderTypeLinkRequest.class))).thenReturn(new DataEvent<>(ProviderInterventionType.builder()
                .provider(Provider.builder().id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")).deliusCode("P1").build())
                .interventionType(InterventionType.builder().id(UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f")).deliusCode("IT1").build())
                .id(
                        new ProviderInterventionTypeId(
                                UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"),
                                UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f"))
                )
                .build(), DataEventType.CREATED));

        mockMvc.perform(post("/interventiontype/4b2f8eed-e426-4555-82b5-55ad103c235f/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"providerId\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\"}")
                .with(jwt()))
                .andExpect(content().json("{ \"interventionTypeId\": \"4b2f8eed-e426-4555-82b5-55ad103c235f\", " +
                                          "\"providerId\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", " +
                                          "\"deliusProviderCode\": \"P1\", " +
                                          "\"deliusInterventionCode\": \"IT1\"}"));

        verify(interventionService, times(1))
                .createProviderTypeLink(CreateProviderTypeLinkRequest.builder()
                        .providerId(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                        .interventionTypeId(UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f"))
                        .build()
                );
    }

    @Test
    void unlinkProviderFromType() throws Exception {
        when(interventionService.deleteProviderTypeLink(any(UUID.class), any(UUID.class))).thenReturn(new DataEvent<>(ProviderInterventionType.builder()
                .provider(Provider.builder().id(UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")).deliusCode("P1").build())
                .interventionType(InterventionType.builder().id(UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f")).deliusCode("IT1").build())
                .id(
                        new ProviderInterventionTypeId(
                                UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f"),
                                UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"))
                )
                .build(), DataEventType.CREATED));

        mockMvc.perform(delete("/interventiontype/4b2f8eed-e426-4555-82b5-55ad103c235f/provider/2e18d2f6-2a38-4cdf-a798-00b0a2e6994d")
                .with(jwt()))
                .andExpect(content().json("{ \"interventionTypeId\": \"4b2f8eed-e426-4555-82b5-55ad103c235f\", " +
                                          "\"providerId\": \"2e18d2f6-2a38-4cdf-a798-00b0a2e6994d\", " +
                                          "\"deliusProviderCode\": \"P1\", " +
                                          "\"deliusInterventionCode\": \"IT1\"}"));

        verify(interventionService, times(1))
                .deleteProviderTypeLink(UUID.fromString("4b2f8eed-e426-4555-82b5-55ad103c235f"),
                                        UUID.fromString("2e18d2f6-2a38-4cdf-a798-00b0a2e6994d"));
    }
}

