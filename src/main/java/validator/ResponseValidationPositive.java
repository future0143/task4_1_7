package validator;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import validator.matcher.DateMatchers;
import config.SerializingCustomer;
import org.hamcrest.Matchers;

import java.time.LocalDateTime;

public class ResponseValidationPositive {

    public static void validateFields(String requestBody, Response responseBody, int statusCode, LocalDateTime nowTime, String phoneNumber, String... expectedFields) {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_positive_schema.json"));


        for (String field : expectedFields) {
            switch (field) {
                case "firstName", "lastName" -> responseBody.then().body(field, Matchers.equalTo(expectedFields[0]));

                case "phoneNumber" -> responseBody.then().body("phoneNumber", Matchers.equalTo(phoneNumber));

                case "id" ->
                        responseBody.then().body(field, Matchers.equalTo(SerializingCustomer.getCustomerFromRequestBody(requestBody).getId()));
                case "email" ->
                        responseBody.then().body(field, Matchers.equalTo(SerializingCustomer.getCustomerFromRequestBody(requestBody).getEmail()));
                case "dateOfBirth" ->
                        responseBody.then().body(field, Matchers.equalTo(SerializingCustomer.getCustomerFromRequestBody(requestBody).getDateOfBirth()));
                case "loyalty.status", "loyalty.bonusCardNumber", "loyalty.discountRate" ->
                        responseBody.then().body(field, Matchers.notNullValue());
                case "shopCode" -> responseBody.then().body(field, Matchers.nullValue());
                case "updatedAt", "createdAt" -> responseBody.then().body(field, DateMatchers.isAfter(nowTime));
            }
        }
    }
}
