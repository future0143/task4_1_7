package method_call.call_get_byPhoneNumber;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetCustomerByPhoneNumber {

    public static Response getCustomerByPhoneNumber(String phoneNumber) {
        return RestAssured.given().queryParam("phoneNumber", phoneNumber)
                .when().get(EndPoints.GET_CUSTOMERS_BY_PHONE_NUMBER.getPath());
    }
}
