package in.regres;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class TestsExamples {

    @Test
    void listUsersSurnamesTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.findAll { it.id < 9 }.last_name", hasItems("Lawson", "Ferguson"));
    }

    @Test
    void singleResourceTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("data",hasKey("color"))
                .body("data.color",equalTo("#C74375"));
    }

    @Test
    void createUserTest() {
        String data = "{ \"name\": \"qa\", \"surname\": \"guru\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("createdAt", greaterThan("2022-11-27T16:50:45.484Z"));
    }

    @Test
    void registerUnsuccessfulTest() {
        String data = "{ \"email\": \"qa@guru.guru\" }";
        given()
                .log().uri()
                .when()
                .contentType(JSON)
                .body(data)
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void deleteUserTest() {
        given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
