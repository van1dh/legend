package valor;

import java.util.List;
import java.util.Scanner;

import core.hero.Hero;
import core.item.Potion;
import core.item.Spell;
import core.item.Weapon;
import core.monster.Monster;

/**
 * Helper methods that implement hero actions in Valor.
 * All methods return true if the hero successfully used up their action.
 */
public class Actions {

    public static boolean moveHero(Scanner scanner, ValorMap map, Hero hero) {
        System.out.print("Move (W/A/S/D): ");
        String input = scanner.nextLine().trim().toUpperCase();
        int row = hero.getRow();
        int col = hero.getCol();
        int newRow = row;
        int newCol = col;
        switch (input) {
            case "W" -> newRow--;
            case "S" -> newRow++;
            case "A" -> newCol--;
            case "D" -> newCol++;
            default -> {
                System.out.println("Invalid direction.");
                return false;
            }
        }
        if (!map.inBounds(newRow, newCol) || !map.isAccessibleForHero(newRow, newCol)) {
            System.out.println("Cannot move there.");
            return false;
        }
        hero.setPosition(newRow, newCol);
        return true;
    }

    public static boolean heroAttack(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        Monster target = null;
        for (Monster m : monsters) {
            if (!m.isAlive()) continue;
            int dist = Math.abs(hero.getRow() - m.getRow()) + Math.abs(hero.getCol() - m.getCol());
            if (dist <= 1) {
                target = m;
                break;
            }
        }
        if (target == null) {
            System.out.println("No monster in attack range.");
            return false;
        }
        hero.attack(target);
        System.out.println(hero.getName() + " attacks " + target.getName());
        return true;
    }

    public static boolean heroCastSpell(Scanner scanner, ValorMap map, Hero hero, List<Monster> monsters) {
        System.out.println("Casting spells is not fully implemented yet.");
        return false;
    }

    public static boolean heroUsePotion(Scanner scanner, Hero hero) {
        System.out.println("Using potions is not fully implemented yet.");
        return false;
    }

    public static boolean teleportHero(Scanner scanner, ValorMap map, Hero hero) {
        System.out.println("Teleport not fully implemented yet.");
        return false;
    }

    public static void recallHero(ValorMap map, Hero hero) {
        map.respawnHeroAtNexus(hero);
        System.out.println("Hero recalled to Nexus.");
    }

    public static boolean removeObstacle(Scanner scanner, ValorMap map, Hero hero) {
        System.out.println("Remove obstacle not fully implemented yet.");
        return false;
    }
}
