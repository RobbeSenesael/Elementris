package domain.grid.utils;

import domain.grid.Field;
import domain.grid.ability.event.*;

import java.util.ArrayList;
import java.util.List;

public class EventDetector {
    private Field parentField;

    ///////////////////////////////////////
    // EVENT DETECTOR/DETERMINER METHODS //
    ///////////////////////////////////////


    public Event activateEvent(Field field) {
        this.parentField = field;
        final int tetrominoColorCode = parentField.getTetromino().getColorCode();
        final int firstNeighbourColorCode = determineFirstNeighbourColorCode();
        return this.determineEvent(tetrominoColorCode, firstNeighbourColorCode, parentField.getPosition());
    }

    private Event determineEvent(int collider, int collided, Position collisionPosition) {
        Event newEvent;
        final int code = createCode(collider, collided);
        switch (code) {
            case 11:
                newEvent = new SteamEvent(code, collisionPosition);
                break;
            case 21:
                newEvent = new GrowthEvent(code, collisionPosition);
                break;
            default:
                newEvent = null;
                break;
        }
        return newEvent;
    }

    ////////////////////////////////////////
    // FIRST NEIGHBOUR DETERMINER METHODS //
    ////////////////////////////////////////

    private int determineFirstNeighbourColorCode() {
        List<Position> neighbourPositions = getNeighbourPositions();
        for (Integer neighbour : getNeighbours(neighbourPositions)) {
            if (neighbour != 0) {
                return neighbour;
            }
        }
        return 0;
    }


    private List<Position> getNeighbourPositions() {
        final List<Position> nPositions = new ArrayList<>();
        for (int y = 0; y < parentField.getTetromino().getGrid().length; y++) {
            for (int x = 0; x < parentField.getTetromino().getGrid()[y].length; x++) {
                nPositions.add(newPosition(
                        x + parentField.getPosition().getX(),
                        y + parentField.getPosition().getY() + 1
                ));
                nPositions.add(newPosition(
                        x + parentField.getPosition().getX() - 1,
                        y + parentField.getPosition().getY()
                ));
                nPositions.add(newPosition(
                        x + parentField.getPosition().getX() + 1,
                        y + parentField.getPosition().getY()
                ));
            }
        }
        return nPositions;
    }

    private Position newPosition(int x, int y) {
        return new Position(x, y);
        // [Performance | AvoidInstantiatingObjectsInLoops]
        //  Avoid instantiating new objects inside loops
    }

    private List<Integer> getNeighbours(List<Position> nPositions) {
        final List<Integer> neighbours = new ArrayList<>();
        for (Position neighbourPos : nPositions) {
            if (!(isOutOfBounds(neighbourPos) || isPartOfTetromino(neighbourPos))) {
                neighbours.add(
                        parentField.getGrid()
                                [neighbourPos.getY()]
                                [neighbourPos.getX()]
                );
            } else {
                neighbours.add(0);
            }
        }
        return neighbours;
    }

    ///////////////////////////////////
    // EVENT DETECTOR HELPER METHODS //
    ///////////////////////////////////

    private int createCode(int collider, int collided) {
        if (isColorCodeNull(collider) || isColorCodeNull(collided)) {
            return 0;
        }
        return Integer.parseInt(Integer.toBinaryString(collider)) + Integer.parseInt(Integer.toBinaryString(collided));
    }

    private boolean isColorCodeNull(int c) {
        return c == 0;
    }

    private boolean isPartOfTetromino(Position position) {
        final int possibleX = position.getX() - parentField.getPosition().getX();
        final int possibleY = position.getY() - parentField.getPosition().getY();
        return
                possibleX >= 0 && possibleX < parentField.getTetromino().getXAxis()
                        && possibleY >= 0 && possibleY < parentField.getTetromino().getYAxis();
    }

    private boolean isOutOfBounds(Position position) {
        return
                position.getX() >= parentField.getXAxis()
                        || position.getY() >= parentField.getYAxis()
                        || position.getX() < 0;
    }
}


//           |  1 (0001)        2 (0010)      3 (0011)          4 (0100)
//  ---------+--------------------------------------------------------------------
//  1 (0001) |  null            11 (Steam)    12 (Magma)        101 (Ash)
//  2 (0010) |  11 (Steam)      null          21 (Growth)       110 (Rain)
//  3 (0011) |  12 (Magma)      21 (Growth)   null              111 (Sand Devil)
//  4 (0100) |  101(Ash)        110 (Rain)    111 (Sand Devil)  null
