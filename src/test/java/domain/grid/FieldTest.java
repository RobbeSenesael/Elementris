package domain.grid;

import domain.grid.ability.event.GrowthEvent;
import domain.grid.ability.event.SteamEvent;
import domain.grid.ability.hero.EarthHero;
import domain.grid.test.TestTetromino;
import domain.grid.test.TestField;
import domain.verification.User;
import org.junit.Before;
import org.junit.Test;
import domain.grid.utils.Position;
import utils.StringLiterals;

import static org.junit.Assert.*;

public class FieldTest {

    private Field field;

    @Before
    public void beforeAnyTest() {
        field = new Field(new User(StringLiterals.TEMP_USERNAME_1));
    }

    @Test
    public void setTetromino() {
        assertTrue(
                field.getTetromino() != null
                        && !field.getPosition().equals(new Position())
        );
    }

    @Test
    public void dropTetromino() {
        final int initialPositionY = field.getPosition().getY();
        field.dropTetromino();
        final int afterDropPositionY = field.getPosition().getY();
        assertEquals(initialPositionY + 1, afterDropPositionY);
    }

    @Test
    public void moveLeftTetromino() {
        final int initialPositionX = field.getPosition().getX();
        field.moveTetromino(-1);
        final int afterLeftMovePositionX = field.getPosition().getX();
        assertEquals(initialPositionX - 1, afterLeftMovePositionX);
    }

    @Test
    public void moveRightTetromino() {
        final int initialPositionX = field.getPosition().getX();
        field.moveTetromino(1);
        final int afterRightMovePositionX = field.getPosition().getX();
        assertEquals(initialPositionX + 1, afterRightMovePositionX);
    }

    @Test
    public void dropOutOfBounds() {
        final int outOfBounds = field.getYAxis() - field.getTetromino().getYAxis() + 1;
        for (int i = 0; i < outOfBounds; i++) {
            field.dropTetromino();
        }
        assertEquals(0, field.getPosition().getY());
    }

    @Test
    public void moveRightOutOfBounds() {
        final int outOfBounds = field.getXAxis() - field.getTetromino().getXAxis() + 1;
        for (int i = 0; i < outOfBounds; i++) {
            field.moveTetromino(1);
        }
        assertEquals(outOfBounds - 1, field.getPosition().getX());
    }

    @Test
    public void moveLeftOutOfBounds() {
        final int outOfBounds = field.getPosition().getX() + 1;
        for (int i = 0; i < outOfBounds; i++) {
            field.moveTetromino(-1);
        }
        assertEquals(0, field.getPosition().getX());
    }

    @Test
    public void pointCalculatorOneRow() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        ((TestField) this.field).oneRow(5);
        final TestTetromino testTetromino = new TestTetromino();
        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertEquals(5, this.field.getScore());
    }

    @Test
    public void pointCalculatorTwoRows() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        ((TestField) this.field).twoRow(5);
        final TestTetromino testTetromino = new TestTetromino();
        testTetromino.twoBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 2));
        assertEquals(15, this.field.getScore());
    }

    @Test
    public void pointCalculatorTwoCalculations() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        ((TestField) this.field).twoRow(5);
        final TestTetromino testTetromino = new TestTetromino();
        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertEquals(10, this.field.getScore());
    }

    @Test
    public void merge() {
        final int outOfBounds = field.getYAxis() - field.getTetromino().getYAxis() + 1;
        for (int i = 0; i < outOfBounds; i++) {
            field.dropTetromino();
        }
        boolean hasSomethingInIt = false;
        for (int j = 0; j < this.field.getXAxis() - 1; j++) {
            if (this.field.getGrid()[this.field.getYAxis() - 1][j] != 0) {
                hasSomethingInIt = true;
            }
        }
        assertTrue(hasSomethingInIt);
    }

    @Test
    public void eventDetectorSteam() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();

        ((TestField) this.field).oneRow(2);
        testTetromino.oneBlock(1);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertTrue(field.getEvent() instanceof SteamEvent);
    }

    @Test
    public void eventDetectorSteamOtherScenario() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();

        ((TestField) this.field).oneRow(1);
        testTetromino.oneBlock(2);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertTrue(field.getEvent() instanceof SteamEvent);
    }

    @Test
    public void eventDetectorGrowth() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();

        ((TestField) this.field).oneRow(3);
        testTetromino.oneBlock(2);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertTrue(field.getEvent() instanceof GrowthEvent);
    }

    @Test
    public void eventDetectorNull() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();

        ((TestField) this.field).oneRow(5);
        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));
        assertNull(field.getEvent());
    }

    @Test
    public void eventRoundCalculation() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();

        ((TestField) this.field).twoRowGrowth(3);
        testTetromino.oneBlock(2);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));

        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(0, field.getYAxis() - 2));
        assertEquals(0, this.field.getEvent().getAmountOfRounds());
    }

    @Test
    public void eventGrowthCheckOnPosition() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();
        ((TestField) this.field).twoRowGrowth(3);

        testTetromino.oneBlock(2);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 1));

        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(0, field.getXAxis() - 2));

        assertEquals(6, this.field.getGrid()[this.field.getYAxis() - 2][this.field.getXAxis() - 1]);
    }

    @Test
    public void eventGrowthCheckNotOnPosition() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1));
        final TestTetromino testTetromino = new TestTetromino();
        ((TestField) this.field).twoRowGrowth(3);

        testTetromino.twoBlock(2);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(field.getXAxis() - 1, field.getYAxis() - 2));

        testTetromino.oneBlock(5);
        this.setTestTetrominoAndDropIt(testTetromino, new Position(0, field.getXAxis() - 1));

        assertEquals(6, this.field.getGrid()[this.field.getYAxis() - 3][this.field.getXAxis() - 1]);
    }

    @Test
    public void heroPowerEarth() {
        this.field = new TestField(new User(StringLiterals.TEMP_USERNAME_1), new EarthHero());
        ((TestField) this.field).twoRow(5);
        this.field.usePower();

        for (int y = 0; y < this.field.getYAxis(); y++) {
            for (int x = 0; x < this.field.getXAxis(); x++) {
                if (this.field.getGrid()[y][x] != 3 && this.field.getGrid()[y][x] != 0) {
                    fail();
                }
            }
        }
    }

    private void setTestTetrominoAndDropIt(Tetromino tetromino, Position position) {
        this.field.setTetromino(tetromino);
        this.field.setPosition(position);
        this.field.dropTetromino();
    }
}
