package uk.gov.justice.digital.hmpps.interventionscatalogue.integration;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class MockOAuthServer extends WireMockServer {

    public MockOAuthServer() {
        super(9010);
    }

    private static final String JWKS_RESPONSE = """
            {
              "keys": [
                {
                  "kty": "RSA",
                  "e": "AQAB",
                  "kid": "dev-jwk-kid",
                  "n": "hu4ADRoY-9sYn3d0rZ3zo_iepSCDx-zOqIzb8kAIkkwfHJQJQlK-qBPwoUr07tStMxOJciOESx71DBaahvFNxrVezePQi-jEzrTmfFF08QJ8BAKjL1YEPqeUwooa1G5yZ2jh7k9lTKbrGhYNnsoNCxGeTF3iuBRl0EOGCo0elZeLfZe6n3JhaPQp5wMN8S3LAYJQ7k8qOJs35r_XXnFapOIsNV56CEPsYxx7dJ2FNzNRTDk3rVLhwaDsCRGzJwEsqt9X_WPRcrm7jDyGV2pSfk9-Pe0dey8RX196zzOGMQveYizqVMHordTDUXdZ-AEqE0BF00wRz8vuqoAWQQKH_w"
                }
              ]
            }
            """;

    public void stubJwksSet() {
        stubFor(
                get(urlEqualTo("/.well-known/jwks.json"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(JWKS_RESPONSE)));
    }
}
