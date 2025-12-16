package valor;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import character.hero.Hero;
import character.monster.Monster;
import item.Spell;
import item.Potion;

/**
 * Handles all hero actions in Legends of Valor.
 */
public class ValorActions {

    /**
     * Move hero (W/A/S/D).
     * Rules:
     * - Cannot move behind a monster without killing it
     * - Cannot move to a cell with another hero
     * - Cannot move through walls
     */
    public static boolean moveHero(Scanner scanner, ValorMap map, Hero hero,
                                   List<Hero> allHeroes, List<Monster> allMonsters) {
        System.out.println("\n--- MOVE ---");
        System.out.print("Direction (W=Up, A=Left, S=Down, D=Right, or 0 to cancel): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("0")) return false;

        int oldRow = hero.getRow();
        int oldCol = hero.getCol();
        int newRow = oldRow;
        int newCol = oldCol;

        switch (input) {
            case "W": newRow--; break; // North
            case "S": newRow++; break; // South
            case "A": newCol--; break; // West
            case "D": newCol++; break; // East
            default:
                System.out.println("[ERROR] Invalid direction!");
                return false;
        }

        // Validate move
        if (!map.inBounds(newRow, newCol)) {
            System.out.println("[ERROR] Cannot move out of bounds!");
            return false;
        }

        ValorCell cell = map.getCell(newRow, newCol);
        if (!cell.isPassableForHero()) {
            System.out.println("[ERROR] Cannot move to that cell!");
            return false;
        }

        // Check if another hero is there
        if (map.getHeroAt(newRow, newCol, allHeroes) != null) {
            System.out.println("[ERROR] Another hero is already in that space!");
            return false;
        }

        // Check if trying to move behind a monster
        if (!canMoveWithoutBypassingMonster(hero, oldRow, oldCol, newRow, newCol, allMonsters)) {
            System.out.println("[ERROR] Cannot move behind a monster without killing it!");
            return false;
        }

        // Remove old terrain buff
        map.removeTerrainBuff(hero, oldRow, oldCol);

        // Move hero
        hero.setPosition(newRow, newCol);

        // Apply new terrain buff
        map.applyTerrainBuff(hero);

        System.out.println("[SUCCESS] " + hero.getName() + " moved to (" + newRow + "," + newCol + ")");
        return true;
    }

    /**
     * Check if hero can move without bypassing a monster.
     * Heroes cannot move "behind" a monster (towards their own nexus from the monster).
     */
    private static boolean canMoveWithoutBypassingMonster(Hero hero, int oldRow, int oldCol,
                                                          int newRow, int newCol,
                                                          List<Monster> monsters) {
        // Check if there's a monster between old and new position
        // Hero moves from row 7 (bottom) towards row 0 (top)
        // Moving "behind" means moving towards row 7 when monster is closer to row 0

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            // Only check monsters in the same lane
            if (Math.abs(m.getCol() - oldCol) > 1) continue;

            int monsterRow = m.getRow();

            // If moving south (towards nexus) and monster is north of new position
            if (newRow > oldRow && monsterRow >= oldRow && monsterRow < newRow) {
                return false; // Trying to move behind monster
            }
        }

        return true;
    }

