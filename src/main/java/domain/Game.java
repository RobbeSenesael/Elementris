package domain;

import domain.grid.Field;
import domain.grid.ability.hero.Hero;
import domain.verification.User;
import io.vertx.core.json.JsonObject;
import utils.ElementrisException;

import java.util.HashMap;
import java.util.Map;

public final class Game {
    private static final String KEY_PLAYER_1 = "P1";
    private static final String KEY_PLAYER_2 = "P2";
    private static Game instance = new Game();
    private Map<String, Hero> heroes;
    private Map<String, User> users;
    private Map<String, Field> fields;


    private Game() {
        this.users = new HashMap<>();
        this.users.put(KEY_PLAYER_1, null);
        this.users.put(KEY_PLAYER_2, null);
        this.fields = new HashMap<>();
        this.fields.put(KEY_PLAYER_1, null);
        this.fields.put(KEY_PLAYER_2, null);
        this.heroes = new HashMap<>();
        this.heroes.put(KEY_PLAYER_1, null);
        this.heroes.put(KEY_PLAYER_2, null);
    }

    public static Game getInstance() {
        return instance;
    }

    public void addHeroes(Hero h1, Hero h2) {
        this.heroes.put(KEY_PLAYER_1, h1);
        this.heroes.put(KEY_PLAYER_2, h2);
    }

    public void addUsers(User u1, User u2) {
        //  if(u1.inDatabase() && u2.inDatabase()){
        this.users.put(KEY_PLAYER_1, u1);
        this.users.put(KEY_PLAYER_2, u2);
        //} else throw new ElementrisException("One or both of these users are not registered yet");
    }

    ///////////////////////
    // GAMELOOP  METHODS //
    ///////////////////////

    public void start() {
        if (isNullUsers()) {
            throw new ElementrisException("Can't start game when one or more user(s) have not logged in yet");
        }
        if (isNullHeroes()) {
            throw new ElementrisException("Can't start game when one or more user(s) don't have a hero selected");
        }

        this.fields.put(KEY_PLAYER_1, new Field(this.users.get(KEY_PLAYER_1), this.heroes.get(KEY_PLAYER_1)));
        this.fields.put(KEY_PLAYER_2, new Field(this.users.get(KEY_PLAYER_2), this.heroes.get(KEY_PLAYER_2)));
    }

    public void drop() {
        dropTetrominoInField(KEY_PLAYER_1);
        dropTetrominoInField(KEY_PLAYER_2);
    }

    private void gameOver() {
        if (this.fields.get(KEY_PLAYER_1).isAlive()) {
            this.fields.get(KEY_PLAYER_1).getUser().addWin();
        }
        if (this.fields.get(KEY_PLAYER_2).isAlive()) {
            this.fields.get(KEY_PLAYER_2).getUser().addWin();
        }
    }

    ///////////////////////////////
    // USER CONTROLLABLE METHODS //
    ///////////////////////////////

    public void dropTetrominoInField(String keyPlayer) {
        final Field field = this.fields.get(keyPlayer);
        if (!field.isAlive()) {
            gameOver();
        } else {
            field.dropTetromino();
            this.fields.put(keyPlayer, field);
        }
    }

    public void moveTetrominoInField(String keyPlayer, int direction) {
        final Field field = this.fields.get(keyPlayer);
        field.moveTetromino(direction);
        this.fields.put(keyPlayer, field);
    }

    public void rotateTetrominoInField(String keyPlayer) {
        final Field field = this.fields.get(keyPlayer);
        field.rotateTetromino();
        this.fields.put(keyPlayer, field);
    }

    public void usePowerInField(String keyPlayer) {
        final Field field = this.fields.get(keyPlayer);
        field.usePower();
        this.fields.put(keyPlayer, field);
    }

    ///////////////////////////////
    // GAME CLASS HELPER METHODS //
    ///////////////////////////////

    private boolean isNullUsers() {
        return this.users.get(KEY_PLAYER_1) == null || this.users.get(KEY_PLAYER_2) == null;
    }

    private boolean isNullHeroes() {
        return this.heroes.get(KEY_PLAYER_1) == null || this.heroes.get(KEY_PLAYER_2) == null;
    }

    ////////////////////////////////
    // GAME CLASS DATA TRANSLATOR //
    ////////////////////////////////

    public JsonObject toJson() {
        final JsonObject jObject = new JsonObject();
        jObject.put(KEY_PLAYER_1, this.fields.get(KEY_PLAYER_1).toJson().encode());
        jObject.put(KEY_PLAYER_2, this.fields.get(KEY_PLAYER_2).toJson().encode());
        return jObject;
    }
}
