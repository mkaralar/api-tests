package com.qatest.products;
import com.qatest.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ProductTest extends BaseTest {

    @Test 
    public void getProductsTest(){
        Response response = RestAssured
            .given()
            .when()
                .get("/products/" + props.getProperty("productId"))
            .then()
                .extract().response();

                assertEquals(200, response.getStatusCode());
                assertEquals("Essence Mascara Lash Princess", response.jsonPath().getString("title"));
                assertEquals(9.99, response.jsonPath().getDouble("price"),0.01);
                assertEquals(99, response.jsonPath().getInt("stock"));
                assertEquals(15.14, response.jsonPath().getDouble("dimensions.width"), 0.01);
                assertEquals("beauty", response.jsonPath().getString("tags[0]"));
                assertEquals("Eleanor Collins", response.jsonPath().getString("reviews[0].reviewerName"));
    }
    
    @Test
    public void getProductsWithLimitTest(){
        Response response = RestAssured
            .given()
                .queryParam("limit", 5)
                .queryParam("skip", 0)
            .when()
                .get("/products")
            .then()
                .extract().response();

                assertEquals(200, response.getStatusCode());
                assertEquals(5, response.jsonPath().getInt("limit"));
                assertEquals(5, response.jsonPath().getList("products").size());
    }

    @Test
    public void searchProductTest(){
        Response response = RestAssured
            .given()
                .queryParam("q", props.getProperty("searchQuery"))
            .when()
                .get("/products/search")
            .then()
                .extract().response();

            assertEquals(200, response.getStatusCode());
    }

    @Test
    public void deleteProductTest(){
        Response response = RestAssured
            .given()
            .when()
                .delete("/products/" + props.getProperty("productId"))
            .then()
                .extract().response();

            assertEquals(200, response.getStatusCode());
            assertTrue(response.jsonPath().getBoolean("isDeleted"));
            assertEquals(1, response.jsonPath().getInt("id"));
    }

    @Test
    public void patchProductTest(){
        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
                .body("{\"price\": 99.99}")
            .when()
                .patch("/products/" + props.getProperty("productId"))
            .then()
                .extract().response();

            assertEquals(200, response.getStatusCode());
            assertEquals(99.99, response.jsonPath().getDouble("price"), 0.01);
            assertEquals("Essence Mascara Lash Princess", response.jsonPath().getString("title"));
    }
}