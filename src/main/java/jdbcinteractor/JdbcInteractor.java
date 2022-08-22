package jdbcinteractor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.h2.tools.Server;
import org.pmw.tinylog.Logger;

import java.sql.SQLException;

public class JdbcInteractor extends AbstractVerticle {
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";

    private Server dbServer;
    private Server webDB;


    private void initializeDB() {
        final JDBCClient jdbcClient = JDBCClient.createNonShared(vertx, new JsonObject()
                .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                // gebruik van H2 DB, deze driver is hiervoor nodig
                .put("driverClassName", "org.h2.Driver")
                // db wordt opgeslagen in je eigen root
                .put("jdbcUrl", "jdbc:h2:~/tetris")
                // standaard user
                .put("username", "elementris"));
        final String sqlString = CREATE_TABLE_IF_NOT_EXISTS
                + "users(id INT NOT NULL AUTO_INCREMENT UNIQUE,"
                + "username VARCHAR(255) NOT NULL UNIQUE, userver VARCHAR(255)"
                + " NOT NULL, wins int, email VARCHAR(255) NOT NULL);"
                + CREATE_TABLE_IF_NOT_EXISTS
                + "leaderboard(rank INT PRIMARY KEY,"
                + "username VARCHAR(255), wins int NOT NULL,"
                + "FOREIGN KEY (username) REFERENCES users(username));";
        jdbcClient.getConnection(res -> {
            if (res.succeeded()) {
                final SQLConnection conn = res.result();
                conn.query(sqlString, queryResult -> {
                    if (queryResult.succeeded()) {
                        Logger.info("Database started");
                    } else {
                        Logger.warn(queryResult.cause());
                    }
                });
            }
        });
    }

    private void startDBServer() {
        try {
            // start de DB
            dbServer = Server.createTcpServer().start();
            // start een web interface op poort 8082
            webDB = Server.createWebServer().start();
        } catch (SQLException e) {
            Logger.warn("Error starting the database: {}", e.getLocalizedMessage());
            Logger.debug(e.getStackTrace());
        }
    }

    @Override
    public void start() {
        startDBServer();
        initializeDB();
    }

    @Override
    public void stop() {
        dbServer.stop();
        webDB.stop();
    }
}
