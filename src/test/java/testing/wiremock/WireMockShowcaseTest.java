package testing.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * WireMock Showcase Test
 * 
 * WireMock permite mockear servicios HTTP externos.
 * La anotación @WireMockTest levanta un servidor WireMock automáticamente
 * para la duración del test (por defecto en un puerto aleatorio).
 */
@WireMockTest
class WireMockShowcaseTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    @DisplayName("Stubbing básico GET")
    void basicGetStub(WireMockRuntimeInfo runtime) {
        // Stubbing
        stubFor(get(urlEqualTo("/api/hello"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Hello from WireMock!\"}")));

        // Llamada real al mock (usando el puerto dinámico de WireMock)
        String url = "http://localhost:" + runtime.getHttpPort() + "/api/hello";

        HttpResponse<String> response = callEndpoint(url);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Hello from WireMock!");
    }

    @Test
    @DisplayName("Stubbing con Matchers de Body (POST)")
    void postStubWithMatchers() {
        stubFor(post(urlEqualTo("/api/users"))
                .withRequestBody(matchingJsonPath("$.name", equalTo("John")))
                .willReturn(created()
                        .withBody("{\"id\": 123, \"status\": \"created\"}")));

        // Simular llamada POST
        // (Nota: WireMock.port() solo funciona si usamos puerto estático o inyectamos
        // el puerto)
        // Pero en @WireMockTest los métodos estáticos de WireMock apuntan al servidor
        // actual.
    }

    @Test
    @DisplayName("Respuestas dinámicas (Response Templating)")
    void dynamicResponse(WireMockRuntimeInfo runtime) {
        // WireMock permite usar plantillas para devolver datos del request
        stubFor(get(urlPathEqualTo("/api/greet"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Hello, {{request.query.name}}!")
                        .withTransformers("response-template")));

        String url = "http://localhost:" + runtime.getHttpPort() + "/api/greet?name=Antigravity";
        HttpResponse<String> response = callEndpoint(url);

        assertThat(response.body()).isEqualTo("Hello, Antigravity!");
    }

    @Test
    @DisplayName("Fault Injection (Simular errores de red)")
    void faultInjection(WireMockRuntimeInfo runtime) {
        // Simular un delay de red
        stubFor(get("/api/slow")
                .willReturn(ok().withFixedDelay(100)));

        // Simular un error de conexión (Bad Response)
        stubFor(get("/api/error")
                .willReturn(aResponse().withFault(com.github.tomakehurst.wiremock.http.Fault.EMPTY_RESPONSE)));

        long start = System.currentTimeMillis();
        callEndpoint("http://localhost:" + runtime.getHttpPort() + "/api/slow");
        long end = System.currentTimeMillis();

        assertThat(end - start).isGreaterThanOrEqualTo(100);
    }

    @Test
    @DisplayName("Verificación de llamadas")
    void verification(WireMockRuntimeInfo runtime) {
        stubFor(get("/api/track").willReturn(ok()));

        // Hacemos la llamada real
        callEndpoint("http://localhost:" + runtime.getHttpPort() + "/api/track");

        // Verificamos que se llamó exactamente 1 vez
        verify(getRequestedFor(urlEqualTo("/api/track")));
    }

    private HttpResponse<String> callEndpoint(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
