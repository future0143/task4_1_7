package db;

import config.TestProperties;
import model.Customer;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static validator.database_validator.DatabaseValidation.validateTableIsEmpty;

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
             Query query = connection.createQuery("SELECT c.id, c.first_name, c.last_name, c.phone_number, c.email, " +
                             "c.date_of_birth, c.shop_code, l.bonus_card_number, l.status, l.discount_rate FROM Customer c " +
                             "JOIN Loyalty l ON c.loyalty_bonus_card_number = l.bonus_card_number WHERE c.phone_number = :phone_number")
                     .addParameter("phone_number", phoneNumber)
                     .setAutoDeriveColumnNames(true))
        {
            return query.executeAndFetchFirst(Customer.class);
        }
    }

    public static List<Map<String, Object>> selectAllCustomerAsList() {
        try (Connection connection = sql2o.open();
             Query query = connection.createQuery("SELECT * FROM Customer")) {
            List<Map<String, Object>> result = query.executeAndFetchTable().asList();
            if (result.isEmpty()) {
                return new ArrayList<>();
            } else {
                return result;
            }
        }
    }

    public static void truncateTable(String nameTable) {
        try (Connection connection = sql2o.open()) {
            String sql = "DELETE FROM " + nameTable;
            Query query = connection.createQuery(sql);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(count);
    }
}