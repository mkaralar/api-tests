package com.qatest.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.qatest.base.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PostTest extends BaseTest{

@Test
public void getPostTest(){

    Response response = RestAssured
    .given()
        .header("Content-Type", "application/json")
    .when()
      .get("/posts/1")
    .then()
        .extract().response();
    
    
        assertEquals(200, response.getStatusCode());
        assertEquals("His mother had always taught him", response.jsonPath().getString("title"));
        assertEquals(192, response.jsonPath().getInt("reactions.likes"));
        assertEquals("history", response.jsonPath().getString("tags[0]"));
        assertEquals(305, response.jsonPath().getInt("views"));
}

@Test
public void getPostCommentsTest(){

    Response response = RestAssured
    .given()
        .header("Content-Type", "application/json")
    .when()
      .get("/posts/1/comments")
    .then()
        .extract().response();
    
    
        assertEquals(200, response.getStatusCode());
        assertEquals(3, response.jsonPath().getInt("total"));
        assertEquals("leahw", response.jsonPath().getString("comments[0].user.username"));
        assertEquals(3, response.jsonPath().getList("comments").size());
}

@Test
public void addPostTest(){

    Response response = RestAssured
    .given()
        .header("Content-Type", "application/json")
        .body("{\"title\": \"Test Post\", \"userId\": 1}")
    .when()
      .post("/posts/add")
    .then()
        .extract().response();
    
    
        assertEquals(201, response.getStatusCode());
        assertEquals("Test Post", response.jsonPath().getString("title"));
        assertEquals(1, response.jsonPath().getInt("userId"));
}

 @Test
    public void getPostNotFoundTest() {

        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
            .when()
                .get("/posts/999")
            .then()
                .extract().response();

        assertEquals(404, response.getStatusCode());
        assertEquals("Post with id '999' not found", response.jsonPath().getString("message"));

    }
    
}
