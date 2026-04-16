package com.qatest.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseTest {

    protected static String accessToken;
    protected static Properties props = new Properties();

    @BeforeAll
    static void setup() throws IOException {
        InputStream input = BaseTest.class
            .getClassLoader()
            .getResourceAsStream("test.properties");
        props.load(input);

        String baseURI = System.getProperty("baseURI", "https://dummyjson.com");
        RestAssured.baseURI = baseURI;

        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{\"username\": \"" + props.getProperty("username") + 
                      "\", \"password\": \"" + props.getProperty("password") + "\"}")
            .when()
                .post("/auth/login")
            .then()
                .extract().response();

        accessToken = response.jsonPath().getString("accessToken");
    }
}