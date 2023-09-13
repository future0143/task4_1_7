package validator;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import matcher.DateMatchers;
import model.Customer;
import org.hamcrest.Matchers;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BodyValidationPositive {

    @Test
    public static void validateFields(String requestBody, Response responseBody, String phoneNumber, LocalDateTime nowTime, int statusCode) throws ParseException {
        responseBody.then().contentType(ContentType.JSON).statusCode(statusCode)
                .body("id", Matchers.notNullValue())
                .body("firstName", Matchers.equalTo(Customer.getCustomer(requestBody).get("firstName")))
                .body("lastName", Matchers.equalTo(Customer.getCustomer(requestBody).get("lastName")))
                .body("phoneNumber", Matchers.equalTo(phoneNumber))
                .body("email", Matchers.equalTo(Customer.getCustomer(requestBody).get("email")))
                .body("dateOfBirth", Matchers.equalTo(Customer.getCustomer(requestBody).get("dateOfBirth")))
                .body("loyalty.bonusCardNumber", Matchers.notNullValue())
                .body("loyalty.status", Matchers.notNullValue())
                .body("loyalty.discountRate", Matchers.notNullValue())
                .body("shopCode", Matchers.nullValue())
                .body("updatedAt", DateMatchers.isToday(nowTime))
                .body("createdAt", DateMatchers.isToday(nowTime));
    }
}
