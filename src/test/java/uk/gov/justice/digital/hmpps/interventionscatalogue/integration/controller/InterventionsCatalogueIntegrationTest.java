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
import uk.gov.justice.digital.hmpps.interventionscatalogue.integration.MvcIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterventionsCatalogueIntegrationTest extends MvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

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
    public void journeyTest() throws Exception {
        // create provider
        MvcResult providerResult = mvc.perform(post("/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProvider.builder().name("North West Provider").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderDto provider = objectMapper.readValue(providerResult.getResponse().getContentAsString(), ProviderDto.class);

        MvcResult provider2Result = mvc.perform(post("/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProvider.builder().name("South East Provider").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderDto provider2 = objectMapper.readValue(providerResult.getResponse().getContentAsString(), ProviderDto.class);


        // get all providers
        MvcResult getAllProviderResult = mvc.perform(get("/provider")
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        List<ProviderDto> providers = objectMapper.readValue(getAllProviderResult.getResponse().getContentAsString(), new TypeReference<ArrayList<ProviderDto>>() {});

        assertThat(providers).hasSize(2);

        // create interventions type
        MvcResult interventionTypeResult = mvc.perform(post("/interventiontype")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionType.builder().name("Violence Booster").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeDto it = objectMapper.readValue(interventionTypeResult.getResponse().getContentAsString(), InterventionTypeDto.class);

        // create interventions subtype linked to type
        MvcResult interventionSubtypeResult = mvc.perform(post(String.format("/interventiontype/%s/subtype", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateInterventionSubType.builder().name("Test subtype").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionSubTypeDto istd = objectMapper.readValue(interventionSubtypeResult.getResponse().getContentAsString(), InterventionSubTypeDto.class);

        // link provider to type
        MvcResult interventionProviderResult = mvc.perform(post(String.format("/interventiontype/%s/provider", it.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProviderTypeLinkDto.builder().providerId(provider.getId()).build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        InterventionTypeDto itd = objectMapper.readValue(interventionProviderResult.getResponse().getContentAsString(), InterventionTypeDto.class);

        //update provider
        MvcResult updatedProviderResult = mvc.perform(put(String.format("/provider/%s", provider.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateProvider.builder().name("North West Regional Provider").build()))
                .with(bearerToken(this.hmppsAuthToken)))
                .andExpect(status().isOk()).andReturn();
        ProviderDto updatedProvider = objectMapper.readValue(updatedProviderResult.getResponse().getContentAsString(), ProviderDto.class);

        assertThat(updatedProvider.getName()).isEqualTo("North West Regional Provider");
        //delete provider

        //get older version of provider

    }
}
