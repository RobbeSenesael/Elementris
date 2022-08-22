package domain.grid.utils;

import domain.grid.Field;

public class CollisionDetector {

    private Field parentField;

    public boolean collides(Field field) {
        this.parentField = field;
        for (int y = 0; y < parentField.getTetromino().getYAxis(); y++) {
            for (int x = 0; x < parentField.getTetromino().getXAxis(); x++) {
                if (collidesWithBorders(x, y) || collidesWithBlockInField(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean collidesWithBorders(int x, int y) {
        return
                x + parentField.getPosition().getX() == parentField.getXAxis()
                        || y + parentField.getPosition().getY() == parentField.getYAxis()
                        || parentField.getPosition().getX() < 0;
    }

    private boolean collidesWithBlockInField(int x, int y) {
        final boolean isTetrominoSpaceEmpty = parentField.getTetromino().getGrid()[y][x] == 0;
        final int newYPosition = y + parentField.getPosition().getY();
        final int newXPosition = x + parentField.getPosition().getX();
        final boolean isFieldSpaceEmpty = parentField.getGrid()[newYPosition][newXPosition] == 0;
        return (!isTetrominoSpaceEmpty) && (!isFieldSpaceEmpty);
    }
}
