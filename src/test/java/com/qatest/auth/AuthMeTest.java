package com.qatest.auth;
import com.qatest.base.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

public class AuthMeTest extends BaseTest {

    @Test
    public void authTest() {
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{\"username\": \"emilys\", \"password\": \"emilyspass\"}")
            .when()
                .post("/auth/login")
            .then()
                .extract().response();

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("accessToken"));

        String accessToken = response.jsonPath().getString("accessToken");

        Response responseSec = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("auth/me")
            .then()
                .extract().response();

        assertEquals(200, responseSec.getStatusCode());
        assertEquals("Green", responseSec.jsonPath().getString("eyeColor"));
        assertEquals("Brown", responseSec.jsonPath().getString("hair.color"));
        assertEquals(29, responseSec.jsonPath().getInt("age"));

        assertEquals(193.24, responseSec.jsonPath().getDouble("height"), 0.01);
        // assertEquals("193.24", responseSec.jsonPath().getString("height"));
    }

    @Test
    public void addUserTest(){

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