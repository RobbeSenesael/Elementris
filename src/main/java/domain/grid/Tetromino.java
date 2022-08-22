package domain.grid;

import data.Repositories;

import java.util.Map;
import java.util.List;

public class Tetromino extends Roster {
    private String color;
    private int colorCode;

    public Tetromino() {
        this.colorCode = (int) (1 + Math.floor(Math.random() * 4));
        final List<int[][]> shapes = Repositories.getInstance().getHardcodedRepository().getShapes();
        this.grid = shapes.get((int) Math.floor(Math.random() * shapes.size()));
        addColorToShape(this.colorCode);
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColor(int colorCode) {
        final Map<String, String> elements = Repositories.getInstance().getHardcodedRepository().getElements();
        this.colorCode = colorCode;
        this.color = elements.get(String.valueOf(colorCode));
        addColorToShape(colorCode);
    }

    ///////////////////////////////
    // TETROMINO ROTATION METHOD //
    ///////////////////////////////

    public void rotate() {
        final int n = this.getYAxis();
        final int m = this.getXAxis();
        final int[][] res = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[j][n - 1 - i] = this.grid[i][j];
            }
        }
        this.grid = res;
    }

    ////////////////////////////////////
    // TETROMINO CLASS HELPER METHODS //
    ////////////////////////////////////

    private void addColorToShape(int c) {
        for (int[] row : grid) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] != 0) {
                    row[i] = c;
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - " + color;
    }
}
