package validator;

import io.restassured.response.Response;
import org.hamcrest.Matchers;

public class ResponseValidationNegativeOneLine {

    public static void validateFields(Response responseBody, String errorMessage, int statusCode) {
        responseBody.then().statusCode(statusCode)
                .body("errors", Matchers.hasItem(errorMessage));
    }
}
