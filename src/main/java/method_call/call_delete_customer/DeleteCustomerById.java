package method_call.call_delete_customer;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DeleteCustomerById {

    public static Response deleteCustomer(int id) {
        return RestAssured.delete(EndPoints.DELETE_CUSTOMERS.getPath(), id);
    }
}
