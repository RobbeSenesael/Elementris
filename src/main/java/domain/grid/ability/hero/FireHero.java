package domain.grid.ability.hero;

import domain.grid.ability.power.EruptionPower;

public class FireHero extends Hero {
    private static final String FIRE_HERO_NAME = "Ignitor";

    public FireHero() {
        super(FIRE_HERO_NAME, new EruptionPower());
    }
}
