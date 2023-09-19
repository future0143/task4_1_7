package validator.database_validator;

import config.SerializingCustomer;
import model.Customer;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDateTime;
import java.util.Map;

public class DatabaseValidation {

    public static void validateTableCustomer(String requestBody, Map<String, Object> customerData, String phoneNumber) {
        Customer requestCustomer = SerializingCustomer.getCustomerFromRequestBody(requestBody);

        Assertions.assertNotNull(customerData.get("id"));
        Assertions.assertEquals(customerData.get("first_name"), requestCustomer.getFirstName());
        Assertions.assertEquals(customerData.get("last_name"), requestCustomer.getLastName());
        Assertions.assertEquals(customerData.get("phone_number"), phoneNumber);
        Assertions.assertEquals(customerData.get("email"), requestCustomer.getEmail());
        Object dateOfBirthObject = customerData.get("date_of_birth");
        if (dateOfBirthObject != null) {
            String dateOfBirthString = dateOfBirthObject.toString();
            Assertions.assertEquals(dateOfBirthString, requestCustomer.getDateOfBirth());
        } else {
            Assertions.assertNull(requestCustomer.getDateOfBirth());
        }
        Assertions.assertNull(customerData.get("shop_code"));
        Assertions.assertNotNull(customerData.get("updated_at"));
        Assertions.assertNotNull(customerData.get("created_at"));
    }

    public static void validateTableLoyalty(Map<String, Object> customerData) {
        Assertions.assertNotNull(customerData.get("bonus_card_number"));
        Assertions.assertNotNull(customerData.get("discount_rate"));
        Assertions.assertNotNull(customerData.get("status"));
    }
}
