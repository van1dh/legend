package game;

import character.hero.Hero;
import character.monster.Monster;
import item.Spell;
import item.Potion;

import java.util.List;
import java.util.Scanner;

/**
 * Manages the battle between heroes and monsters.
 * Implements turn-based combat with hero actions and monster AI.
 */
public class BattleManager {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private Scanner scanner;

    public BattleManager(Scanner scanner, List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.scanner = scanner;
    }

    /**
     * Execute the battle and return true if heroes win.
     */
    public boolean fight() {
        int round = 1;

        while (hasAliveHeroes() && hasAliveMonsters()) {
            System.out.println("\n=== ROUND " + round + " ===");
            displayBattleStatus();

            // Heroes' turn
            heroTurn();

            if (!hasAliveMonsters()) {
                break;
            }

            // Monsters' turn
            monsterTurn();

            if (!hasAliveHeroes()) {
                break;
            }

            // End of round regeneration
            endOfRoundRegen();

            round++;
        }

        return hasAliveHeroes();
    }

    /**
     * Display current battle status.
     */
    private void displayBattleStatus() {
        System.out.println("\n--- HEROES ---");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            String status = h.isAlive() ? "ALIVE" : "FAINTED";
            System.out.println((i + 1) + ") " + h.getName() + " [" + status + "]");
            if (h.isAlive()) {
                System.out.println("   HP: " + h.getHP() + "/" + h.getMaxHP() +
                        " | Mana: " + h.getMana() + "/" + h.getMaxMana());
            }
        }

        System.out.println("\n--- MONSTERS ---");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            String status = m.isAlive() ? "ALIVE" : "DEFEATED";
            System.out.println((i + 1) + ") " + m.getName() + " [" + status + "]");
            if (m.isAlive()) {
                System.out.println("   HP: " + m.getCurrentHP() + "/" + m.getMaxHP() +
                        " | DMG: " + m.getBaseDamage() + " | DEF: " + m.getDefense());
            }
        }
        System.out.println();
    }

    /**
     * Process all heroes' turns.
     */
    private void heroTurn() {
        for (Hero hero : heroes) {
            if (!hero.isAlive()) {
                continue;
            }

            System.out.println("\n" + hero.getName() + "'s turn:");
            processHeroAction(hero);
        }
    }

    /**
     * Process a single hero's action.
     */
    private void processHeroAction(Hero hero) {
        while (true) {
            System.out.println("\nChoose action:");
            System.out.println("1) Attack with weapon");
            System.out.println("2) Cast spell");
            System.out.println("3) Use potion");
            System.out.println("4) View stats (doesn't use turn)");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (heroAttack(hero)) return;
                    break;
                case "2":
                    if (heroCastSpell(hero)) return;
                    break;
                case "3":
                    if (heroUsePotion(hero)) return;
                    break;
                case "4":
                    displayBattleStatus();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    /**
     * Hero attacks a monster with equipped weapon.
     */
    private boolean heroAttack(Hero hero) {
        if (hero.getEquippedWeapon() == null) {
            System.out.println("No weapon equipped!");
            return false;
        }

        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }

        hero.attack(target);

        if (!target.isAlive()) {
            System.out.println(target.getName() + " has been defeated!");
        }

        return true;
    }

    /**
     * Hero casts a spell on a monster.
     */
    private boolean heroCastSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println("No spells in inventory!");
            return false;
        }

        System.out.println("\nAvailable spells:");
        for (int i = 0; i < spells.size(); i++) {
            System.out.println((i + 1) + ") " + spells.get(i));
        }
        System.out.print("Select spell (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > spells.size()) {
                System.out.println("Invalid choice!");
                return false;
            }

            Spell spell = spells.get(choice - 1);

            if (hero.getMana() < spell.getManaCost()) {
                System.out.println("Not enough mana!");
                return false;
            }

            Monster target = selectMonsterTarget();
            if (target == null) {
                return false;
            }

            hero.castSpell(spell, target);

            if (!target.isAlive()) {
                System.out.println(target.getName() + " has been defeated!");
            }

            return true;

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return false;
        }
    }

    /**
     * Hero uses a potion.
     */
    private boolean heroUsePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("No potions in inventory!");
            return false;
        }

        System.out.println("\nAvailable potions:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ") " + potions.get(i));
        }
        System.out.print("Select potion (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > potions.size()) {
                System.out.println("Invalid choice!");
                return false;
            }

            Potion potion = potions.get(choice - 1);
            hero.usePotion(potion);

            return true;

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return false;
        }
    }

    /**
     * Let player select a monster target.
     */
    private Monster selectMonsterTarget() {
        List<Monster> aliveMonsters = monsters.stream()
                .filter(Monster::isAlive)
                .toList();

        if (aliveMonsters.isEmpty()) {
            return null;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < aliveMonsters.size(); i++) {
            System.out.println((i + 1) + ") " + aliveMonsters.get(i).getName() +
                    " (HP: " + aliveMonsters.get(i).getCurrentHP() + ")");
        }
        System.out.print("Target (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return null;
            if (choice >= 1 && choice <= aliveMonsters.size()) {
                return aliveMonsters.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }

        return null;
    }

    /**
     * Process all monsters' turns (simple AI).
     */
    private void monsterTurn() {
        System.out.println("\n=== MONSTERS' TURN ===");

        for (Monster monster : monsters) {
            if (!monster.isAlive()) {
                continue;
            }

            // Simple AI: attack random alive hero
            List<Hero> aliveHeroes = heroes.stream()
                    .filter(Hero::isAlive)
                    .toList();

            if (!aliveHeroes.isEmpty()) {
                Hero target = aliveHeroes.get((int) (Math.random() * aliveHeroes.size()));
                monster.attack(target);

                if (!target.isAlive()) {
                    System.out.println(target.getName() + " has fainted!");
                }
            }
        }
    }

    /**
     * End of round: heroes regenerate 10% HP and Mana.
     */
    private void endOfRoundRegen() {
        System.out.println("\n--- End of Round Regeneration ---");
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                int oldHP = hero.getHP();
                int oldMana = hero.getMana();
                hero.regen();
                System.out.println(hero.getName() + " regenerated " +
                        (hero.getHP() - oldHP) + " HP and " +
                        (hero.getMana() - oldMana) + " Mana");
            }
        }
    }

    /**
     * Check if any hero is still alive.
     */
    private boolean hasAliveHeroes() {
        return heroes.stream().anyMatch(Hero::isAlive);
    }

    /**
     * Check if any monster is still alive.
     */
    private boolean hasAliveMonsters() {
        return monsters.stream().anyMatch(Monster::isAlive);
    }
}
