import config.DataProvider;
import config.TestProperties;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import model.Customer;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.GeneratorPhoneNumber;
import validator.BodyValidationPositive;
import java.time.LocalDateTime;

public class Creating_Customer_Test {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = TestProperties.getValue("api.base.url");;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Создание клиента только с обязательными полями")
    public void createNewCustomerWithRequiredFields() throws ParseException {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-required-fields.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .when().post("/customers");

        BodyValidationPositive.validateFields(requestBody,responseBody,phoneNumber,nowTime);
    }

    @Test
    @DisplayName("Создание клиента со всеми полями")
    @Description("Создание клиента с заполнением всех полей валидными значениями")
    public void createNewCustomerWithAllFields() throws ParseException {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-all-fields.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .when().post("/customers");

        BodyValidationPositive.validateFields(requestBody,responseBody,phoneNumber,nowTime);
    }

    @Test
    @DisplayName("Создание клиента с неуникальным id")
    @Description("Создание клиента с уже существующим id")
    public void createNewCustomerWithNotUniqueId() throws ParseException {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-all-fields.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        int id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .when().post("/customers").then().extract().path("id");
        System.out.println("извлеченный id: " + id);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .body(Customer.getCustomer(requestBody).replace("id", id))
                .when().post("/customers")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Пустое значение для одного из обязательных полей")
    @Description("Создание пользователя с пустым значением для поля lastName")
    public void createNewCustomerWithEmptyValueOneRequiredField() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-required-fields.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .body(requestBody.replace("Petrov", ""))
                .when().post("/customers")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание пользователя без одного из обязательных полей")
    @Description("Создание пользователя с удаленным полем lastName")
    public void createNewCustomerWithoutOneRequiredField() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-without-one-required-field.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .when().post("/customers")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание пользователя с переставленными местами полями")
    @Description("Создание пользователя с переставленными местами полями lastName и firstName")
    public void createNewCustomerWithSwappedRequiredFields() throws ParseException {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post-request/create-customer-swapped-fields.json");
        String phoneNumber = GeneratorPhoneNumber.getPhoneNumber();

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody.replace("+78905927238", phoneNumber))
                .when().post("/customers");

        BodyValidationPositive.validateFields(requestBody,responseBody,phoneNumber,nowTime);
    }
}
