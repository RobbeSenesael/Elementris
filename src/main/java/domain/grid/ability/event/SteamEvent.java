package domain.grid.ability.event;

import domain.grid.utils.Position;

public class SteamEvent extends Event {

    public SteamEvent(int eventCode, Position collisionPosition) {
        super(eventCode, collisionPosition);
        this.setAmountOfRounds(1);
    }

    @Override
    public int[][] initalizeAbility(int[][] grid) {
        this.setAmountOfRounds(this.getAmountOfRounds() - 1);
        return grid;
    }
}
