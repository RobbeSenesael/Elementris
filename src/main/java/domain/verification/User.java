package domain.verification;

import data.UserRepository;

import java.util.Objects;

public class User implements Comparable<User> {
    private String username;
    private String password;
    private int wins;
    private String email;

    public User(String username, String password, int wins, String email) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.email = email;
    }

    public User(String username, String password, int wins) {
        this(username, password, wins, "None");
    }

    public User(String username, String password, String email) {
        this(username, password, 0, email);
    }

    public User(String username, String password) {
        this(username, password, 0);
    }

    public User(String username) {
        this(username, "");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    //////////////////
    // USER METHODS //
    //////////////////

    public void addWin() {
        new UserRepository().updateWins(getUsername());
    }

    public boolean inDatabase() {
        for (User registeredUser : new UserRepository().getAll()) {
            if (registeredUser.equals(this)) {
                return true;
            }
        }
        return false;
    }

    ///////////////
    // OVERRIDES //
    ///////////////

    @Override
    public int compareTo(User user) {
        return user.wins - this.wins;
    }

    @Override
    public String toString() {
        return "User met naam: " + getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            final User user = (User) o;
            return
                    Objects.equals(this.getUsername(), user.getUsername())
                            && Objects.equals(this.getPassword(), user.getPassword());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getWins());
    }

    public String getEmail() {
        return email;
    }
}
