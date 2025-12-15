package valor;

import java.util.ArrayList;
import java.util.List;

import valor.cells.*;
import core.hero.Hero;
import core.monster.Monster;

/**
 * Fixed 8x8 three-lane map for Legends of Valor.
 * Lanes: columns [0,1], [3,4], [6,7]
 * Walls: columns 2 and 5 are inaccessible.
 * Row 0: Monster Nexus
 * Row 7: Hero Nexus
 */
public class ValorMap {

    private final int size;
    private final ValorCell[][] grid;

    public ValorMap(int size) {
        this.size = size;
        this.grid = new ValorCell[size][size];
        initMap();
    }

    private void initMap() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // columns 2 and 5 are walls
                if (c == 2 || c == 5) {
                    grid[r][c] = new InaccessibleCell();
                }
                // Row 0 = monsters' nexus, Row 7 = heroes' nexus
                else if (r == 0 || r == size - 1) {
                    grid[r][c] = new NexusCell();
                } else {
                    // 内部格子：随机分配 Plain / Bush / Cave / Koulou / Obstacle
                    double p = Math.random();
                    if (p < 0.1) grid[r][c] = new ObstacleCell();
                    else if (p < 0.25) grid[r][c] = new BushCell();
                    else if (p < 0.4) grid[r][c] = new CaveCell();
                    else if (p < 0.55) grid[r][c] = new KoulouCell();
                    else grid[r][c] = new PlainCell();
                }
            }
        }
    }

    public int getSize() {
        return size;
    }

    public ValorCell getCell(int row, int col) {
        return grid[row][col];
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public void placeInitialHeroes(List<Hero> heroes) {
        //bottom row 3 lane：col 0, 3, 6
        int row = size - 1;
        int[][] cols = {{0, 1}, {3, 4}, {6, 7}};

        for (int i = 0; i < heroes.size() && i < 3; i++) {
            int col = cols[i][0];
            heroes.get(i).setPosition(row, col);
        }
    }

    public void placeInitialMonsters(List<Monster> monsters) {
        // 与英雄对应，放在 top row （row 0）
        int row = 0;
        int[][] cols = {{0, 1}, {3, 4}, {6, 7}};

        for (int i = 0; i < monsters.size() && i < 3; i++) {
            int col = cols[i][0];
            monsters.get(i).setPosition(row, col);
        }
    }

    public boolean isAccessibleForHero(int row, int col) {
        if (!inBounds(row, col)) return false;
        return grid[row][col].isPassableForHero();
    }

    public boolean isAccessibleForMonster(int row, int col) {
        if (!inBounds(row, col)) return false;
        return grid[row][col].isPassableForMonster();
    }

    public boolean anyHeroReachedMonsterNexus(List<Hero> heroes) {
        for (Hero h : heroes) {
            if (!h.isAlive()) continue;
            if (h.getRow() == 0) return true;
        }
        return false;
    }

    public boolean anyMonsterReachedHeroNexus(List<Monster> monsters) {
        for (Monster m : monsters) {
            if (!m.isAlive()) continue;
            if (m.getRow() == size - 1) return true;
        }
        return false;
    }

    public void respawnHeroAtNexus(Hero hero) {
        int laneIndex = hero.getLaneIndex();
        int[][] cols = {{0, 1}, {3, 4}, {6, 7}};
        int col = cols[laneIndex][0];
        hero.setPosition(size - 1, col);
    }


    public void printMap(List<Hero> heroes, List<Monster> monsters) {
        char[][] display = new char[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                display[r][c] = grid[r][c].symbol();
            }
        }

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;
            int r = m.getRow();
            int c = m.getCol();
            if (inBounds(r, c)) {
                display[r][c] = 'M';
            }
        }

        for (Hero h : heroes) {
            if (!h.isAlive()) continue;
            int r = h.getRow();
            int c = h.getCol();
            if (inBounds(r, c)) {
                if (display[r][c] == 'M') display[r][c] = 'X'; // both present
                else display[r][c] = 'H';
            }
        }

        System.out.println("Map:");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print(display[r][c] + " ");
            }
            System.out.println();
        }
    }
}
