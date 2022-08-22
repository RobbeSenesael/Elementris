package domain.grid;

import io.vertx.core.json.JsonObject;
import utils.StringLiterals;
import java.util.Arrays;

public class Roster {

    protected int[][] grid;

    public int[][] getGrid() {
        int[][] newGrid = new int[getYAxis()][getXAxis()];
        for (int y = 0; y < getYAxis(); y++) {
            if (getXAxis() >= 0) {
                System.arraycopy(grid[y], 0, newGrid[y], 0, getXAxis());
            }
        }
        return newGrid;
    }

    public int getYAxis() {
        return this.grid.length;
    }

    public int getXAxis() {
        return this.grid[0].length;
    }

    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder();
        for (int[] row : this.grid) {
            int key = 0;
            for (int square : row) {
                res.append(square);
                if (key != row.length - 1) {
                    res.append(StringLiterals.COMMA);
                }
                key++;
            }
            res.append('\n');
        }
        res.append(this.toStringAxis());
        return res.toString();
    }

    public JsonObject toJson() {
        final StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; i < this.grid.length; i++) {
            res.append(Arrays.toString(this.grid[i]));
            if (i != this.grid.length - 1) {
                res.append(StringLiterals.COMMA);
            } else {
                res.append(']');
            }
        }
        final JsonObject jObject = new JsonObject();
        jObject.put("grid", res.toString());
        return jObject;
    }

    private String toStringAxis() {
        return getYAxis() + "x" + getXAxis();
    }
}
