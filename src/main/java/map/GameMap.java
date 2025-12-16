package map;

import java.util.Arrays;

/**
 * GameMap handles the grid of the game world, including hero positions and cell types.
 */
public class GameMap {
    private final Cell[][] grid;
    private final Position[] heroPositions;

    public GameMap(int rows, int cols, int numHeroes) {
        grid = new Cell[rows][cols];
        heroPositions = new Position[numHeroes];
        initDefaultMap();
    }

    private void initDefaultMap() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (j % 3 == 1) {
                    grid[i][j] = new InaccessibleCell();
                } else if (j % 3 == 2) {
                    grid[i][j] = new MarketCell();
                } else {
                    grid[i][j] = new CommonCell();
                }
            }
        }
    }

    public void setHeroPosition(int index, int x, int y) {
        if (inBounds(x, y) && grid[x][y].isAccessible()) {
            heroPositions[index] = new Position(x, y);
        }
    }

    public int[] getHeroPosition(int index) {
        Position pos = heroPositions[index];
        return new int[]{pos.getX(), pos.getY()};
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public void displayMap() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                boolean occupied = false;
                for (int k = 0; k < heroPositions.length; k++) {
                    Position pos = heroPositions[k];
                    if (pos != null && pos.getX() == i && pos.getY() == j) {
                        System.out.print("H" + (k + 1) + " ");
                        occupied = true;
                        break;
                    }
                }
                if (!occupied) {
                    System.out.print(grid[i][j].toChar() + "  ");
                }
            }
            System.out.println();
        }
    }
}

