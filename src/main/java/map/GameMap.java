package map;

import util.Position;

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
        // Generate random map: 20% inaccessible, 30% market, 50% common
        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                double p = rand.nextDouble();
                if (p < 0.2) {
                    grid[i][j] = new InaccessibleCell();
                } else if (p < 0.5) {
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
        if (pos == null) return new int[]{0, 0};
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

                // Check if any hero is at this position
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

