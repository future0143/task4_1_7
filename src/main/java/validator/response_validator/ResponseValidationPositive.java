package validator.response_validator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import model.Customer;
import model.Loyalty;
import org.junit.jupiter.api.Assertions;
import validator.matcher.DateMatchers;
import config.SerializingCustomer;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseValidationPositive {

    public static void validateFieldsFromResponse(String requestBody, Response responseBody, int statusCode, LocalDateTime nowTime, String phoneNumber) {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_positive_schema.json"));

        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);
        Customer responseCustomer = SerializingCustomer.getCustomer(responseBody);

        validateFields(requestCustomer, responseCustomer, nowTime, phoneNumber);
    }

    public static void validateFieldsFromCustomer(String requestBody, Response responseBody, Customer responseCustomer, int statusCode, LocalDateTime nowTime, String phoneNumber) {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_positive_schema.json"));

        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);

        validateFields(requestCustomer, responseCustomer, nowTime, phoneNumber);
    }

    public static void validateFields(Customer requestCustomer, Customer responseCustomer, LocalDateTime nowTime, String phoneNumber) {
        Assertions.assertNotNull(responseCustomer.getId());
        Assertions.assertEquals(responseCustomer.getFirstName(), requestCustomer.getFirstName());
        Assertions.assertEquals(responseCustomer.getLastName(), requestCustomer.getLastName());
        Assertions.assertEquals(responseCustomer.getPhoneNumber(), phoneNumber);
        Assertions.assertEquals(responseCustomer.getEmail(), requestCustomer.getEmail());
        Assertions.assertEquals(responseCustomer.getDateOfBirth(), requestCustomer.getDateOfBirth());
        Assertions.assertNotNull(responseCustomer.getLoyalty().getBonusCardNumber());
        Assertions.assertNotNull(responseCustomer.getLoyalty().getStatus());
        Assertions.assertNotNull(responseCustomer.getLoyalty().getDiscountRate());
        Assertions.assertNull(responseCustomer.getShopCode());
        assertThat(responseCustomer.getUpdatedAt(), DateMatchers.isAfter(nowTime));
        assertThat(responseCustomer.getCreatedAt(), DateMatchers.isAfter(nowTime));
    }

    public static void validateLoyalty(Loyalty loyalty) {
        assertNotNull(loyalty.getBonusCardNumber());
        assertNotNull(loyalty.getStatus());
        assertNotNull(loyalty.getDiscountRate());
    }
}