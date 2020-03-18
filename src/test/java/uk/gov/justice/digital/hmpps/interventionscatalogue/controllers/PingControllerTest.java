package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Intervention;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InterventionsController.class)
class PingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InterventionRepository interventionRepository;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void getInterventionsReturnsArrayOfInterventions() throws Exception {
        when(interventionRepository.findAll())
                .thenReturn(List.of(new Intervention(1L, "Skills for Life - Literacy"),
                        new Intervention(2L, "Violence Booster")));

        mockMvc.perform(get("/intervention").with(jwt()))
                .andExpect(content().json("""
                           [{
                           "id": 1,
                           	"name": "Skills for Life - Literacy"
                           },
                           {
                           "id": 2,
                           	"name": "Violence Booster"
                           }]
                        """));

        verify(interventionRepository, times(1))
                .findAll();
    }

    @Test
    void createInterventionCallsSaveAndReturnsIntervention() throws Exception {
        when(interventionRepository.save(any(Intervention.class))).
                thenAnswer((Answer<Intervention>) invocation -> {
            Intervention intervention = invocation.getArgument(0);
            intervention.setId(1L);
            return intervention;
        });

        mockMvc.perform(post("/intervention")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                           {
                           	"name": "Skills for Life - Literacy"
                           }
                        """)
                .with(jwt()))
                .andExpect(content().json("""
                            {
                                "id": 1,
                            	"name": "Skills for Life - Literacy"
                            }
                        """));

        verify(interventionRepository, times(1))
                .save(new Intervention(1L, "Skills for Life - Literacy"));
    }
}
