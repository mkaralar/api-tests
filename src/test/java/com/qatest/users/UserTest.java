package com.qatest.users;

import org.junit.jupiter.api.Test;

import com.qatest.base.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTest extends BaseTest{

@Test
public void getUsersTest() {
    given()
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("users", notNullValue())
            .body("users.size()", greaterThan(0))
            .body("total", equalTo(208))
            .body("limit", equalTo(30))
            .body("skip", equalTo(0));
}

@Test
public void getUserByIdTest() {
    given()
        .pathParam("id", 1)
    .when()
        .get("/users/{id}")
    .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("firstName", equalTo("Emily"))
        .body("lastName", equalTo("Johnson"))
        .body("address.city", equalTo("Phoenix"))
        .body("company.name", equalTo("Dooley, Kozey and Cronin"));
}

@Test
public void searchUserTest() {
    given()
        .queryParam("q", "Emily")
    .when()
        .get("/users/search")
    .then()
        .statusCode(200)
        .body("users", notNullValue())
        .body("users.size()", greaterThan(0))
        .body("users[0].firstName", equalTo("Emily"));
}

@Test
public void getUsersWithFilterTest() {
    given()
        .queryParam("select", "firstName,lastName,email")
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .body("users[0].firstName", notNullValue())
        .body("users[0].lastName", notNullValue())
        .body("users[0].email", notNullValue())
        .body("users[0].phone", nullValue());
}

@Test
public void addUserTest() {
    given()
        .contentType("application/json")
        .body("{\"firstName\": \"Ahmet\", \"lastName\": \"Yilmaz\", \"age\": 30}")
    .when()
        .post("/users/add")
    .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("firstName", equalTo("Ahmet"))
        .body("lastName", equalTo("Yilmaz"))
        .body("age", equalTo(30));
}

@Test
public void updateUserTest() {
    given()
        .contentType("application/json")
        .body("{\"firstName\": \"Mehmet\"}")
    .when()
        .put("/users/1")
    .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("firstName", equalTo("Mehmet"));
}
    
}
