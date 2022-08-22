package domain.grid.ability.power;

public class SolidifyPower extends Power {

    @Override
    public int[][] initalizeAbility(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    grid[i][j] = 3;
                }
            }
        }
        return grid;
    }
}
