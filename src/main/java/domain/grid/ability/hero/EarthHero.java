package domain.grid.ability.hero;

import domain.grid.ability.power.SolidifyPower;

public class EarthHero extends Hero {
    private static final String EARTH_HERO_NAME = "Rocky";

    public EarthHero() {
        super(EARTH_HERO_NAME, new SolidifyPower());
    }
}
