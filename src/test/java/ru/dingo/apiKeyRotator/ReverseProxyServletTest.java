package ru.dingo.apiKeyRotator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReverseProxyServletTest {
    private String baseUrl = "http://localhost:8180/admin/endpoints";
    private String baseUrl2 = "http://localhost:8180/admin/endpoint/";

    private String hostnameUrl = "http://localhost:8180";

    private int initialCount;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = baseUrl;

        // Get initial count of "card-header" occurrences
        Response initialResponse = given()
                .get("");

        initialResponse.then()
                .statusCode(200);
        initialCount = initialResponse.body().asString().split("card-header").length - 1;
    }

    @Test
    public void testGetRequest() {
        Response response = given().get("");
        int currentCount = response.body().asString().split("card-header").length - 1;

        assertEquals(initialCount, currentCount, "Count of 'card-header' should remain the same after GET request");
    }

    @Test
    public void testPostRequest() {
        // Create a unique endpoint name using current timestamp
        String endpointName = "test" + Instant.now().toEpochMilli();

        // Send POST request with endpoint-name parameter
        given()
                .param("endpoint-name", endpointName)
                .post("")
                .then()
                .statusCode(302);

        // Get count after POST request
        Response response = given().get("");
        int updatedCount = response.body().asString().split("card-header").length - 1;

        assertEquals(initialCount + 1, updatedCount, "Count of 'card-header' should increase by 1 after POST request");
    }

    @Test
    public void testSecondUrlRequests() {
        String endpointName = "test" + Instant.now().toEpochMilli();

        // Send POST request with endpoint-name parameter
        given()
                .param("endpoint-name", endpointName)
                .post("");

        // Get URL based on previously created endpoint
        String secondUrl = baseUrl2 + endpointName;

        // Step 1: Perform GET request on the second URL
        Response getResponse = given().baseUri(secondUrl).get(secondUrl);
        getResponse.then().statusCode(200);

        // Step 2: Prepare form data
        String stateEndpoint = "Enabled";
        String from = "/"+endpointName;
        String to = "https://httpbin.org";
        String whereInsertStr = "PARAM";
        String keyName = "token";
        String timeConditionStr = "3/5,4/10";
        String keypack = "SpotifyKeys";

        // Send POST request with form parameters
        given()
                .param("endpointName", endpointName)
                .param("state-endpoint", stateEndpoint)
                .param("from", from)
                .param("to", to)
                .param("where-insert", whereInsertStr)
                .param("keyName", keyName)
                .param("timeCondition", timeConditionStr)
                .param("keypack", keypack)
                .baseUri(secondUrl)
                .post(secondUrl)
                .then()
                .statusCode(302); // Expecting a redirect
    }

    @Test
    public void testReplacedToken(){
        // wait 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String endpointName = "test" + Instant.now().toEpochMilli();

        // Send POST request with endpoint-name parameter
        given()
                .param("endpoint-name", endpointName)
                .post("");

        // Get URL based on previously created endpoint
        String secondUrl = baseUrl2 + endpointName;

        // Step 1: Perform GET request on the second URL
        Response getResponse = given().baseUri(secondUrl).get(secondUrl);
        getResponse.then().statusCode(200);

        // Step 2: Prepare form data
        String stateEndpoint = "Enabled";
        String from = "/"+endpointName;
        String to = "https://httpbin.org";
        String whereInsertStr = "PARAM";
        String keyName = "token";
        String timeConditionStr = "3/5,4/10";
        String keypack = "SpotifyKeys";

        // Send POST request with form parameters
        given()
                .param("endpointName", endpointName)
                .param("state-endpoint", stateEndpoint)
                .param("from", from)
                .param("to", to)
                .param("where-insert", whereInsertStr)
                .param("keyName", keyName)
                .param("timeCondition", timeConditionStr)
                .param("keypack", keypack)
                .baseUri(secondUrl)
                .post(secondUrl)
                .then()
                .statusCode(302); // Expecting a redirect

        // do request to endpoint3 with some token in param
        Response response = given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get");
        response.then().statusCode(200);
        Response jsonResponse = response.then().contentType(ContentType.JSON).extract().response();
        // check args.token is different from 123
        assertNotEquals("123", jsonResponse.jsonPath().getString("args.token"));


        given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get")
                .then()
                .statusCode(200);


        given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get")
                .then()
                .statusCode(200);


        given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get")
                .then()
                .statusCode(404);

        // Wait 6 second
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get")
                .then()
                .statusCode(200);

        given()
                .param("token", "123")
                .baseUri(hostnameUrl)
                .get("/"+endpointName+"/get")
                .then()
                .statusCode(404);

    }


}