    /**
     * Attack a monster.
     * Range: current cell and adjacent cells (not diagonal).
     */
    public static boolean attack(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        List<Monster> inRange = getMonstersInAttackRange(hero, monsters);

        if (inRange.isEmpty()) {
            System.out.println("[ERROR] No monsters in attack range!");
            return false;
        }

        System.out.println("\n--- ATTACK ---");
        System.out.println("Monsters in range:");
        for (int i = 0; i < inRange.size(); i++) {
            Monster m = inRange.get(i);
            System.out.println((i + 1) + ") " + m.getName() +
                    " at (" + m.getRow() + "," + m.getCol() +
                    ") HP: " + m.getCurrentHP());
        }

        System.out.print("Select target (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > inRange.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Monster target = inRange.get(choice - 1);
            hero.attack(target);

            if (!target.isAlive()) {
                System.out.println("[VICTORY] " + target.getName() + " defeated!");
            }

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Cast spell on a monster.
     * Same range as attack.
     */
    public static boolean castSpell(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        if (hero.getInventory().getSpells().isEmpty()) {
            System.out.println("[ERROR] No spells available!");
            return false;
        }

        List<Monster> inRange = getMonstersInAttackRange(hero, monsters);

        if (inRange.isEmpty()) {
            System.out.println("[ERROR] No monsters in range!");
            return false;
        }

        System.out.println("\n--- CAST SPELL ---");

        // Show spells
        List<Spell> spells = hero.getInventory().getSpells();
        System.out.println("Available spells (" + spells.size() + " total):");

        java.util.Map<String, Integer> spellCounts = new java.util.HashMap<>();
        for (Spell s : spells) {
            spellCounts.put(s.getName(), spellCounts.getOrDefault(s.getName(), 0) + 1);
        }

        int index = 1;
        java.util.Set<String> displayed = new java.util.HashSet<>();
        for (Spell s : spells) {
            if (!displayed.contains(s.getName())) {
                int count = spellCounts.get(s.getName());
                System.out.println((index++) + ") " + s.getName() +
                        " (DMG: " + s.getBaseDamage() +
                        ", Mana: " + s.getManaCost() + ") x" + count);
                displayed.add(s.getName());
            }
        }

        System.out.print("Select spell (or 0 to cancel): ");
        try {
            int spellChoice = Integer.parseInt(scanner.nextLine().trim());
            if (spellChoice == 0) return false;
            if (spellChoice < 1 || spellChoice > displayed.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            String selectedName = (String) displayed.toArray()[spellChoice - 1];
            Spell spell = null;
            for (Spell s : spells) {
                if (s.getName().equals(selectedName)) {
                    spell = s;
                    break;
                }
            }

            if (hero.getMana() < spell.getManaCost()) {
                System.out.println("[ERROR] Not enough mana!");
                return false;
            }

            // Select target
            System.out.println("\nMonsters in range:");
            for (int i = 0; i < inRange.size(); i++) {
                Monster m = inRange.get(i);
                System.out.println((i + 1) + ") " + m.getName() +
                        " at (" + m.getRow() + "," + m.getCol() +
                        ") HP: " + m.getCurrentHP());
            }

            System.out.print("Select target: ");
            int targetChoice = Integer.parseInt(scanner.nextLine().trim());
            if (targetChoice < 1 || targetChoice > inRange.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Monster target = inRange.get(targetChoice - 1);
            hero.castSpell(spell, target);

            if (!target.isAlive()) {
                System.out.println("[VICTORY] " + target.getName() + " defeated!");
            }

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Use a potion.
     */
    public static boolean usePotion(Scanner scanner, Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("[ERROR] No potions available!");
            return false;
        }

        System.out.println("\n--- USE POTION ---");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.println((i + 1) + ") " + p.getName() +
                    " (+" + p.getEffectAmount() + " to " + p.getAffectedAttributes() + ")");
        }

        System.out.print("Select potion (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > potions.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Potion potion = potions.get(choice - 1);
            hero.usePotion(potion);

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Teleport to another lane (adjacent to another hero).
     */
    public static boolean teleport(Scanner scanner, ValorMap map, Hero hero, List<Hero> allHeroes,
                                   List<Monster> allMonsters) {
        System.out.println("\n--- TELEPORT ---");

        // Find heroes in other lanes
        List<Hero> otherLaneHeroes = new ArrayList<>();
        for (Hero h : allHeroes) {
            if (h != hero && h.isAlive() && h.getLaneIndex() != hero.getLaneIndex()) {
                otherLaneHeroes.add(h);
            }
        }

        if (otherLaneHeroes.isEmpty()) {
            System.out.println("[ERROR] No heroes in other lanes!");
            return false;
        }

        System.out.println("Teleport to hero:");
        for (int i = 0; i < otherLaneHeroes.size(); i++) {
            Hero h = otherLaneHeroes.get(i);
            System.out.println((i + 1) + ") " + h.getName() +
                    " at (" + h.getRow() + "," + h.getCol() + ")");
        }

        System.out.print("Select hero (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > otherLaneHeroes.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Hero targetHero = otherLaneHeroes.get(choice - 1);

            // Find valid adjacent positions
            int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
            List<int[]> validPositions = new ArrayList<>();

            for (int[] dir : directions) {
                int newRow = targetHero.getRow() + dir[0];
                int newCol = targetHero.getCol() + dir[1];

                if (map.canHeroMoveTo(newRow, newCol, allHeroes, allMonsters)) {
                    // Check not behind monster
                    boolean behindMonster = false;
                    for (Monster m : allMonsters) {
                        if (m.isAlive() && m.getRow() < newRow &&
                                Math.abs(m.getCol() - newCol) <= 1) {
                            behindMonster = true;
                            break;
                        }
                    }
                    if (!behindMonster) {
                        validPositions.add(new int[]{newRow, newCol});
                    }
                }
            }

            if (validPositions.isEmpty()) {
                System.out.println("[ERROR] No valid positions to teleport to!");
                return false;
            }

            // Teleport to first valid position
            int[] pos = validPositions.get(0);
            int oldRow = hero.getRow();
            int oldCol = hero.getCol();

            map.removeTerrainBuff(hero, oldRow, oldCol);
            hero.setPosition(pos[0], pos[1]);
            hero.setLaneIndex(map.getLaneForColumn(pos[1]));
            map.applyTerrainBuff(hero);

            System.out.println("[SUCCESS] Teleported to (" + pos[0] + "," + pos[1] + ")");
            return true;

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Recall hero to their nexus.
     */
    public static boolean recall(ValorMap map, Hero hero) {
        System.out.println("\n--- RECALL ---");

        int oldRow = hero.getRow();
        int oldCol = hero.getCol();

        map.removeTerrainBuff(hero, oldRow, oldCol);
        map.respawnHeroAtNexus(hero);
        map.applyTerrainBuff(hero);

        System.out.println("[SUCCESS] " + hero.getName() + " recalled to Nexus!");
        return true;
    }

    /**
     * Get monsters within attack range (adjacent cells).
     */
    private static List<Monster> getMonstersInAttackRange(Hero hero, List<Monster> monsters) {
        List<Monster> inRange = new ArrayList<>();
        int heroRow = hero.getRow();
        int heroCol = hero.getCol();

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            int mRow = m.getRow();
            int mCol = m.getCol();

            // Check if adjacent (Manhattan distance = 1) or same cell
            int distance = Math.abs(heroRow - mRow) + Math.abs(heroCol - mCol);
            if (distance <= 1) {
                inRange.add(m);
            }
        }

        return inRange;
    }
}