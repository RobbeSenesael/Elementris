package domain.verification;

import java.util.Collections;
import java.util.List;

public class Leaderboard {
    private List<User> users;

    public Leaderboard(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void sortByWins() {
        Collections.sort(this.users);
    }

    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder();
        for (User user : users) {
            res.append(user.getUsername()).append('-').append(user.getWins()).append('\n');
        }
        return res.toString();
    }
}
