package vertx.messagehandler;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class UserInputHandler extends MessageHandler {

    private static final String FIELD_FIELDKEY = "keyplayer";
    private static final String FIELD_DIRECTION = "direction";

    @Override
    public void start() {
        final EventBus eb = vertx.eventBus();
        eb.consumer("tetris-27.socket.game.drop", this::drop);
        eb.consumer("tetris-27.socket.game.move", this::move);
        eb.consumer("tetris-27.socket.game.rotate", this::rotate);
        eb.consumer("tetris-27.socket.game.power", this::activatePower);
    }

    private void activatePower(Message message) {
        if (message.body() != null) {
            final JsonObject json = (JsonObject) message.body();
            final String fieldkKey = json.getString(FIELD_FIELDKEY);
            this.getGame().usePowerInField(fieldkKey);
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }

    private void rotate(Message message) {
        if (message.body() != null) {
            final JsonObject json = (JsonObject) message.body();
            final String fieldKey = json.getString(FIELD_FIELDKEY);
            this.getGame().rotateTetrominoInField(fieldKey);
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }

    private void move(Message message) {
        if (message.body() != null) {
            final JsonObject json = (JsonObject) message.body();
            final String fieldKey = json.getString(FIELD_FIELDKEY);
            final int direction = json.getInteger(FIELD_DIRECTION);
            this.getGame().moveTetrominoInField(fieldKey, direction);
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }

    private void drop(Message message) {
        if (message.body() != null) {
            final JsonObject json = (JsonObject) message.body();
            final String fieldKey = json.getString(FIELD_FIELDKEY);
            this.getGame().dropTetrominoInField(fieldKey);
            this.replyWithJson(message);
        } else {
            throwNullMessageException();
        }
    }
}
