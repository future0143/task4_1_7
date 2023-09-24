package db;

import config.TestProperties;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class ConnectionDb {

    private static Sql2o sql2o;

    private ConnectionDb() {
    }

    public static Connection getConnection() {
        if (sql2o == null) {
            sql2o = new Sql2o(TestProperties.getValue("db.url"),
                    TestProperties.getValue("db.username"),
                    TestProperties.getValue("db.password"));
        }
        return sql2o.open();
    }
}