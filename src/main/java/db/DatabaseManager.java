package db;

import config.TestProperties;
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

    public static Map<String, Object> selectCustomer(String phoneNumber) {
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
}
