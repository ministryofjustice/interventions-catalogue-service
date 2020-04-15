package uk.gov.justice.digital.hmpps.interventionscatalogue.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionSubTypeDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionTypeDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProvider;
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
                .content(objectMapper.writeValueAsString(CreateProvider.builder().name("North West Provider").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(providerResult.getResponse().getContentAsString(), ProviderDto.class);
    }

    private InterventionTypeDto createInterventionType() throws Exception {
        MvcResult interventionTypeResult = mvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionType.builder().name("Violence Booster").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(interventionTypeResult.getResponse().getContentAsString(), InterventionTypeDto.class);
    }

    private InterventionSubTypeDto createSubtype(InterventionTypeDto it) throws Exception {
        MvcResult interventionSubtypeResult = mvc.perform(post(String.format("/interventiontype/%s/subtype", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionSubType.builder().name("Test subtype").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readValue(interventionSubtypeResult.getResponse().getContentAsString(), InterventionSubTypeDto.class);
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
                .content(objectMapper.writeValueAsString(CreateProvider.builder().name("North West Provider").build()))
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
                .content(objectMapper.writeValueAsString(UpdateProvider.builder().name("Modified provider name").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();

        ProviderDto modifiedProvider = objectMapper.readValue(getProviderResult.getResponse().getContentAsString(), ProviderDto.class);
        assertThat(modifiedProvider.getName()).isEqualTo("Modified provider name");
    }

    @Test
    public void createInterventionTypeTest() throws Exception {
        MvcResult interventionTypeResult = mvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionType.builder().name("Violence Booster").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeDto it = objectMapper.readValue(interventionTypeResult.getResponse().getContentAsString(), InterventionTypeDto.class);
        assertThat(it.getName()).isEqualTo("Violence Booster");
    }

    @Test
    public void deleteInterventionTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();
        //delete type
        MvcResult deleteTypeResult = mvc.perform(delete(String.format("/interventiontype/%s", it.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();

        assertThat(deleteTypeResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void createInterventionSubTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();

        MvcResult interventionSubtypeResult = mvc.perform(post(String.format("/interventiontype/%s/subtype", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionSubType.builder().name("Test subtype").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionSubTypeDto ist = objectMapper.readValue(interventionSubtypeResult.getResponse().getContentAsString(), InterventionSubTypeDto.class);

        assertThat(ist.getName()).isEqualTo("Test subtype");
    }

    @Test
    public void removeSubtypeFromTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();

        InterventionSubTypeDto ist = createSubtype(it);

        //delete subtype from type
        MvcResult deleteSubtypeFromTypeResult = mvc.perform(delete(String.format("/interventiontype/%s/subtype/%s", it.getId(), ist.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(deleteSubtypeFromTypeResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void getInterventionTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();

        MvcResult getTypeResult = mvc.perform(get(String.format("/interventiontype/%s", it.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(getTypeResult.getResponse().getStatus()).isEqualTo(200);
        InterventionTypeDto interventionTypeDto = objectMapper.readValue(getTypeResult.getResponse().getContentAsString(), InterventionTypeDto.class);
    }

    @Test
    public void addProviderToTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();
        ProviderDto createdProvider = createProvider();

        // link provider to type
        MvcResult interventionProviderResult = mvc.perform(post(String.format("/interventiontype/%s/provider", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderTypeLinkDto.builder().providerId(createdProvider.getId()).build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeDto itd = objectMapper.readValue(interventionProviderResult.getResponse().getContentAsString(), InterventionTypeDto.class);
    }

    @Test
    public void removeProviderFromTypeTest() throws Exception {
        InterventionTypeDto it = createInterventionType();
        ProviderDto createdProvider = createProvider();

        // link provider to type
        MvcResult interventionProviderResult = mvc.perform(post(String.format("/interventiontype/%s/provider", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderTypeLinkDto.builder().providerId(createdProvider.getId()).build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeDto itd = objectMapper.readValue(interventionProviderResult.getResponse().getContentAsString(), InterventionTypeDto.class);

        //remove provider from type
        MvcResult deleteLinkedProviderFromTypeResult = mvc.perform(delete(String.format("/interventiontype/%s/provider/%s", it.getId(), createdProvider.getId()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        assertThat(deleteLinkedProviderFromTypeResult.getResponse().getStatus()).isEqualTo(200);
    }
}
