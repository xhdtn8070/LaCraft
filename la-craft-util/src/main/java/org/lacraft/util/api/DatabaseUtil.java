package org.lacraft.util.api;

import org.flywaydb.core.Flyway;

public class DatabaseUtil {
    public static void flywayMigrate(String url, String user, String password, String table) {
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .table(table)
                .load();

        // Start migration
        flyway.migrate();
    }
}
