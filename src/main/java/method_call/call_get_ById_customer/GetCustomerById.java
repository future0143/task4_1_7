package method_call.call_get_ById_customer;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetCustomerById {

    public static Response getCustomerById(int id) {
        Response responseBody = RestAssured.get(EndPoints.GET_CUSTOMERS_BY_ID.getPath(), id);
        return responseBody;
    }
}
