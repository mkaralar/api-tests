package com.qatest.carts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.qatest.base.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CartTest extends BaseTest {

    @Test
    public void getCartsTest() {

        Response response = RestAssured
            .given()
                .header("Content-Type", "application/json")
            .when()
                .get("/carts/1")
            .then()
                .extract().response();

        assertEquals(200, response.getStatusCode());
        assertEquals(4, response.jsonPath().getInt("totalProducts"));
        assertEquals(15, response.jsonPath().getInt("totalQuantity"));
        assertEquals("Charger SXT RWD", response.jsonPath().getString("products[0].title"));
        assertEquals(3, response.jsonPath().getInt("products[0].quantity"));
        assertEquals(33, response.jsonPath().getInt("userId"));

    }

    @Test
    public void getCartProductsTest() {
        given()
            .when()
                .get("/carts/1")
            .then()
                .statusCode(200)
                .body("products", not(empty()))
                .body("products[0].id", notNullValue())
                .body("products[0].title", not(emptyString()))
                .body("products[0].price", greaterThan(0f))
                .body("products[0].quantity", greaterThan(0))
                .body("products[0].total", greaterThan(0f))
                .body("products[0].discountPercentage", greaterThan(0f))
                .body("products[0].discountedTotal", greaterThan(0f));
    }

    @Test
    public void cartTotalConsistencyTest() {
        Response response = given()
            .when()
                .get("/carts/1")
            .then()
                .statusCode(200)
                .extract().response();

        int totalProducts = response.path("totalProducts");
        List<?> products = response.path("products");

        // Kaç ürün geldi gerçekten?
        assert products.size() == totalProducts
            : "Mismatch! totalProducts=" + totalProducts 
              + " but products.size()=" + products.size();
    }

    @Test
    public void getCartNotFoundTest() {
         Response response = RestAssured
        .given()
            .header("Content-Type", "application/json")
        .when()
            .get("/carts/99999")
        .then()
            .extract().response();

    assertEquals(404, response.getStatusCode());
}

    /*
    @Test
public void getCartProductsTest() {
    Response response = RestAssured
        .given()
            .header("Content-Type", "application/json")
        .when()
            .get("/carts/1")
        .then()
            .extract().response();

    assertEquals(200, response.getStatusCode());
    // products array boş değil
    assertEquals(false, response.jsonPath().getList("products").isEmpty());
    // ilk ürün field'ları
    assertEquals("Charger SXT RWD", response.jsonPath().getString("products[0].title"));
    assertEquals(3, response.jsonPath().getInt("products[0].quantity"));
}

@Test
public void cartTotalConsistencyTest() {
    Response response = RestAssured
        .given()
            .header("Content-Type", "application/json")
        .when()
            .get("/carts/1")
        .then()
            .extract().response();

    int totalProducts = response.jsonPath().getInt("totalProducts");
    int actualSize = response.jsonPath().getList("products").size();

    assertEquals(totalProducts, actualSize, 
        "totalProducts=" + totalProducts + " ama products.size()=" + actualSize);
}
 */

}
