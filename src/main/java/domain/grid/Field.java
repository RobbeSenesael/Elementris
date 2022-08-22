package domain.grid;

import domain.grid.ability.event.Event;
import domain.grid.ability.hero.Hero;
import domain.grid.utils.CollisionDetector;
import domain.grid.utils.EventDetector;
import domain.grid.utils.PointCalculator;
import domain.grid.utils.Position;
import domain.verification.User;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class Field extends Roster {
    private static final String FIELD_GRID = "grid";
    private static final String FIELD_POS = "position";
    private static final String FIELD_TETROMINO = "tetromino";
    private static final String FIELD_SCORE = "score";
    private static final String FIELD_EVENT = "event";
    private static final String FIELD_HERO = "hero";
    private static final String FIELD_USER = "user";
    private static final String FIELD_ALIVE = "alive";
    private static final int FIELD_DIMENSION = 15;

    private User user;
    private Tetromino tetromino;
    private Position pos;
    private int score;
    private int eventCode;
    private Event event;
    private Hero hero;
    private boolean alive;
    private final PointCalculator pc = new PointCalculator();
    private final CollisionDetector cd = new CollisionDetector();
    private final EventDetector ed = new EventDetector();

    public Field(User user) {
        this.grid = new int[FIELD_DIMENSION][FIELD_DIMENSION];
        this.user = user;
        this.pos = new Position();

        for (int[] row : this.grid) {
            Arrays.fill(row, 0);
        }

        this.event = null;
        this.eventCode = 0;

        this.score = 0;
        this.alive = true;
        setTetromino();
    }

    public Field(User user, Hero hero) {
        this(user);
        this.hero = hero;
    }

    public Tetromino getTetromino() {
        return tetromino;
    }

    public Position getPosition() {
        return pos;
    }

    public int getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }

    public boolean isAlive() {
        return alive;
    }

    public Event getEvent() {
        return event;
    }

    public Hero getHero() {
        return hero;
    }

    public void setGrid(int[]... grid) {
        this.grid = grid;
    }

    public void setPosition(Position position) {
        this.pos = position;
    }

    protected void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
    }

    private void setTetromino() {
        this.tetromino = new Tetromino();
        this.pos.setY(0);
        this.pos.setX((this.grid[this.pos.getY()].length / 2) - (this.tetromino.grid[0].length / 2));
        if (cd.collides(this)) {
            alive = false;
        }
    }

    //////////////////////////////
    // FIELD USER INPUT METHODS //
    //////////////////////////////

    public void dropTetromino() {
        this.pos.setY(this.pos.getY() + 1);
        if (cd.collides(this)) {
            nextRound();
        }
    }

    public void moveTetromino(int direction) {
        this.pos.setX(this.pos.getX() + direction);
        if (cd.collides(this)) {
            this.pos.setX(this.pos.getX() - direction);
        }
    }

    public void rotateTetromino() {
        final boolean isOutOfBounds = this.pos.getX() + this.tetromino.getYAxis() > this.getXAxis()
                || this.pos.getY() + this.tetromino.getXAxis() > this.getYAxis();
        if (!isOutOfBounds) {
            this.tetromino.rotate();
        }
    }

    public void usePower() {
        this.grid = this.hero.usePower(this);
    }

    ////////////////////////////////
    // FIELD CLASS HELPER METHODS //
    ////////////////////////////////

    private void nextRound() {
        this.pos.setY(this.pos.getY() - 1);
        if (event == null) {
            event = ed.activateEvent(this);
        }
        this.merge();
        this.handleEvent();
        this.score = pc.calculateNewScore(this);
        this.setTetromino();
    }

    private void handleEvent() {
        if (event != null && event.getAmountOfRounds() != 0) {
            eventCode = event.getEventCode();
            this.grid = event.initalizeAbility(this.getGrid());
        } else {
            event = null;
            eventCode = 0;
        }
    }

    private void merge() {
        for (int y = 0; y < this.tetromino.grid.length; y++) {
            for (int x = 0; x < this.tetromino.grid[y].length; x++) {
                if (this.tetromino.grid[y][x] != 0) {
                    this.grid[y + this.pos.getY()][x + this.pos.getX()] = this.tetromino.grid[y][x];
                }
            }
        }
    }

    /////////////////////////////////
    // FIELD CLASS DATA TRANSLATOR //
    /////////////////////////////////


    @Override
    public JsonObject toJson() {
        final JsonObject jObject = new JsonObject();
        jObject.put(FIELD_GRID, super.toJson().getValue(FIELD_GRID));
        jObject.put(FIELD_TETROMINO, this.tetromino.toJson().getValue(FIELD_GRID));
        jObject.put(FIELD_POS, this.pos.toJson().getValue(FIELD_POS));
        jObject.put(FIELD_SCORE, this.score);
        jObject.put(FIELD_EVENT, this.eventCode);
        jObject.put(FIELD_HERO, this.hero.getName());
        jObject.put(FIELD_USER, this.user.getUsername());
        jObject.put(FIELD_ALIVE, this.alive);
        return jObject;
    }
}
