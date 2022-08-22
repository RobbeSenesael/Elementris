package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HardcodedRepository {

    private static HardcodedRepository repo = new HardcodedRepository();

    private HardcodedRepository() {
    }

    public static HardcodedRepository getInstance() {
        return repo;
    }

    public Map<String, String> getElements() {
        final Map<String, String> elements = new HashMap<String, String>();
        elements.put("1", "fire");
        elements.put("2", "water");
        elements.put("3", "earth");
        elements.put("4", "air");
        elements.put("5", "neutral");
        elements.put("6", "wood");
        return elements;
    }

    public List<int[][]> getShapes() {
        final List<int[][]> shapes = new ArrayList<>();
        shapes.add(new int[][]{
                {1, 1, 1},
                {0, 1, 0},
        });
        shapes.add(new int[][]{
                {0, 1},
                {0, 1},
                {1, 1},
        });
        shapes.add(new int[][]{
                {1, 0},
                {1, 1},
                {0, 1},
        });
        shapes.add(new int[][]{
                {0, 1},
                {1, 1},
                {1, 0},
        });
        shapes.add(new int[][]{
                {1, 0},
                {1, 0},
                {1, 1},
        });
        shapes.add(new int[][]{
                {1},
                {1},
                {1},
                {1},
        });
        shapes.add(new int[][]{
                {1, 1},
                {1, 1},
        });
        shapes.add(new int[][]{
                {0, 1},
                {1, 1},
        });
        shapes.add(new int[][]{
                {1, 0},
                {1, 1},
        });
        shapes.add(new int[][]{
                {1},
                {1},
        });
        return shapes;
    }

}
