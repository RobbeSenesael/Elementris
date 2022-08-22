package vertx.messagehandler;

import domain.grid.ability.hero.EarthHero;
import domain.grid.ability.hero.FireHero;
import domain.verification.User;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import utils.StringLiterals;

public class GameLoopHandler extends MessageHandler {


    @Override
    public void start() {
        this.getGame().addUsers(new User(StringLiterals.TEMP_USERNAME_1), new User(StringLiterals.TEMP_USERNAME_2));
        this.getGame().addHeroes(new FireHero(), new EarthHero());
        final EventBus eb = vertx.eventBus();
        eb.consumer("tetris-27.socket.game.start", this::startGame);
        eb.consumer("tetris-27.socket.game", this::naturalDrop);
    }

    private void startGame(Message message) {
        if (message.body() != null) {
            this.getGame().start();
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }

    private void naturalDrop(Message message) {
        if (message.body() != null) {
            this.getGame().drop();
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }
}
