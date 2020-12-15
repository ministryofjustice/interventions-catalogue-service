package uk.gov.justice.digital.hmpps.interventionscatalogue.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MvcIntegrationTest {
    protected String hmppsAuthToken = "eyJraWQiOiJkZXYtandrLWtpZCIsImFsZyI6IlJTMjU2In0.ewogICJzY29wZSI6IFsKICAgICJyZWFkIgogIF0sCiAgImF1dGhfc291cmNlIjogIm5vbmUiLAogICJpc3MiOiAiaHR0cHM6Ly9nYXRld2F5LnQzLm5vbWlzLWFwaS5obXBwcy5kc2QuaW8vYXV0aC9pc3N1ZXIiLAogICJleHAiOiAyMDg0MDEwNzE5LAogICJhdXRob3JpdGllcyI6IFsKICAgICJST0xFX1NZU1RFTV9VU0VSIgogIF0sCiAgImp0aSI6ICJiMTE0YWYyOC0xMWI4LTQ5ZDgtYWNiMS1kN2MxMDQzNWQ3OWEiLAogICJjbGllbnRfaWQiOiAiaW50ZXJ2ZW50aW9ucy1jYXRhbG9ndWUtc3lzdGVtIgp9Cg.Ghbjg2CjgX8hzQdeRTnazjIlpirxQcGI84nnmfgJRKoqq_KGjBSWv9AEPKfEXvUaGEBNrYHxL3oYYo5H7wfyHGMWPcLa9Szivyjjx2bFUxYuQR1EL6r1aOtnfhyQkcKLvvbcXKYcWdkYX8rjROUfs0-oFOnuv8upL7fFM33KaCvc8Aqn4pUJhhUuTDCD4ZElpRRVFxHtJRMqb0dtU6Rde4E7pYDzauRRs8Ab3DFX5MT2TNNro1av-2ALrLGPa4B82L-OSQ3JBm8l92i5gvRUHTkZfBXdddFWP6ek8r7lFWnrYCLOdp9kXRIWWPNEJ0w6tHhlTrSl6yLMHBTBYtretQ";

    @Autowired
    protected MockMvc mvc;

    public static MockOAuthServer mockOAuthServer = new MockOAuthServer();

    @BeforeAll
    public static void startMocks() {
        mockOAuthServer.start();
        mockOAuthServer.stubJwksSet();
    }

    @AfterAll
    public static void stopMocks() {
        mockOAuthServer.stop();
    }


    private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {
        private String token;

        BearerTokenRequestPostProcessor(String token) {
            this.token = token;
        }

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.addHeader("Authorization", "Bearer " + this.token);
            return request;
        }
    }

    protected static BearerTokenRequestPostProcessor bearerToken(String token) {
        return new BearerTokenRequestPostProcessor(token);
    }
}
