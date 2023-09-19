package validator.response_validator;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import model.Customer;
import org.junit.jupiter.api.Assertions;
import validator.matcher.DateMatchers;
import config.SerializingCustomer;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseValidationPositive {

    public static void validateFields(String requestBody, Response responseBody, int statusCode, LocalDateTime nowTime, String phoneNumber) {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_positive_schema.json"));

        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);
        Customer responseCustomer = SerializingCustomer.getCustomer(responseBody);

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
}
