package uk.gov.justice.digital.hmpps.interventionscatalogue.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionSubTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.integration.MvcIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterventionsCatalogueIntegrationTest extends MvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private ProviderDto createProvider() throws Exception {
        MvcResult providerResult = mvc.perform(post("/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderRequest.builder()
                        .name("North West Provider")
                        .deliusCode("NWCRC")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(providerResult.getResponse().getContentAsString(), ProviderDto.class);
    }

    private InterventionTypeResponse createInterventionType() throws Exception {
        MvcResult interventionTypeResult = mvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionTypeRequest.builder()
                        .name("Violence Booster")
                        .deliusCode("VBOOST")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(interventionTypeResult.getResponse().getContentAsString(), InterventionTypeResponse.class);
    }

    private InterventionSubTypeResponse createSubtype(InterventionTypeResponse it) throws Exception {
        MvcResult interventionSubtypeResult = mvc.perform(post(String.format("/interventiontype/%s/subtype", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionSubTypeRequest.builder()
                        .name("Test subtype")
                        .deliusCode("TEST")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(interventionSubtypeResult.getResponse().getContentAsString(), InterventionSubTypeResponse.class);
    }

    @Test
    public void whenValidBearerTokenGetInterventionsReturnsInterventionsList()
            throws Exception {

        mvc.perform(get("/interventiontype").with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void whenNoBearerTokenGetInterventionsReturnsUnauthorized()
            throws Exception {

        mvc.perform(get("/interventiontype"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createProviderTest() throws Exception {
        MvcResult providerResult = mvc.perform(post("/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderRequest.builder()
                        .name("North West Provider")
                        .deliusCode("NWCRC")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderDto provider = objectMapper.readValue(providerResult.getResponse().getContentAsString(), ProviderDto.class);

        assertThat(provider.getId()).isNotNull();
        assertThat(provider.getName()).isEqualTo("North West Provider");
    }

    @Test
    public void deleteProviderTest() throws Exception {
        ProviderDto provider = createProvider();

        MvcResult deleteProviderResult = mvc.perform(delete(String.format("/provider/%s", provider.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(deleteProviderResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void getAllProvidersTest() throws Exception {
        MvcResult getAllProviderResult = mvc.perform(get("/provider")
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        List<ProviderDto> providers = objectMapper.readValue(getAllProviderResult.getResponse().getContentAsString(), new TypeReference<ArrayList<ProviderDto>>() {});

        createProvider();

        MvcResult getAllProviderResult2 = mvc.perform(get("/provider")
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        List<ProviderDto> providers2 = objectMapper.readValue(getAllProviderResult2.getResponse().getContentAsString(), new TypeReference<ArrayList<ProviderDto>>() {});

        assertThat(providers2).hasSize(providers.size() + 1);
    }

    @Test
    public void getProviderTest() throws Exception {
        ProviderDto createdProvider = createProvider();

        MvcResult getProviderResult = mvc.perform(get(String.format("/provider/%s", createdProvider.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();

        ProviderDto provider = objectMapper.readValue(getProviderResult.getResponse().getContentAsString(), ProviderDto.class);

        assertThat(provider.getId()).isEqualTo(createdProvider.getId());
    }

    @Test
    public void modifyProviderTest() throws Exception {
        ProviderDto createdProvider = createProvider();

        MvcResult getProviderResult = mvc.perform(put(String.format("/provider/%s", createdProvider.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UpdateProviderRequest.builder()
                        .name("Modified provider name")
                        .active(true)
                        .deliusCode("NWCRC")
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();

        ProviderDto modifiedProvider = objectMapper.readValue(getProviderResult.getResponse().getContentAsString(), ProviderDto.class);
        assertThat(modifiedProvider.getName()).isEqualTo("Modified provider name");
    }

    @Test
    public void createInterventionTypeTest() throws Exception {
        MvcResult interventionTypeResult = mvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionTypeRequest.builder()
                        .name("Violence Booster")
                        .deliusCode("VBOOST")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeResponse it = objectMapper.readValue(interventionTypeResult.getResponse().getContentAsString(), InterventionTypeResponse.class);
        assertThat(it.getName()).isEqualTo("Violence Booster");
    }

    @Test
    public void deleteInterventionTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();
        //delete type
        MvcResult deleteTypeResult = mvc.perform(delete(String.format("/interventiontype/%s", it.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();

        assertThat(deleteTypeResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void createInterventionSubTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();

        MvcResult interventionSubtypeResult = mvc.perform(post(String.format("/interventiontype/%s/subtype", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionSubTypeRequest.builder()
                        .name("Test subtype")
                        .deliusCode("TEST")
                        .active(true)
                        .build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionSubTypeResponse ist = objectMapper.readValue(interventionSubtypeResult.getResponse().getContentAsString(), InterventionSubTypeResponse.class);

        assertThat(ist.getName()).isEqualTo("Test subtype");
    }

    @Test
    public void removeSubtypeFromTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();

        InterventionSubTypeResponse ist = createSubtype(it);

        //delete subtype from type
        MvcResult deleteSubtypeFromTypeResult = mvc.perform(delete(String.format("/interventiontype/%s/subtype/%s", it.getId(), ist.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(deleteSubtypeFromTypeResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void getInterventionTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();

        MvcResult getTypeResult = mvc.perform(get(String.format("/interventiontype/%s", it.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(getTypeResult.getResponse().getStatus()).isEqualTo(200);
        InterventionTypeResponse interventionTypeResponse = objectMapper.readValue(getTypeResult.getResponse().getContentAsString(), InterventionTypeResponse.class);
    }

    @Test
    public void addProviderToTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();
        ProviderDto createdProvider = createProvider();

        // link provider to type
        MvcResult interventionProviderResult = mvc.perform(post(String.format("/interventiontype/%s/provider", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderTypeLinkRequest.builder().providerId(createdProvider.getId()).build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderTypeLinkResponse itd = objectMapper.readValue(interventionProviderResult.getResponse().getContentAsString(), ProviderTypeLinkResponse.class);
    }

    @Test
    public void removeProviderFromTypeTest() throws Exception {
        InterventionTypeResponse it = createInterventionType();
        ProviderDto createdProvider = createProvider();

        // link provider to type
        MvcResult interventionProviderResult = mvc.perform(post(String.format("/interventiontype/%s/provider", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderTypeLinkRequest.builder().providerId(createdProvider.getId()).build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderTypeLinkResponse itd = objectMapper.readValue(interventionProviderResult.getResponse().getContentAsString(), ProviderTypeLinkResponse.class);

        //remove provider from type
        MvcResult deleteLinkedProviderFromTypeResult = mvc.perform(delete(String.format("/interventiontype/%s/provider/%s", it.getId(), createdProvider.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(deleteLinkedProviderFromTypeResult.getResponse().getStatus()).isEqualTo(200);
    }
}
