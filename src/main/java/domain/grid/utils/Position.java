package domain.grid.utils;

import io.vertx.core.json.JsonObject;
import utils.StringLiterals;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + StringLiterals.COMMA + y;
    }

    public JsonObject toJson() {
        final StringBuilder res = new StringBuilder();
        res.append('[').append(this.x).append(StringLiterals.COMMA).append(this.y).append(']');
        final JsonObject jObject = new JsonObject();
        jObject.put("position", res.toString());
        return jObject;
    }
}
