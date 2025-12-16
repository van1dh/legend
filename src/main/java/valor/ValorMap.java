package valor;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import valor.cells.*;
import character.hero.Hero;
import character.monster.Monster;

/**
 * 8x8 three-lane map for Legends of Valor.
 * Layout:
 * - Columns 0-1: Top Lane
 * - Column 2: Wall (Inaccessible)
 * - Columns 3-4: Mid Lane
 * - Column 5: Wall (Inaccessible)
 * - Columns 6-7: Bot Lane
 * - Row 0: Monster Nexus
 * - Row 7: Hero Nexus
 */
public class ValorMap {
    private static final int SIZE = 8;
    private static final int[] WALL_COLUMNS = {2, 5};

    private ValorCell[][] grid;
    private Random random;

    public ValorMap() {
        this.grid = new ValorCell[SIZE][SIZE];
        this.random = new Random();
        initializeMap();
    }

    /**
     * Initialize the map according to Valor specifications.
     */
    private void initializeMap() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // Walls at columns 2 and 5
                if (col == WALL_COLUMNS[0] || col == WALL_COLUMNS[1]) {
                    grid[row][col] = new InaccessibleCell();
                }
                // Nexus at rows 0 (monster) and 7 (hero)
                else if (row == 0 || row == 7) {
                    grid[row][col] = new NexusCell();
                }
                // Lane cells with random terrain
                else {
                    grid[row][col] = generateRandomTerrain();
                }
            }
        }
    }

    /**
     * Generate random terrain with proper distribution:
     * 20% Bush, 20% Cave, 20% Koulou, 40% Plain
     */
    private ValorCell generateRandomTerrain() {
        double rand = random.nextDouble();
        if (rand < 0.20) {
            return new BushCell();
        } else if (rand < 0.40) {
            return new CaveCell();
        } else if (rand < 0.60) {
            return new KoulouCell();
        } else {
            return new PlainCell();
        }
    }

    /**
     * Place initial heroes in their nexus (row 7, left column of each lane).
     */
    public void placeInitialHeroes(List<Hero> heroes) {
        int[][] laneColumns = {{0}, {3}, {6}}; // Left column of each lane

        for (int i = 0; i < Math.min(heroes.size(), 3); i++) {
            Hero hero = heroes.get(i);
            int col = laneColumns[i][0];
            hero.setPosition(7, col);
            hero.setLaneIndex(i);
        }
    }

    /**
     * Place initial monsters in their nexus (row 0, right column of each lane).
     */
    public void placeInitialMonsters(List<Monster> monsters) {
        int[][] laneColumns = {{1}, {4}, {7}}; // Right column of each lane

        for (int i = 0; i < Math.min(monsters.size(), 3); i++) {
            Monster monster = monsters.get(i);
            int col = laneColumns[i][0];
            monster.setPosition(0, col);
        }
    }

    /**
     * Spawn new monsters (every 8 rounds).
     */
    public void spawnMonsters(List<Monster> monsters) {
        int[][] laneColumns = {{1}, {4}, {7}};

        for (int i = 0; i < Math.min(monsters.size(), 3); i++) {
            Monster monster = monsters.get(i);
            int col = laneColumns[i][0];
            monster.setPosition(0, col);
        }
    }

    /**
     * Check if position is within bounds.
     */
    public boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Check if a hero can move to this position.
     */
    public boolean canHeroMoveTo(int row, int col, List<Hero> allHeroes, List<Monster> allMonsters) {
        if (!inBounds(row, col)) return false;
        if (!grid[row][col].isPassableForHero()) return false;

        // Cannot move to a cell with another hero
        for (Hero h : allHeroes) {
            if (h.isAlive() && h.getRow() == row && h.getCol() == col) {
                return false;
            }
        }

        // Cannot move behind a monster (must kill it first)
        // This is checked in the move validation logic

        return true;
    }

    /**
     * Check if there's a monster at this position.
     */
    public Monster getMonsterAt(int row, int col, List<Monster> monsters) {
        for (Monster m : monsters) {
            if (m.isAlive() && m.getRow() == row && m.getCol() == col) {
                return m;
            }
        }
        return null;
    }

    /**
     * Check if there's a hero at this position.
     */
    public Hero getHeroAt(int row, int col, List<Hero> heroes) {
        for (Hero h : heroes) {
            if (h.isAlive() && h.getRow() == row && h.getCol() == col) {
                return h;
            }
        }
        return null;
    }

    /**
     * Check if any hero reached monster nexus (row 0).
     */
    public boolean anyHeroReachedMonsterNexus(List<Hero> heroes) {
        for (Hero h : heroes) {
            if (h.isAlive() && h.getRow() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if any monster reached hero nexus (row 7).
     */
    public boolean anyMonsterReachedHeroNexus(List<Monster> monsters) {
        for (Monster m : monsters) {
            if (m.isAlive() && m.getRow() == 7) {
                return true;
            }
        }
        return false;
    }

    /**
     * Respawn hero at their nexus.
     */
    public void respawnHeroAtNexus(Hero hero) {
        int laneIndex = hero.getLaneIndex();
        int[][] laneColumns = {{0}, {3}, {6}};
        int col = laneColumns[laneIndex][0];
        hero.setPosition(7, col);
    }

    /**
     * Get lane index for a column.
     */
    public int getLaneForColumn(int col) {
        if (col <= 1) return 0; // Top lane
        if (col >= 3 && col <= 4) return 1; // Mid lane
        if (col >= 6) return 2; // Bot lane
        return -1; // Wall
    }

    /**
     * Apply terrain buff when hero enters a cell.
     */
    public void applyTerrainBuff(Hero hero) {
        ValorCell cell = grid[hero.getRow()][hero.getCol()];
        cell.onHeroEnter(hero);
    }

    /**
     * Remove terrain buff when hero leaves a cell.
     */
    public void removeTerrainBuff(Hero hero, int oldRow, int oldCol) {
        if (inBounds(oldRow, oldCol)) {
            ValorCell cell = grid[oldRow][oldCol];
            cell.onHeroLeave(hero);
        }
    }

    /**
     * Get cell at position.
     */
    public ValorCell getCell(int row, int col) {
        if (!inBounds(row, col)) return null;
        return grid[row][col];
    }

    /**
     * Display the map with heroes and monsters.
     */
    public void display(List<Hero> heroes, List<Monster> monsters) {
        System.out.println("\n========================================");
        System.out.println("  VALOR BATTLEFIELD");
        System.out.println("========================================");

        // Print column headers
        System.out.print("     ");
        for (int col = 0; col < SIZE; col++) {
            System.out.print(col + "   ");
        }
        System.out.println();
        System.out.println("   " + "----".repeat(SIZE));

        for (int row = 0; row < SIZE; row++) {
            System.out.print(row + " | ");

            for (int col = 0; col < SIZE; col++) {
                ValorCell cell = grid[row][col];
                Hero heroHere = getHeroAt(row, col, heroes);
                Monster monsterHere = getMonsterAt(row, col, monsters);

                String display;

                if (heroHere != null && monsterHere != null) {
                    // Both present - show X
                    display = "X";
                } else if (heroHere != null) {
                    // Show hero number (1, 2, 3)
                    display = "H" + (heroHere.getLaneIndex() + 1);
                } else if (monsterHere != null) {
                    // Show M
                    display = "M";
                } else {
                    // Show terrain
                    display = String.valueOf(cell.symbol());
                }

                // Pad to 3 characters for alignment
                System.out.print(String.format("%-3s", display) + " ");
            }
            System.out.println("|");
        }
        System.out.println("   " + "----".repeat(SIZE));

        // Legend
        System.out.println("\nLegend:");
        System.out.println("H1/H2/H3 - Heroes  |  M - Monster  |  X - Hero+Monster");
        System.out.println("N - Nexus  |  I - Wall  |  P - Plain");
        System.out.println("B - Bush (+DEX)  |  C - Cave (+AGI)  |  K - Koulou (+STR)");
        System.out.println();
    }

    public int getSize() {
        return SIZE;
    }
}