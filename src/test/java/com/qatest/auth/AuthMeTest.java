package com.qatest.auth;

import com.qatest.base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class AuthMeTest extends BaseTest {

    @Test
    public void authTest() {
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("/auth/me")
            .then()
                .extract().response();

        assertEquals(200, response.getStatusCode());
        assertEquals("Green", response.jsonPath().getString("eyeColor"));
        assertEquals("Brown", response.jsonPath().getString("hair.color"));
        assertEquals(29, response.jsonPath().getInt("age"));
        assertEquals(193.24, response.jsonPath().getDouble("height"), 0.01);
    }

    @Test
    public void addUserTest() {
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body(Map.of("firstName", "Test", "tags", Arrays.asList("qa", "automation")))
            .when()
                .post("/users/add")
            .then()
                .extract().response();

        assertEquals(201, response.getStatusCode());
    }
}