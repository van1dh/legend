package map;

import core.character.Hero;
import java.util.Random;

/**
 * Represents the 8x8 game map with accessible, inaccessible, and market cells.
 * Handles movement logic, display, and cell validation.
 */
public class Map {
    public static final int SIZE = 8;
    private final Cell[][] grid;

    public Map() {
        grid = new Cell[SIZE][SIZE];
        generateRandomMap();
    }

    /**
     * Generates a new map with random cell types.
     */
    private void generateRandomMap() {
        Random rand = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                double p = rand.nextDouble();
                if (p < 0.2) grid[i][j] = new InaccessibleCell();
                else if (p < 0.3) grid[i][j] = new MarketCell();
                else grid[i][j] = new CommonCell();
            }
        }
    }

    /**
     * Checks if a position is within the map and accessible.
     */
    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && grid[x][y].isAccessible();
    }

    /**
     * Prints the map with heroes displayed.
     */
    public void display(Hero[] heroes, int[] x, int[] y) {
        for (int i = 0; i < SIZE; i++) {
            for (int row = 0; row < 3; row++) {
                for (int j = 0; j < SIZE; j++) {
                    if (row == 1 && hasHeroAt(heroes, x, y, i, j)) {
                        System.out.print("|" + Colors.colorText("H", Colors.YELLOW) + " ");
                    } else {
                        System.out.print("|" + grid[i][j].displayRow(row));
                    }
                }
                System.out.println("|");
            }
        }
    }

    private boolean hasHeroAt(Hero[] heroes, int[] x, int[] y, int i, int j) {
        for (int k = 0; k < heroes.length; k++) {
            if (x[k] == i && y[k] == j) return true;
        }
        return false;
    }

    public boolean isMarket(int x, int y) {
        return grid[x][y] instanceof MarketCell;
    }
}