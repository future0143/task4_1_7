package validator.database_validator;

import config.SerializingCustomer;
import model.Customer;
import model.Loyalty;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseValidation {

    public static void validateCustomerAsMap(String requestBody, Map<String, Object> customerData, String phoneNumber) {
        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);

        assertNotNull(customerData.get("id"));
        assertEquals(customerData.get("first_name"), requestCustomer.getFirstName());
        assertEquals(customerData.get("last_name"), requestCustomer.getLastName());
        assertEquals(customerData.get("phone_number"), phoneNumber);
        assertEquals(customerData.get("email"), requestCustomer.getEmail());
        Object dateOfBirthObject = customerData.get("date_of_birth");
        if (dateOfBirthObject != null) {
            String dateOfBirthString = dateOfBirthObject.toString();
            assertEquals(dateOfBirthString, requestCustomer.getDateOfBirth());
        } else {
            assertNull(requestCustomer.getDateOfBirth());
        }
        assertNull(customerData.get("shop_code"));
        assertNotNull(customerData.get("updated_at"));
        assertNotNull(customerData.get("created_at"));
    }

    public static void validateLoyaltyAsMap(Map<String, Object> loyaltyData) {
        assertNotNull(loyaltyData.get("bonus_card_number"));
        assertNotNull(loyaltyData.get("discount_rate"));
        assertNotNull(loyaltyData.get("status"));
    }

    public static void validateTableIsEmpty(int countLines) {
        assertTrue(countLines == 0);
    }

    public static void checkThatTableNotHaveExtraColumn(String nameAddingColumn) {
        try {
            Customer.class.getDeclaredField("nameAddingColumn");
            fail("Поле " + nameAddingColumn + "найдено, хотя не должно было.");
        } catch (NoSuchFieldException e) {
            e.getStackTrace();
        }
    }

//    public static void checkCustomersFromTableEqualsCreatedCustomers(List<Customer> allCreatedCustomers, List<Map<String, Object>> listFromDb) {
//
//    }
}