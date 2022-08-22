package domain.grid.ability.event;

import domain.grid.ability.Ability;
import domain.grid.utils.Position;

public abstract class Event extends Ability {

    private final Position collisionPosition;
    private int eventCode;
    private int amountOfRounds;

    protected Event(int eventCode, Position collisionPosition) {
        this.eventCode = eventCode;
        this.collisionPosition = new Position(collisionPosition.getX(), collisionPosition.getY());
    }

    public int getEventCode() {
        return eventCode;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    protected Position getCollisionPosition() {
        return collisionPosition;
    }

    protected void setAmountOfRounds(int amountOfRounds) {
        this.amountOfRounds = amountOfRounds;
    }
}
