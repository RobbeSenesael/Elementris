package vertx.messagehandler;

import domain.Game;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import utils.ElementrisException;

class MessageHandler extends AbstractVerticle {
    private Game game = Game.getInstance();

    Game getGame() {
        return game;
    }

    void throwNullMessageException() {
        throw new ElementrisException("The message was null");
    }

    void replyWithJson(Message message) {
        message.reply(game.toJson().encode());
    }
}
