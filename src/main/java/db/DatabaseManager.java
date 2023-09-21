package db;

import config.TestProperties;
import model.Customer;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static final Sql2o sql2o = new Sql2o(TestProperties.getValue("db.url"),
            TestProperties.getValue("db.username"),
            TestProperties.getValue("db.password"));

    public static Map<String, Object> selectCustomerAsMap(String phoneNumber) {
        try (Connection connection = sql2o.open();
             Query query = connection.createQuery("SELECT * FROM Customer WHERE Customer.phone_number = :phone_number")
                     .addParameter("phone_number", phoneNumber)) {
            List<Map<String, Object>> result = query.executeAndFetchTable().asList();
            if (result.isEmpty()) {
                return new HashMap<>();
            } else {
                return result.get(0);
            }
        }
    }
    public static Map<String, Object> selectLoyalty(String bonusCardNumber) {
        try (Connection connection = sql2o.open();
             Query query = connection.createQuery("SELECT * FROM Loyalty WHERE Loyalty.bonus_card_number = :bonus_card_number")
                     .addParameter("bonus_card_number", bonusCardNumber)) {
            List<Map<String, Object>> result = query.executeAndFetchTable().asList();
            if (result.isEmpty()) {
                return new HashMap<>();
            } else {
                return result.get(0);
            }
        }
    }
    public static int selectCountOfLinesInTableCustomer() {
        try (Connection connection = sql2o.open()) {
             String query = "SELECT COUNT(*) FROM Customer";
             return connection.createQuery(query).executeScalar(Integer.class);
        }
    }

    public static Customer selectCustomer (String phoneNumber) {
        try (Connection connection = sql2o.open();
             Query query = connection.createQuery("SELECT first_name, last_name,phone_number,email,date_of_birth," +
                             "shop_code,l.bonus_card_number AS loyalty.bonus_card_number, l.status " +
                             "AS loyalty.status, l.discount_rate AS loyalty.discount_rate FROM customer " +
                             "JOIN loyalty l ON c.loyalty_bonus_card_number = l.bonus_card_number " +
                             "WHERE phone_number = :phone_number")
                     .addParameter("phone_number", phoneNumber)) {
            return query.executeScalar(Customer.class);
        }
    }
}
