package test.base_tests;

import config.TestProperties;
import db.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.sql2o.Sql2o;

public class BaseTestCleanTable {

    private static final Sql2o sql2o = new Sql2o(TestProperties.getValue("db.url"),
            TestProperties.getValue("db.username"),
            TestProperties.getValue("db.password"));
    @BeforeEach
    public void truncateTable() {
        DatabaseManager.truncateTable("Customer");
       // DatabaseManager.truncateTable("Loyalty");
    }
}