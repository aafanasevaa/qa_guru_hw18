package aafanasyevaa;

import com.codeborne.selenide.Condition;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;


public class DemoWebshopTest {

    @BeforeAll
    static void configureUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    @Tag("API")
    public void clearCompareListTest() {

        step("Add cookie to browser", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", "nastyxa0111@yandex.ru")
                            .formParam("Password", "Nastya")
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            open("/Themes/DefaultClean/Content/images/logo.png");

            getWebDriver().manage().addCookie(
                    new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        });

        step("Add camera to compare list", () ->
                given()
                        .cookie("NOPCOMMERCE.AUTH")
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .when()
                        .get("/compareproducts/add/17")
                        .then()
                        .statusCode(200))
                .extract().response();

        step("Clear compare list", () ->
                given()
                        .when()
                        .get("/clearcomparelist")
                        .then()
                        .statusCode(200));

        step("Check that compare list is empty", () -> {
            open("/compareproducts");
            $(".page-body").shouldHave(Condition.text("You have no items to compare."));
        });
    }
}

