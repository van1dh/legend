package game;

import character.hero.Hero;
import character.monster.Monster;
import item.Spell;
import item.Potion;

import java.util.List;
import java.util.Scanner;

/**
 * Manages the battle between heroes and monsters.
 * Features: Quit during battle, detailed stats display.
 */
public class BattleManager {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private Scanner scanner;
    private boolean playerQuit;

    public BattleManager(Scanner scanner, List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.scanner = scanner;
        this.playerQuit = false;
    }

    public boolean fight() {
        int round = 1;

        while (hasAliveHeroes() && hasAliveMonsters()) {
            System.out.println("\n========================================");
            System.out.println("  ROUND " + round);
            System.out.println("========================================");
            displayBattleStatus();

            heroTurn();

            if (playerQuit) {
                return false;
            }

            if (!hasAliveMonsters()) {
                break;
            }

            monsterTurn();

            if (!hasAliveHeroes()) {
                break;
            }

            endOfRoundRegen();

            round++;
        }

        return hasAliveHeroes();
    }

    public boolean isPlayerQuit() {
        return playerQuit;
    }

    private void displayBattleStatus() {
        System.out.println("\n--- YOUR HEROES ---");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            String status = h.isAlive() ? "ALIVE" : "FAINTED";
            System.out.println((i + 1) + ") " + h.getName() + " [" + status + "]");
            if (h.isAlive()) {
                String hpBar = createBar(h.getHP(), h.getMaxHP(), 20, "=", "-");
                String manaBar = createBar(h.getMana(), h.getMaxMana(), 20, "=", "-");
                System.out.println("   HP:   [" + hpBar + "] " + h.getHP() + "/" + h.getMaxHP());
                System.out.println("   Mana: [" + manaBar + "] " + h.getMana() + "/" + h.getMaxMana());
            }
        }

