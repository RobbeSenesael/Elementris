package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.ElementrisException;

public final class H2Connection {
    private static final String URL = "jdbc:h2:~/tetris";
    private static final String USER = "elementris";
    private static final String PWD = null;

    private H2Connection() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (SQLException ex) {
            throw new ElementrisException("Cannot connect to db", ex);
        }
    }

}
