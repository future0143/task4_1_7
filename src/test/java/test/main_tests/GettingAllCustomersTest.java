package test.main_tests;

import config.ApiConfigSetup;
import config.DataProvider;
import jdk.jfr.Description;
import model.Customer;
import org.junit.jupiter.api.*;
import test.base_tests.BaseTestSetupAndCleanup;

import java.util.ArrayList;
import java.util.List;

import static config.SerializingCustomer.getAllCustomersAsList;
import static config.SerializingCustomer.getCustomer;
import static db.DatabaseManager.selectAllCustomerAsList;
import static method_call.CustomersRequestHandler.*;
import static utils.PhoneUtils.getPhoneNumber;

public class GettingAllCustomersTest extends BaseTestSetupAndCleanup implements ApiConfigSetup {


    @Test  //запрос не возвращает список из всех добавленных клиентов, возвращает меньшее количество
    @DisplayName("Получение списка из нескольких клиентов")
    @Description("Создание нескольких клиентов и получение их в качестве списка")
    public void getListFromSeveralCustomers() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");

        Customer customer1 = getCustomer(createCustomerResponse(requestBody, getPhoneNumber()));
        Customer customer2 = getCustomer(createCustomerResponse(requestBody, getPhoneNumber()));
        Customer customer3 = getCustomer(createCustomerResponse(requestBody, getPhoneNumber()));

        List<Customer> allCustomer = new ArrayList<>();
        allCustomer.add(customer1);
        allCustomer.add(customer2);
        allCustomer.add(customer3);

        List<Customer> allCustomersAsList = getAllCustomersAsList(getAllCustomers());
        Assertions.assertEquals(allCustomer, allCustomersAsList);

        List<Customer> listFromDb = selectAllCustomerAsList();
        Assertions.assertEquals(allCustomer, listFromDb);
    }

    @Test
    @DisplayName("Получение списка из пустой таблицы Customer")
    @Description("Получение списка из пустой таблицы Customer")
    public void getListIfNotCustomers() {
        List<Customer> allCustomersAsList = getAllCustomersAsList(getAllCustomers());
        Assertions.assertTrue(allCustomersAsList.isEmpty());

        List<Customer> listFromDb = selectAllCustomerAsList();
        Assertions.assertTrue(listFromDb.isEmpty());
    }
}