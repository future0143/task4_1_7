package test.base_tests;

import db.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

public class BaseTestSetupAndCleanup {

    @BeforeEach
    public void truncateTable() {
        DatabaseManager.truncateTable("Customer");
        DatabaseManager.truncateTable("Loyalty");
        DatabaseManager.truncateTable("Shop");
    }

    @BeforeEach
    public void createShop() {
        DatabaseManager.insertShop("1357", "AddressOfShop");
    }
}