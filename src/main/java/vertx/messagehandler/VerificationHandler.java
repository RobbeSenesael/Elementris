package vertx.messagehandler;

import data.UserRepository;
import domain.verification.User;
import domain.verification.UserControl;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class VerificationHandler extends MessageHandler {

    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "pass";
    private static final String FIELD_C_PASSWORD = "cpass";
    private static final String FIELD_EMAIL = "email";

    private UserRepository userRepo = new UserRepository();

    @Override
    public void start() {
        final EventBus eb = vertx.eventBus();
        eb.consumer("tetris-27.socket.login", this::doLogin);
        eb.consumer("tetris-27.socket.register", this::doRegister);
        eb.consumer("tetris-27.socket.leaderboard", this::retrieveLeaderboard);
        eb.consumer("tetris-27.socket.wins", this::updateWins);
    }

    private void retrieveLeaderboard(Message message) {
        if (message != null) {
            final JsonObject main = new JsonObject();

            final List<User> users = userRepo.getAll();

            for (int i = 0; i < users.size(); i++) {
                main.put("user" + i, passUsers(i, users));
            }
            message.reply(main);
        }
    }

    private void updateWins(Message message) {
        if (message != null) {
            final JsonObject json = (JsonObject) message.body();
            String name = json.getString(FIELD_USERNAME);
            userRepo.updateWins(name);
        }
    }

    private JsonObject passUsers(int i, List<User> users) {
        final JsonObject user = new JsonObject();
        user.put(FIELD_USERNAME, users.get(i).getUsername());
        user.put("wins", users.get(i).getWins());
        return user;
    }

    private void doRegister(final Message message) {
        if (message != null) {
            final JsonObject json = (JsonObject) message.body();
            message.reply(this.handleRegister(
                    new UserControl(
                            json.getString(FIELD_USERNAME),
                            json.getString(FIELD_PASSWORD),
                            json.getString(FIELD_C_PASSWORD),
                            json.getString(FIELD_EMAIL))
            ));
        } else {
            throwNullMessageException();
        }
    }

    private void doLogin(final Message message) {
        if (message != null) {
            final JsonObject json = (JsonObject) message.body();
            message.reply(this.handleLogin(json.getString(FIELD_USERNAME), json.getString(FIELD_PASSWORD)));
        } else {
            throwNullMessageException();
        }
    }

    private boolean handleLogin(String name, String pass) {
        if (name != null) {
            final User u = userRepo.getUser(name);
            return u.getUsername().equals(name) && BCrypt.checkpw(pass, u.getPassword());
        }
        return false;
    }

    private boolean handleRegister(UserControl user) {
        if (user.passwordsMatch()) {
            userRepo.addUser(
                    user.getUsername(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)),
                    user.getEmail());
        }
        return user.passwordsMatch();
    }

}
