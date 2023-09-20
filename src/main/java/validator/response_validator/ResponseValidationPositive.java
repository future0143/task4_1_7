package validator.response_validator;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import model.Customer;
import validator.matcher.DateMatchers;
import config.SerializingCustomer;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseValidationPositive {

    public static void validateFields(String requestBody, Response responseBody, int statusCode, LocalDateTime nowTime, String phoneNumber) {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_positive_schema.json"));

        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);
        Customer responseCustomer = SerializingCustomer.getCustomer(responseBody);

        assertNotNull(responseCustomer.getId());
        assertEquals(responseCustomer.getFirstName(), requestCustomer.getFirstName());
        assertEquals(responseCustomer.getLastName(), requestCustomer.getLastName());
        assertEquals(responseCustomer.getPhoneNumber(), phoneNumber);
        assertEquals(responseCustomer.getEmail(), requestCustomer.getEmail());
        assertEquals(responseCustomer.getDateOfBirth(), requestCustomer.getDateOfBirth());
        assertNotNull(responseCustomer.getLoyalty().getBonusCardNumber());
        assertNotNull(responseCustomer.getLoyalty().getStatus());
        assertNotNull(responseCustomer.getLoyalty().getDiscountRate());
        assertNull(responseCustomer.getShopCode());
        assertThat(responseCustomer.getUpdatedAt(), DateMatchers.isAfter(nowTime));
        assertThat(responseCustomer.getCreatedAt(), DateMatchers.isAfter(nowTime));
    }
}