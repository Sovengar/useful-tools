package testing.rest;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * REST Assured Showcase Test
 * 
 * REST Assured permite testar APIs REST de forma fluida y legible.
 * En este ejemplo lo combinamos con WireMock para simular el servidor.
 */
@WireMockTest
class RestAssuredShowcaseTest {

    @BeforeEach
    void setup(WireMockRuntimeInfo runtime) {
        // Configurar el puerto base para REST Assured
        RestAssured.port = runtime.getHttpPort();
    }

    @Test
    @DisplayName("Petición GET y validación básica")
    void getRequest() {
        stubFor(get("/api/ping").willReturn(ok("pong")));

        given()
                .when()
                .get("/api/ping")
                .then()
                .statusCode(200)
                .body(equalTo("pong"));
    }

    @Test
    @DisplayName("Validación de JSON complejo")
    void jsonValidation() {
        stubFor(get("/api/users/1")
                .willReturn(okJson("""
                        {
                            "id": 1,
                            "name": "John Doe",
                            "roles": ["ADMIN", "USER"],
                            "address": {
                                "city": "Madrid"
                            }
                        }
                        """)));

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/users/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("name", is("John Doe"))
                .body("roles", hasItems("ADMIN"))
                .body("address.city", equalTo("Madrid"));
    }

    @Test
    @DisplayName("Petición POST con body")
    void postRequest() {
        stubFor(post("/api/echo")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"received\": true}")));

        given()
                .contentType(ContentType.JSON)
                .body("{\"test\": \"data\"}")
                .when()
                .post("/api/echo")
                .then()
                .statusCode(201)
                .body("received", is(true));
    }

    @Test
    @DisplayName("Validación de Headers y Cookies")
    void headerAndCookieValidation() {
        stubFor(get("/api/secure")
                .willReturn(ok()
                        .withHeader("X-Custom-Header", "Value123")
                        .withHeader("Set-Cookie", "session=xyz")));

        given()
                .when()
                .get("/api/secure")
                .then()
                .header("X-Custom-Header", "Value123")
                .cookie("session", "xyz");
    }
}
