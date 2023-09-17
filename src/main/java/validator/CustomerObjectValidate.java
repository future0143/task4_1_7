package validator;

import validator.matcher.DateMatchers;
import model.Customer;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;

public class CustomerObjectValidate {
    public static void validateFields(Customer requestBody, Customer responseBody, String phoneNumber, LocalDateTime nowTime) {
        Assertions.assertEquals(responseBody.getId(), requestBody.getId());
        Assertions.assertEquals(responseBody.getFirstName(), requestBody.getFirstName());
        Assertions.assertEquals(responseBody.getLastName(), requestBody.getLastName());
        Assertions.assertEquals(responseBody.getPhoneNumber(), phoneNumber);
        Assertions.assertEquals(responseBody.getEmail(), requestBody.getEmail());
        Assertions.assertEquals(responseBody.getDateOfBirth(), requestBody.getDateOfBirth());
        Assertions.assertNotEquals(responseBody.getLoyalty().getBonusCardNumber(), requestBody.getLoyalty().getBonusCardNumber());
        Assertions.assertNotEquals(responseBody.getLoyalty().getStatus(), requestBody.getLoyalty().getStatus());
        Assertions.assertNotEquals(responseBody.getLoyalty().getDiscountRate(), requestBody.getLoyalty().getDiscountRate());
        Assertions.assertNull(responseBody.getShopCode());
        assertThat(responseBody.getUpdatedAt(), DateMatchers.isAfter(nowTime));
        assertThat(responseBody.getCreatedAt(), DateMatchers.isAfter(nowTime));
    }
}
