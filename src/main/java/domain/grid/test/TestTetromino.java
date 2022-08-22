package domain.grid.test;

import domain.grid.Tetromino;

public class TestTetromino extends Tetromino {

    public void oneBlock(int c) {
        this.setColor(c);
        this.grid = new int[1][1];
        this.grid[0][0] = c;
    }

    public void twoBlock(int c) {
        this.setColor(c);
        this.grid = new int[2][1];
        this.grid[0][0] = c;
        this.grid[1][0] = c;
    }
}
