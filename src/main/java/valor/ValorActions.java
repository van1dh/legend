package valor;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import character.hero.Hero;
import character.monster.Monster;
import item.Spell;
import item.Potion;
import item.Weapon;
import item.Armor;

/**
 * Handles all hero actions in Legends of Valor.
 * Complete implementation of all PDF-specified actions.
 */
public class ValorActions {

    /**
     * Move hero (W/A/S/D).
     * Rules:
     * - Cannot move behind a monster without killing it
     * - Cannot move to a cell with another hero
     * - Cannot move through walls
     * - No diagonal movement
     */
    public static boolean moveHero(Scanner scanner, ValorMap map, Hero hero,
                                   List<Hero> allHeroes, List<Monster> allMonsters) {
        System.out.println("\n--- MOVE ---");
        System.out.print("Direction (W=Up/North, A=Left/West, S=Down/South, D=Right/East, or 0 to cancel): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("0")) return false;

        int oldRow = hero.getRow();
        int oldCol = hero.getCol();
        int newRow = oldRow;
        int newCol = oldCol;

        switch (input) {
            case "W": newRow--; break; // North (towards monster nexus)
            case "S": newRow++; break; // South (towards hero nexus)
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
            System.out.println("[ERROR] Cannot move to that cell (wall or obstacle)!");
            return false;
        }

        // Check if another hero is there
        if (map.getHeroAt(newRow, newCol, allHeroes) != null) {
            System.out.println("[ERROR] Another hero is already in that space!");
            return false;
        }

        // Check if trying to move behind a monster (heroes can't move past monsters)
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
     * Heroes cannot move "behind" a monster (away from monster towards their own nexus).
     */
    private static boolean canMoveWithoutBypassingMonster(Hero hero, int oldRow, int oldCol,
                                                          int newRow, int newCol,
                                                          List<Monster> monsters) {
        // Check all monsters in the same lane
        int heroLane = hero.getLaneIndex();

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            // Check if monster is in same lane (within 1 column of hero)
            if (Math.abs(m.getCol() - oldCol) > 1 && Math.abs(m.getCol() - newCol) > 1) {
                continue; // Different lane
            }

            int monsterRow = m.getRow();

            // If moving south (towards hero nexus) and monster is north of hero
            // This means trying to move "behind" the monster
            if (newRow > oldRow && monsterRow < newRow && monsterRow >= oldRow) {
                // Check if monster is actually blocking this path
                if (Math.abs(m.getCol() - newCol) <= 1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Attack a monster.
     * Range: current cell and adjacent cells (Manhattan distance <= 1).
     */
    public static boolean attack(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        if (hero.getEquippedWeapon() == null) {
            System.out.println("[ERROR] No weapon equipped!");
            return false;
        }

        List<Monster> inRange = getMonstersInAttackRange(hero, monsters);

        if (inRange.isEmpty()) {
            System.out.println("[ERROR] No monsters in attack range!");
            System.out.println("(Attack range: current cell and adjacent cells)");
            return false;
        }

        System.out.println("\n--- ATTACK ---");
        System.out.println("Monsters in range:");
        for (int i = 0; i < inRange.size(); i++) {
            Monster m = inRange.get(i);
            System.out.println((i + 1) + ") " + m.getName() +
                    " at (" + m.getRow() + "," + m.getCol() +
                    ") HP: " + m.getCurrentHP() + "/" + m.getMaxHP());
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
     * Same range as attack (current and adjacent cells).
     */
    public static boolean castSpell(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        if (hero.getInventory().getSpells().isEmpty()) {
            System.out.println("[ERROR] No spells available!");
            return false;
        }

        List<Monster> inRange = getMonstersInAttackRange(hero, monsters);

        if (inRange.isEmpty()) {
            System.out.println("[ERROR] No monsters in range!");
            System.out.println("(Spell range: current cell and adjacent cells)");
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
                System.out.println("[ERROR] Not enough mana! (Need: " + spell.getManaCost() +
                        ", Have: " + hero.getMana() + ")");
                return false;
            }

            // Select target
            System.out.println("\nMonsters in range:");
            for (int i = 0; i < inRange.size(); i++) {
                Monster m = inRange.get(i);
                System.out.println((i + 1) + ") " + m.getName() +
                        " at (" + m.getRow() + "," + m.getCol() +
                        ") HP: " + m.getCurrentHP() + "/" + m.getMaxHP());
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
     * Change weapon equipment.
     */
    public static boolean changeWeapon(Scanner scanner, Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("[ERROR] No weapons in inventory!");
            return false;
        }

        System.out.println("\n--- CHANGE WEAPON ---");
        System.out.println("Currently equipped: " +
                (hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getName() : "None"));
        System.out.println();

        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            String equipped = (w == hero.getEquippedWeapon()) ? " [EQUIPPED]" : "";
            System.out.println((i + 1) + ") " + w.getName() +
                    " (DMG: " + w.getDamage() + ", Hands: " + w.getHandsRequired() + ")" + equipped);
        }

        System.out.print("Select weapon to equip (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > weapons.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Weapon weapon = weapons.get(choice - 1);
            hero.equipWeapon(weapon);
            System.out.println("[SUCCESS] Weapon equipped!");

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Change armor equipment.
     */
    public static boolean changeArmor(Scanner scanner, Hero hero) {
        List<Armor> armors = hero.getInventory().getArmors();

        if (armors.isEmpty()) {
            System.out.println("[ERROR] No armor in inventory!");
            return false;
        }

        System.out.println("\n--- CHANGE ARMOR ---");
        System.out.println("Currently equipped: " +
                (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
        System.out.println();

        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            String equipped = (a == hero.getEquippedArmor()) ? " [EQUIPPED]" : "";
            System.out.println((i + 1) + ") " + a.getName() +
                    " (DEF: " + a.getDamageReduction() + ")" + equipped);
        }

        System.out.print("Select armor to equip (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > armors.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            Armor armor = armors.get(choice - 1);
            hero.equipArmor(armor);
            System.out.println("[SUCCESS] Armor equipped!");

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Teleport to another lane (adjacent to another hero).
     * Can only teleport between different lanes.
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
            System.out.println("[ERROR] No heroes in other lanes to teleport to!");
            return false;
        }

        System.out.println("Available heroes to teleport to:");
        for (int i = 0; i < otherLaneHeroes.size(); i++) {
            Hero h = otherLaneHeroes.get(i);
            System.out.println((i + 1) + ") " + h.getName() +
                    " (Lane " + (h.getLaneIndex() + 1) + ") at (" + h.getRow() + "," + h.getCol() + ")");
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

            // Find valid adjacent positions (4 directions)
            int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
            List<int[]> validPositions = new ArrayList<>();

            for (int[] dir : directions) {
                int newRow = targetHero.getRow() + dir[0];
                int newCol = targetHero.getCol() + dir[1];

                if (map.inBounds(newRow, newCol)) {
                    ValorCell cell = map.getCell(newRow, newCol);

                    // Check if passable and not occupied by another hero
                    if (cell.isPassableForHero() && map.getHeroAt(newRow, newCol, allHeroes) == null) {
                        // Check not ahead of target hero (can't teleport ahead)
                        if (newRow >= targetHero.getRow()) {
                            // Check not behind a monster in the new lane
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
            // DO NOT change laneIndex - hero keeps their original lane identity
            map.applyTerrainBuff(hero);

            int currentLane = map.getLaneForColumn(pos[1]);
            System.out.println("[SUCCESS] " + hero.getName() + " (Lane " + (hero.getLaneIndex() + 1) +
                    ") teleported to Lane " + (currentLane + 1) + " at (" + pos[0] + "," + pos[1] + ")");
            return true;

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    /**
     * Recall hero to their original nexus.
     * Always returns to the nexus space they spawned at.
     */
    public static boolean recall(ValorMap map, Hero hero) {
        System.out.println("\n--- RECALL ---");
        System.out.print("Confirm recall to Nexus? (Y/N): ");

        int oldRow = hero.getRow();
        int oldCol = hero.getCol();

        map.removeTerrainBuff(hero, oldRow, oldCol);
        map.respawnHeroAtNexus(hero);
        map.applyTerrainBuff(hero);

        System.out.println("[SUCCESS] " + hero.getName() + " recalled to Nexus at Lane " +
                (hero.getLaneIndex() + 1) + "!");
        return true;
    }

    /**
     * Pass turn (do nothing).
     */
    public static boolean passTurn(Hero hero) {
        System.out.println("[PASS] " + hero.getName() + " passes their turn.");
        return true;
    }

    /**
     * Get monsters within attack range (adjacent cells, Manhattan distance <= 1).
     */
    private static List<Monster> getMonstersInAttackRange(Hero hero, List<Monster> monsters) {
        List<Monster> inRange = new ArrayList<>();
        int heroRow = hero.getRow();
        int heroCol = hero.getCol();

        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            int mRow = m.getRow();
            int mCol = m.getCol();

            // Check if adjacent (Manhattan distance <= 1) or same cell
            int distance = Math.abs(heroRow - mRow) + Math.abs(heroCol - mCol);
            if (distance <= 1) {
                inRange.add(m);
            }
        }

        return inRange;
    }
}