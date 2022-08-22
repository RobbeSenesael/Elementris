package domain.grid.test;

import domain.grid.Field;
import domain.grid.ability.hero.Hero;
import domain.verification.User;

public class TestField extends Field {
    public TestField(User user) {
        super(user);
    }

    public TestField(User user, Hero hero) {
        super(user, hero);
    }

    public void oneRow(int c) {
        for (int i = 0; i < this.getXAxis() - 1; i++) {
            this.grid[this.getYAxis() - 1][i] = c;
        }
    }

    public void twoRow(int c) {
        for (int i = 0; i < this.getXAxis() - 1; i++) {
            this.grid[this.getYAxis() - 1][i] = c;
            this.grid[this.getYAxis() - 2][i] = c;
        }
    }

    public void twoRowGrowth(int c) {
        for (int i = 0; i < this.getXAxis() - 1; i++) {
            this.grid[this.getYAxis() - 1][i] = c;
            if (i < this.getXAxis() - 2) {
                this.grid[this.getYAxis() - 2][i] = c;
            }
        }
    }
}
