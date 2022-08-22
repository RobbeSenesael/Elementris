package domain.grid.utils;

import domain.grid.Field;

import java.util.Arrays;

public class PointCalculator {

    public int calculateNewScore(Field field) {
        int rowsCleared = 0;
        int y;
        int[][] newGrid = new int[field.getYAxis()][field.getXAxis()];
        for (y = field.getYAxis() - 1; y >= 0; y--) {
            if (isFullRow(y, field)) {
                for (int r = field.getYAxis() - 1; r >= 0; r--) {
                    if (r == 0) {
                        newGrid[r] = getEmptyRow(field);
                    } else {
                        newGrid[r] = field.getGrid()[r - 1];
                    }
                }
                field.setGrid(newGrid);
                y++;
                rowsCleared++;
            }
        }
        return field.getScore() + rowCalculationFormula(rowsCleared);
    }

    private int[] getEmptyRow(Field field) {
        int[] row = new int[field.getXAxis()];
        Arrays.fill(row, 0);
        return row;
    }

    private int rowCalculationFormula(int rowsCleared) {
        final int pointsPerRow = 5;
        int bonusPoints = 0;
        boolean moreThanOneRowCleared = rowsCleared >= 2;
        if (moreThanOneRowCleared) {
            bonusPoints = (int) Math.floor(rowsCleared * 2.5);
        }
        return pointsPerRow * rowsCleared + bonusPoints;
    }

    private boolean isFullRow(int y, Field field) {
        for (int x = 0; x < field.getGrid()[y].length; x++) {
            if (field.getGrid()[y][x] == 0) {
                return false;
            }
        }
        return true;
    }
}
