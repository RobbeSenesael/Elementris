package data;

import domain.verification.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.ElementrisException;

public class UserRepository {

    private static final String GET_USER = "SELECT * FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users(username, userver, email, wins) VALUES(?, ?, ?, 0)";
    private static final String GET_ALL_USERS = "SELECT * FROM users ORDER BY wins desc";
    private static final String UPDATE_WINS = "UPDATE users SET wins = wins + 1 WHERE username = ?";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASS = "password";
    private static final String FIELD_WINS = "wins";

    public User getUser(String name) {
        User u = null;
        try (Connection con = H2Connection.getConnection();
             PreparedStatement prep = con.prepareStatement(GET_USER)
        ) {
            prep.setString(1, name);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    u = rs2User(rs);
                }
            }
        } catch (SQLException e) {
            throw new ElementrisException("Can't fetch user from database", e);
        }
        return u;
    }

    public void updateWins(String username) {
        try (Connection con = H2Connection.getConnection();
             PreparedStatement prep = con.prepareStatement(UPDATE_WINS)) {
            prep.setString(1, username);
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new ElementrisException("Can't update wins : ", e);
        }
    }

    private boolean addUser(User user) {
        try {
            if (!user.inDatabase()) {
                try (Connection con = H2Connection.getConnection();
                     PreparedStatement prep = con.prepareStatement(INSERT_USER)
                ) {
                    prep.setString(1, user.getUsername());
                    prep.setString(2, user.getPassword());
                    prep.setString(3, user.getEmail());
                    prep.executeUpdate();
                    return true;
                }
            }
        } catch (ElementrisException | SQLException e) {
            throw new ElementrisException("Can't add user to database", e);
        }
        return false;
    }

    public void addUser(String name, String password, String email) {
        this.addUser(new User(name, password, email));
    }

    public List<User> getAll() {
        final List<User> allUsers = new ArrayList<>();
        try (Connection con = H2Connection.getConnection();
             PreparedStatement prep = con.prepareStatement(GET_ALL_USERS)
        ) {
            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    allUsers.add(rs2User(rs));
                }
            }
        } catch (SQLException ex) {
            throw new ElementrisException("Can't fetch users from database ", ex);
        }
        return allUsers;
    }

    private User rs2User(ResultSet rs) throws SQLException {
        return new User(rs.getString(FIELD_USERNAME), rs.getString(FIELD_PASS), rs.getInt(FIELD_WINS));
    }
}