        System.out.println("\n--- ENEMIES ---");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            String status = m.isAlive() ? "ALIVE" : "DEFEATED";
            System.out.println((i + 1) + ") " + m.getName() + " [" + status + "]");
            if (m.isAlive()) {
                String hpBar = createBar(m.getCurrentHP(), m.getMaxHP(), 20, "=", "-");
                System.out.println("   HP:  [" + hpBar + "] " + m.getCurrentHP() + "/" + m.getMaxHP() +
                        " | DMG: " + m.getBaseDamage() + " | DEF: " + m.getDefense());
            }
        }
        System.out.println();
    }

    private String createBar(int current, int max, int length, String filled, String empty) {
        if (max == 0) return empty.repeat(length);
        int filledLength = (int) ((double) current / max * length);
        filledLength = Math.max(0, Math.min(length, filledLength));
        return filled.repeat(filledLength) + empty.repeat(length - filledLength);
    }

    private void heroTurn() {
        for (Hero hero : heroes) {
            if (!hero.isAlive()) {
                continue;
            }

            System.out.println("\n*** " + hero.getName() + "'s turn ***");
            processHeroAction(hero);

            if (playerQuit) {
                return;
            }
        }
    }

    private void processHeroAction(Hero hero) {
        while (true) {
            System.out.println("\nChoose action:");
            System.out.println("1) Attack with weapon");
            System.out.println("2) Cast spell");
            System.out.println("3) Use potion");
            System.out.println("4) View stats (doesn't use turn)");
            System.out.println("Q) Quit game");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("Q")) {
                if (confirmQuit()) {
                    playerQuit = true;
                    return;
                }
                continue;
            }

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
                    System.out.println("[ERROR] Invalid choice!");
                    break;
            }
        }
    }

    private boolean confirmQuit() {
        System.out.println("\n[WARNING] Quit during battle?");
        System.out.print("Enter 'Y' to quit or 'N' to continue: ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

    private boolean heroAttack(Hero hero) {
        if (hero.getEquippedWeapon() == null) {
            System.out.println("[ERROR] No weapon equipped!");
            return false;
        }

        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }

        hero.attack(target);

        if (!target.isAlive()) {
            System.out.println("[VICTORY] " + target.getName() + " has been defeated!");
        }

        return true;
    }

    private boolean heroCastSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println("[ERROR] No spells in inventory!");
            return false;
        }

        System.out.println("\nAvailable spells (" + spells.size() + " total):");

        // 统计每种法术的数量
        java.util.Map<String, Integer> spellCounts = new java.util.HashMap<>();
        for (Spell s : spells) {
            String name = s.getName();
            spellCounts.put(name, spellCounts.getOrDefault(name, 0) + 1);
        }

        // 显示去重后的法术列表
        int index = 1;
        java.util.Set<String> displayed = new java.util.HashSet<>();
        for (Spell s : spells) {
            if (!displayed.contains(s.getName())) {
                int count = spellCounts.get(s.getName());
                System.out.println((index++) + ") " + s.getName() +
                        " (Damage: " + s.getBaseDamage() +
                        ", Mana: " + s.getManaCost() +
                        ") x" + count);
                displayed.add(s.getName());
            }
        }
        System.out.print("Select spell (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return false;
            if (choice < 1 || choice > displayed.size()) {
                System.out.println("[ERROR] Invalid choice!");
                return false;
            }

            // 获取实际的法术对象（取第一个匹配的）
            String selectedName = (String) displayed.toArray()[choice - 1];
            Spell spell = null;
            for (Spell s : spells) {
                if (s.getName().equals(selectedName)) {
                    spell = s;
                    break;
                }
            }

            if (spell == null) {
                System.out.println("[ERROR] Spell not found!");
                return false;
            }

            if (hero.getMana() < spell.getManaCost()) {
                System.out.println("[ERROR] Not enough mana! (Need: " + spell.getManaCost() +
                        ", Have: " + hero.getMana() + ")");
                return false;
            }

            Monster target = selectMonsterTarget();
            if (target == null) {
                return false;
            }

            hero.castSpell(spell, target);

            if (!target.isAlive()) {
                System.out.println("[VICTORY] " + target.getName() + " has been defeated!");
            }

            return true;

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
            return false;
        }
    }

    private boolean heroUsePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("[ERROR] No potions in inventory!");
            return false;
        }

        System.out.println("\nAvailable potions:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.println((i + 1) + ") " + p.getName() +
                    " (Effect: +" + p.getEffectAmount() +
                    " to " + p.getAffectedAttributes() + ")");
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

    private Monster selectMonsterTarget() {
        List<Monster> aliveMonsters = monsters.stream()
                .filter(Monster::isAlive)
                .toList();

        if (aliveMonsters.isEmpty()) {
            return null;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < aliveMonsters.size(); i++) {
            Monster m = aliveMonsters.get(i);
            System.out.println((i + 1) + ") " + m.getName() +
                    " (HP: " + m.getCurrentHP() + "/" + m.getMaxHP() + ")");
        }
        System.out.print("Target (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return null;
            if (choice >= 1 && choice <= aliveMonsters.size()) {
                return aliveMonsters.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
        }

        return null;
    }

    private void monsterTurn() {
        System.out.println("\n=== MONSTERS' TURN ===");

        for (Monster monster : monsters) {
            if (!monster.isAlive()) {
                continue;
            }

            List<Hero> aliveHeroes = heroes.stream()
                    .filter(Hero::isAlive)
                    .toList();

            if (!aliveHeroes.isEmpty()) {
                Hero target = aliveHeroes.get((int) (Math.random() * aliveHeroes.size()));
                monster.attack(target);

                if (!target.isAlive()) {
                    System.out.println("[DEFEAT] " + target.getName() + " has fainted!");
                }
            }
        }
    }

    private void endOfRoundRegen() {
        System.out.println("\n--- End of Round Regeneration ---");
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                int oldHP = hero.getHP();
                int oldMana = hero.getMana();
                hero.regen();
                int hpGain = hero.getHP() - oldHP;
                int manaGain = hero.getMana() - oldMana;
                if (hpGain > 0 || manaGain > 0) {
                    System.out.println(hero.getName() + " regenerated +" + hpGain + " HP, +" + manaGain + " Mana");
                }
            }
        }
    }

    private boolean hasAliveHeroes() {
        return heroes.stream().anyMatch(Hero::isAlive);
    }

    private boolean hasAliveMonsters() {
        return monsters.stream().anyMatch(Monster::isAlive);
    }
}