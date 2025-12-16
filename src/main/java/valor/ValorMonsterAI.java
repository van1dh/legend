package valor;

import java.util.List;
import java.util.ArrayList;

import character.hero.Hero;
import character.monster.Monster;

/**
 * Monster AI for Legends of Valor.
 * Rules:
 * - If hero in attack range, attack
 * - Otherwise, move south (towards hero nexus)
 */
public class ValorMonsterAI {

    /**
     * Process all monsters' turns.
     */
    public static void processMonsters(ValorMap map, List<Monster> monsters, List<Hero> heroes) {
        System.out.println("\n=== MONSTERS' TURN ===");

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            // Check if any hero is in attack range
            Hero target = findHeroInAttackRange(m, heroes);

            if (target != null) {
                // Attack the hero
                m.attack(target);

                if (!target.isAlive()) {
                    System.out.println("[DEFEAT] " + target.getName() + " has fallen!");
                }
            } else {
                // Move south (towards hero nexus)
                moveSouth(m, map, monsters, heroes);
            }
        }
    }

    /**
     * Find a hero within attack range of the monster.
     * Attack range: current cell and adjacent cells.
     */
    private static Hero findHeroInAttackRange(Monster m, List<Hero> heroes) {
        int mRow = m.getRow();
        int mCol = m.getCol();

        for (Hero h : heroes) {
            if (!h.isAlive()) continue;

            int hRow = h.getRow();
            int hCol = h.getCol();

            // Check if adjacent (Manhattan distance <= 1)
            int distance = Math.abs(mRow - hRow) + Math.abs(mCol - hCol);
            if (distance <= 1) {
                return h;
            }
        }

        return null;
    }

    /**
     * Move monster one step south (towards hero nexus).
     * Follow same movement rules as heroes:
     * - Cannot move behind another monster
     * - Cannot move to cell with another monster
     */
    private static void moveSouth(Monster m, ValorMap map, List<Monster> allMonsters, List<Hero> heroes) {
        int currentRow = m.getRow();
        int currentCol = m.getCol();
        int newRow = currentRow + 1; // Move south
        int newCol = currentCol;

        // Check if can move
        if (!map.inBounds(newRow, newCol)) {
            return; // Can't move further
        }

        ValorCell cell = map.getCell(newRow, newCol);
        if (!cell.isPassableForMonster()) {
            return; // Can't move to this cell
        }

        // Check if another monster is there
        for (Monster other : allMonsters) {
            if (other != m && other.isAlive() &&
                    other.getRow() == newRow && other.getCol() == newCol) {
                return; // Another monster is blocking
            }
        }

        // Check if hero is there (can share space with hero)
        // This is allowed in Valor

        // Move
        m.setPosition(newRow, newCol);
        System.out.println(m.getName() + " moved to (" + newRow + "," + newCol + ")");
    }
}
