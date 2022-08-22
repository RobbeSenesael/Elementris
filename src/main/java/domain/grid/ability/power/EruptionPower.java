package domain.grid.ability.power;

import java.security.SecureRandom;

public class EruptionPower extends Power {

    private int[][] makeThreeRandNums() {
        int[][] nums = new int[15][15];

        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
                nums[i][j] = 0;
            }
        }
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < 3; i++) {
            int randomY = r.nextInt(14);
            int randomX = r.nextInt(14);
            nums[randomY][randomX] = 7;
        }
        return nums;
    }

    @Override
    public int[][] initalizeAbility(int[][] grid) {
        //loop over randomNumbers grid, als er een 7 staat dan plaats je op die plaats in de grid een 7
        final int colorCode = 7;
        int[][] randomNumberGrid = makeThreeRandNums();
        int i;
        for (i = 0; i < randomNumberGrid.length; i++) {
            for (int j = 0; j < randomNumberGrid[i].length; j++) {
                if (randomNumberGrid[i][j] == colorCode) {
                    if (grid[i][j] != 0) {
                        while (grid[i + 1][j] != 0) {
                            i--;
                        }
                        grid[i][j] = 7;
                    } else {
                        grid[i][j] = 0;
                    }
                }
            }
        }

        return grid;
    }
}
