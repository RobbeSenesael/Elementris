package domain.grid.ability.hero;

import domain.grid.Field;
import domain.grid.ability.power.Power;

public class Hero {
    private String name;
    private Power power;

    public Hero(String name, Power power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int[][] usePower(Field field) {
        return power.initalizeAbility(field.getGrid());
    }


}
