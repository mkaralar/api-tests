package com.qatest.auth;
import com.qatest.base.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseTest{

    @Test
    public void happyPathTest() {
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
    }
@Test
public void wrongPasswordTest() {
    Response response = RestAssured
        .given()
            .header("Content-Type", "application/json")
            .body("{\"username\":\"emilys\",\"password\":\"wrongpassword\"}")
        .when()
            .post("/auth/login")
        .then()
            .extract().response();

    assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 401);
    assertNull(response.jsonPath().getString("accessToken"));
}
@Test
public void plainBodyTest() {
    Response response = RestAssured
        .given()
            .header("Content-Type", "application/json")
            .body("{}")
        .when()
            .post("/auth/login")
        .then()
            .extract().response();

    assertEquals(400, response.getStatusCode());
    assertEquals("Username and password required", response.jsonPath().getString("message"));
    assertNull(response.jsonPath().getString("accessToken"));
}
@Test
public void sqlInjectionTest(){

    Response response = RestAssured
        .given()
            .header("Content-type","application/json")
            .body("{\"username\": \"admin'--\", \"password\": \"anything\"}")
        .when()
            .post("/auth/login")
        .then()
        .extract().response();

        assertEquals(400, response.getStatusCode());
        assertNull(response.jsonPath().getString("accessToken"));
        assertEquals("Invalid credentials", response.jsonPath().getString("message"));
        

}



}