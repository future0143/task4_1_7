package db;

import model.Customer;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.ConnectionDb.getConnection;
import static validator.database_validator.DatabaseValidation.validateTableIsEmpty;

public class DatabaseManager {

    public static Map<String, Object> selectCustomerAsMap(String phoneNumber) {
        String select = "SELECT * FROM Customer WHERE Customer.phone_number = :phone_number";

        try (Connection connection = getConnection();
             Query query = connection.createQuery(select)
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
        String select = "SELECT * FROM Loyalty WHERE Loyalty.bonus_card_number = :bonus_card_number";

        try (Connection connection = getConnection();
             Query query = connection.createQuery(select)
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
        String select = "SELECT COUNT(*) FROM Customer";

        try (Connection connection = getConnection()) {
            String query = select;

            return connection.createQuery(query).executeScalar(Integer.class);
        }
    }

    public static Customer selectCustomer(String phoneNumber) {
        String select = "SELECT id, first_name, last_name, phone_number, email, date_of_birth, shop_code, updated_At, created_at," +
                "l.bonus_card_number AS \"loyalty.bonus_card_number\", l.status AS \"loyalty.status\", " +
                "l.discount_rate AS \"loyalty.discount_rate\" FROM customer c JOIN loyalty l " +
                "ON c.loyalty_bonus_card_number = l.bonus_card_number WHERE phone_number = :phone_number";

        try (Connection connection = getConnection();
             Query query = connection.createQuery(select)
                     .addParameter("phone_number", phoneNumber)
                     .setAutoDeriveColumnNames(true)) {
            return query.executeAndFetchFirst(Customer.class);
        }
    }

    public static List<Customer> selectAllCustomerAsList() {
        String select = "SELECT id, first_name, last_name, phone_number, email, date_of_birth, shop_code, updated_At, created_at," +
                "l.bonus_card_number AS \"loyalty.bonus_card_number\", l.status AS \"loyalty.status\", " +
                "l.discount_rate AS \"loyalty.discount_rate\" FROM customer c JOIN loyalty l " +
                "ON c.loyalty_bonus_card_number = l.bonus_card_number";

        try (Connection connection = getConnection();
             Query query = connection.createQuery(select).setAutoDeriveColumnNames(true)) {
            List<Customer> result = query.executeAndFetch(Customer.class);
            if (result.isEmpty()) {
                return new ArrayList<>();
            } else {
                return result;
            }
        }
    }

    public static void truncateTable(String nameTable) {
        String select = "DELETE FROM ";
        String sql = select + nameTable;

        try (Connection connection = getConnection();
             Query query = connection.createQuery(sql)
        ) {
            query.executeUpdate();
        }
        int count = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(count);
    }

    public static void insertShop(String code, String address) {
        String sql = "INSERT INTO Shop (code, address) values (:code, :address)";

        try (Connection connection = getConnection();
             Query query = connection.createQuery(sql)
                     .addParameter("code", code)
                     .addParameter("address", address)
        ) {
            query.executeUpdate();
        }
    }
}