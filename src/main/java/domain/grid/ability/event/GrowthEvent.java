package domain.grid.ability.event;

import domain.grid.utils.Position;

public class GrowthEvent extends Event {

    public GrowthEvent(int eventCode, Position collisionPosition) {
        super(eventCode, collisionPosition);
        this.setAmountOfRounds(2);
    }

    private Position getGrowthBlock(int[][] grid, Position collisionPosition) {
        for (int i = collisionPosition.getY(); i >= 0; i--) {
            if (grid[i][collisionPosition.getX()] == 0) {
                return new Position(collisionPosition.getX(), i);
            }
        }
        return null;
    }

    @Override
    public int[][] initalizeAbility(int[][] grid) {
        Position growthBlock = this.getGrowthBlock(grid, this.getCollisionPosition());
        if (growthBlock != null) {
            grid[growthBlock.getY()][growthBlock.getX()] = 6;
        }
        this.setAmountOfRounds(this.getAmountOfRounds() - 1);
        return grid;
    }
}
