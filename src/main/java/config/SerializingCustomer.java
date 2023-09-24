package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import model.Customer;

import java.util.List;

//достает данные из json как объект
public class SerializingCustomer {

    public static String getJson(Customer customer) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest;
        try {
            jsonRequest = objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonRequest;
    }

    public static Customer getCustomer(Response responseBody) {

        return responseBody.then().extract().as(Customer.class);
    }

    public static List<Customer> getAllCustomersAsList(Response responseBody) {
        return responseBody.then().extract().body().jsonPath().getList("", Customer.class);
    }

    public static Customer getCustomerFromRequestBody(String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(requestBody, Customer.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize request body to Customer", e);
        }
    }
}