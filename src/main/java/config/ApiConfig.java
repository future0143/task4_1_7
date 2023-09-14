package config;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class ApiConfig {

    public static void setUp() {
        RestAssured.baseURI = TestProperties.getValue("api.base.url");
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
