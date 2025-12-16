package valor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import character.hero.Hero;
import character.hero.Warrior;
import character.hero.Paladin;
import character.hero.Sorcerer;
import character.monster.Monster;
import character.monster.Dragon;
import character.monster.Spirit;
import character.monster.Exoskeleton;
import util.FileLoader;
import game.Market;

/**
 * Main game controller for Legends of Valor.
 * MOBA-style gameplay with 3 lanes, matching PDF specifications.
 */
public class ValorGame {
    private static final int MONSTER_SPAWN_INTERVAL = 8; // Spawn new monsters every 8 rounds

    private Scanner scanner;
    private ValorMap map;
    private List<Hero> heroes;
    private List<Monster> monsters;
    private int currentRound;
    private boolean gameOver;

    public ValorGame() {
        this.scanner = new Scanner(System.in);
        this.currentRound = 0;
        this.gameOver = false;
    }

    /**
     * Start the game.
     */
    public void start() {
        printWelcome();
        setupGame();
        gameLoop();
        printGameOver();
    }

    /**
     * Print welcome message and rules.
     */
    private void printWelcome() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║       LEGENDS OF VALOR                         ║");
        System.out.println("║       MOBA-Style Battle Arena                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("═══════════════ GAME OBJECTIVE ═══════════════");
        System.out.println("• You control 3 heroes across 3 lanes");
        System.out.println("• WIN: Any hero reaches Monster Nexus (Row 0)");
        System.out.println("• LOSE: Any monster reaches Hero Nexus (Row 7)");
        System.out.println();
        System.out.println("═══════════════ GAME MECHANICS ═══════════════");
        System.out.println("• Heroes spawn at Row 7 (Hero Nexus)");
        System.out.println("• Monsters spawn at Row 0 (Monster Nexus)");
        System.out.println("• New monsters spawn every 8 rounds");
        System.out.println("• Dead heroes respawn at their Nexus next round");
        System.out.println("• Heroes regenerate 10% HP/Mana each round");
        System.out.println();
        System.out.println("═══════════════ TERRAIN EFFECTS ══════════════");
        System.out.println("• Bush (B): +10% Dexterity");
        System.out.println("• Cave (C): +10% Agility");
        System.out.println("• Koulou (K): +10% Strength");
        System.out.println("• Plain (P): No bonus");
        System.out.println("• Wall (I): Impassable");
        System.out.println();
        System.out.println("═══════════════ MOVEMENT RULES ═══════════════");
        System.out.println("• Cannot move through walls (columns 2, 5)");
        System.out.println("• Cannot share cell with another hero");
        System.out.println("• Cannot move behind monsters (must kill first)");
        System.out.println("• Attack range: Current + adjacent cells");
        System.out.println();
        System.out.print("Press Enter to begin hero selection...");
        scanner.nextLine();
    }

    /**
     * Setup the game: map, heroes, and initial monsters.
     */
    private void setupGame() {
        // Create map
        map = new ValorMap();

        // Select heroes
        heroes = selectHeroes();
        map.placeInitialHeroes(heroes);

        // Create initial monsters (3, one per lane)
        monsters = new ArrayList<>();
        spawnNewMonsters();

        System.out.println("\n[GAME START] All heroes and monsters are in position!");
        System.out.println("Each hero has been equipped with powerful starter gear!");
        System.out.print("\nPress Enter to view the battlefield...");
        scanner.nextLine();
    }

    /**
     * Let player select 3 heroes (one per lane).
     */
    private List<Hero> selectHeroes() {
        List<Hero> selected = new ArrayList<>();

        // Load available heroes
        List<Hero> warriors = loadWarriors();
        List<Hero> paladins = loadPaladins();
        List<Hero> sorcerers = loadSorcerers();

        String[] laneNames = {"Top Lane (Columns 0-1)", "Mid Lane (Columns 3-4)", "Bot Lane (Columns 6-7)"};

        for (int i = 0; i < 3; i++) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║  SELECT HERO FOR " + laneNames[i].toUpperCase());
            System.out.println("╚════════════════════════════════════════════╝");

            System.out.println("\n1) Warrior - High Strength & Agility");
            System.out.println("   Favored stats: Strength +10%, Agility +10% per level");

            System.out.println("\n2) Paladin - Balanced Fighter");
            System.out.println("   Favored stats: Strength +10%, Dexterity +10% per level");

            System.out.println("\n3) Sorcerer - Powerful Magic User");
            System.out.println("   Favored stats: Dexterity +10%, Agility +10% per level");

            System.out.print("\nChoice (1-3): ");

            String choice = scanner.nextLine().trim();
            Hero hero = null;

            switch (choice) {
                case "1":
                    if (!warriors.isEmpty()) {
                        hero = selectSpecificHero(warriors, "Warrior");
                    }
                    break;
                case "2":
                    if (!paladins.isEmpty()) {
                        hero = selectSpecificHero(paladins, "Paladin");
                    }
                    break;
                case "3":
                    if (!sorcerers.isEmpty()) {
                        hero = selectSpecificHero(sorcerers, "Sorcerer");
                    }
                    break;
                default:
                    System.out.println("[WARNING] Invalid choice!");
            }

            if (hero != null) {
                selected.add(hero);
                System.out.println("\n[SUCCESS] " + hero.getName() + " selected for " + laneNames[i] + "!");
            } else {
                System.out.println("[INFO] Selecting default warrior...");
                selected.add(new Warrior("DefaultWarrior" + (i+1), 100, 700, 500, 600, 1000, 0));
            }
        }

        return selected;
    }

    /**
     * Select a specific hero from a list.
     */
    private Hero selectSpecificHero(List<Hero> heroList, String type) {
        System.out.println("\nAvailable " + type + "s:");
        for (int i = 0; i < Math.min(heroList.size(), 5); i++) {
            Hero h = heroList.get(i);
            System.out.println((i + 1) + ") " + h.getName() +
                    " (STR:" + h.getStrength() +
                    " DEX:" + h.getDexterity() +
                    " AGI:" + h.getAgility() +
                    " MANA:" + h.getMaxMana() + ")");
        }

        System.out.print("Select (1-" + Math.min(heroList.size(), 5) + ") or 0 for first: ");
        String input = scanner.nextLine().trim();

        try {
            int choice = input.equals("0") ? 1 : Integer.parseInt(input);
            if (choice >= 1 && choice <= Math.min(heroList.size(), 5)) {
                return heroList.remove(choice - 1);
            }
        } catch (NumberFormatException e) {
            // Fall through to return first
        }

        return heroList.isEmpty() ? null : heroList.remove(0);
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        while (!gameOver) {
            currentRound++;
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║           ROUND " + currentRound);
            System.out.println("╚════════════════════════════════════════════════╝");

            // Display map
            map.display(heroes, monsters);

            // Display hero status summary
            displayHeroStatusSummary();

            // Heroes' turn
            heroTurn();

            if (gameOver) break;

            // Check win condition
            if (map.anyHeroReachedMonsterNexus(heroes)) {
                System.out.println("\n╔════════════════════════════════════════════════╗");
                System.out.println("║           *** HEROES WIN! ***                  ║");
                System.out.println("╚════════════════════════════════════════════════╝");
                System.out.println("A hero has reached the Monster Nexus!");
                gameOver = true;
                break;
            }

            // Monsters' turn
            ValorMonsterAI.processMonsters(map, monsters, heroes);

            // Check lose condition
            if (map.anyMonsterReachedHeroNexus(monsters)) {
                System.out.println("\n╔════════════════════════════════════════════════╗");
                System.out.println("║           *** MONSTERS WIN! ***                ║");
                System.out.println("╚════════════════════════════════════════════════╝");
                System.out.println("A monster has reached your Nexus!");
                gameOver = true;
                break;
            }

            // End of round
            endOfRound();

            // Spawn new monsters every 8 rounds
            if (currentRound % MONSTER_SPAWN_INTERVAL == 0) {
                System.out.println("\n[SPAWN] New wave of monsters has appeared!");
                spawnNewMonsters();
            }
        }
    }

    /**
     * Display hero status summary.
     */
    private void displayHeroStatusSummary() {
        System.out.println("\n═══════════════ HERO STATUS ═══════════════");
        for (Hero h : heroes) {
            String status = h.isAlive() ? "ALIVE" : "FALLEN";
            String lane = "Lane " + (h.getLaneIndex() + 1);
            String pos = "(" + h.getRow() + "," + h.getCol() + ")";
            String hpBar = createProgressBar(h.getHP(), h.getMaxHP(), 10);

            System.out.println(h.getName() + " [" + status + "] " + lane + " " + pos);
            System.out.println("  HP:[" + hpBar + "] " + h.getHP() + "/" + h.getMaxHP() +
                    " | MP:" + h.getMana() + "/" + h.getMaxMana() +
                    " | Lvl:" + h.getLevel());
        }
        System.out.println();
    }

    /**
     * Create a simple progress bar.
     */
    private String createProgressBar(int current, int max, int length) {
        if (max == 0) return "=".repeat(length);
        int filled = (int)((double)current / max * length);
        filled = Math.max(0, Math.min(length, filled));
        return "=".repeat(filled) + "-".repeat(length - filled);
    }

    /**
     * Process all heroes' turns.
     */
    private void heroTurn() {
        System.out.println("═══════════════ HEROES' TURN ═══════════════");

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);

            if (!hero.isAlive()) {
                System.out.println("\n[SKIP] " + hero.getName() + " is fallen (will respawn next round)");
                continue;
            }

            System.out.println("\n─────────────────────────────────────────");
            System.out.println("  " + hero.getName() + "'s Turn");
            System.out.println("  Lane " + (hero.getLaneIndex() + 1) + " | Position: (" + hero.getRow() + "," + hero.getCol() + ")");
            System.out.println("  HP: " + hero.getHP() + "/" + hero.getMaxHP() +
                    " | Mana: " + hero.getMana() + "/" + hero.getMaxMana());
            System.out.println("─────────────────────────────────────────");

            boolean actionTaken = false;
            while (!actionTaken && !gameOver) {
                actionTaken = processHeroAction(hero);
            }
        }
    }

    /**
     * Process a single hero's action.
     */
    private boolean processHeroAction(Hero hero) {
        System.out.println("\nAvailable Actions:");
        System.out.println("1) Move (W/A/S/D)");
        System.out.println("2) Attack");
        System.out.println("3) Cast Spell");
        System.out.println("4) Use Potion");
        System.out.println("5) Change Weapon");
        System.out.println("6) Change Armor");
        System.out.println("7) Teleport to Another Lane");
        System.out.println("8) Recall to Nexus");
        System.out.println("9) Pass Turn");
        System.out.println("---");
        System.out.println("I) View Hero Info");
        System.out.println("M) View Map");
        System.out.println("S) Enter Market (if at Nexus)");
        System.out.println("Q) Quit Game");
        System.out.print("\nChoice: ");

        String choice = scanner.nextLine().trim().toUpperCase();

        switch (choice) {
            case "1":
                return ValorActions.moveHero(scanner, map, hero, heroes, monsters);
            case "2":
                return ValorActions.attack(scanner, map, hero, monsters);
            case "3":
                return ValorActions.castSpell(scanner, map, hero, monsters);
            case "4":
                return ValorActions.usePotion(scanner, hero);
            case "5":
                return ValorActions.changeWeapon(scanner, hero);
            case "6":
                return ValorActions.changeArmor(scanner, hero);
            case "7":
                return ValorActions.teleport(scanner, map, hero, heroes, monsters);
            case "8":
                return ValorActions.recall(map, hero);
            case "9":
                return ValorActions.passTurn(hero);
            case "I":
                displayDetailedHeroInfo(hero);
                return false; // Don't consume turn
            case "M":
                map.display(heroes, monsters);
                return false; // Don't consume turn
            case "S":
                if (hero.getRow() == 7) { // At hero nexus
                    Market market = new Market(scanner);
                    market.enter(hero);
                } else {
                    System.out.println("[ERROR] Not at Nexus! (Must be at row 7)");
                }
                return false; // Don't consume turn
            case "Q":
                if (confirmQuit()) {
                    gameOver = true;
                    return true;
                }
                return false;
            default:
                System.out.println("[ERROR] Invalid choice!");
                return false;
        }
    }

    /**
     * Display detailed hero information.
     */
    private void displayDetailedHeroInfo(Hero hero) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  " + hero.getName() + " - " + hero.getHeroClass());
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("Level: " + hero.getLevel() + " | EXP: " + hero.getExperience());
        System.out.println("Gold: " + hero.getMoney());
        System.out.println("\nStats:");
        System.out.println("  HP: " + hero.getHP() + "/" + hero.getMaxHP());
        System.out.println("  Mana: " + hero.getMana() + "/" + hero.getMaxMana());
        System.out.println("  Strength: " + hero.getStrength());
        System.out.println("  Dexterity: " + hero.getDexterity());
        System.out.println("  Agility: " + hero.getAgility());
        System.out.println("\nEquipment:");
        System.out.println("  Weapon: " + (hero.getEquippedWeapon() != null ?
                hero.getEquippedWeapon().getName() : "None"));
        System.out.println("  Armor: " + (hero.getEquippedArmor() != null ?
                hero.getEquippedArmor().getName() : "None"));
        System.out.println("\nInventory:");
        System.out.println("  Weapons: " + hero.getInventory().getWeapons().size());
        System.out.println("  Armors: " + hero.getInventory().getArmors().size());
        System.out.println("  Potions: " + hero.getInventory().getPotions().size());
        System.out.println("  Spells: " + hero.getInventory().getSpells().size());
        System.out.println();
    }

    /**
     * End of round processing.
     */
    private void endOfRound() {
        System.out.println("\n═══════════════ END OF ROUND " + currentRound + " ═══════════════");

        // Regenerate heroes (10% HP and Mana)
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                int oldHP = hero.getHP();
                int oldMana = hero.getMana();
                hero.regen();
                int hpGain = hero.getHP() - oldHP;
                int manaGain = hero.getMana() - oldMana;
                if (hpGain > 0 || manaGain > 0) {
                    System.out.println("[REGEN] " + hero.getName() +
                            ": +" + hpGain + " HP, +" + manaGain + " Mana");
                }
            } else {
                // Respawn fallen heroes at full HP and full MP
                map.respawnHeroAtNexus(hero);
                hero.setHP(hero.getMaxHP());
                hero.setMana(hero.getMaxMana());
                System.out.println("[RESPAWN] " + hero.getName() +
                        " has respawned at Nexus (Lane " + (hero.getLaneIndex() + 1) + ")!");
            }
        }

        // Award gold and exp for defeated monsters
        List<Monster> defeated = new ArrayList<>();
        for (Monster m : monsters) {
            if (!m.isAlive()) {
                defeated.add(m);
            }
        }

        if (!defeated.isEmpty()) {
            System.out.println("\n[REWARDS] Monsters defeated this round:");
            for (Monster m : defeated) {
                int gold = 500 * m.getLevel();
                int exp = 2 * m.getLevel();

                System.out.println("  • " + m.getName() + " (Level " + m.getLevel() + ")");
                System.out.println("    All heroes gain: " + gold + " gold, " + exp + " exp");

                for (Hero h : heroes) {
                    h.gainGold(gold);
                    h.gainExperience(exp);
                }
            }

            // Remove defeated monsters
            monsters.removeAll(defeated);
        }

        System.out.println("\n[INFO] Round " + currentRound + " complete.");
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Spawn 3 new monsters (one per lane).
     * Monster level equals highest hero level.
     */
    private void spawnNewMonsters() {
        // Find highest hero level
        int highestLevel = 1;
        for (Hero h : heroes) {
            if (h.getLevel() > highestLevel) {
                highestLevel = h.getLevel();
            }
        }

        // Load monster pools
        List<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(loadDragons());
        allMonsters.addAll(loadSpirits());
        allMonsters.addAll(loadExoskeletons());

        // Filter monsters near the highest level (within 2 levels)
        List<Monster> suitableMonsters = new ArrayList<>();
        for (Monster m : allMonsters) {
            if (Math.abs(m.getLevel() - highestLevel) <= 2) {
                suitableMonsters.add(m);
            }
        }

        if (suitableMonsters.isEmpty()) {
            suitableMonsters = allMonsters; // Fall back to all
        }

        // Create 3 monsters (one per lane)
        List<Monster> newMonsters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int)(Math.random() * suitableMonsters.size());
            Monster template = suitableMonsters.get(randomIndex);

            // Create a copy of the monster
            Monster newMonster;
            if (template instanceof Dragon) {
                newMonster = new Dragon(template.getName(), template.getLevel(),
                        template.getBaseDamage(), template.getDefense(), template.getDodgeChance());
            } else if (template instanceof Spirit) {
                newMonster = new Spirit(template.getName(), template.getLevel(),
                        template.getBaseDamage(), template.getDefense(), template.getDodgeChance());
            } else {
                newMonster = new Exoskeleton(template.getName(), template.getLevel(),
                        template.getBaseDamage(), template.getDefense(), template.getDodgeChance());
            }

            newMonsters.add(newMonster);
            System.out.println("  [Lane " + (i + 1) + "] " + newMonster.getName() +
                    " (Level " + newMonster.getLevel() + ")");
        }

        map.spawnMonsters(newMonsters);
        monsters.addAll(newMonsters);
    }

    /**
     * Confirm quit.
     */
    private boolean confirmQuit() {
        System.out.print("\n[WARNING] Are you sure you want to quit? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

    /**
     * Print game over message.
     */
    private void printGameOver() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           GAME OVER                            ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("\nGame Statistics:");
        System.out.println("  Total Rounds: " + currentRound);
        System.out.println("\nFinal Hero Status:");
        for (Hero h : heroes) {
            System.out.println("  • " + h.getName() + " (Lane " + (h.getLaneIndex() + 1) + ")");
            System.out.println("    Level: " + h.getLevel() + " | Gold: " + h.getMoney() +
                    " | HP: " + h.getHP() + "/" + h.getMaxHP());
        }
        System.out.println("\nThank you for playing Legends of Valor!");
    }

    // === Data Loading Methods ===

    private List<Hero> loadWarriors() {
        List<Hero> warriors = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Warriors.txt");
        for (String[] row : data) {
            warriors.add(new Warrior(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4]),
                    Integer.parseInt(row[5]), Integer.parseInt(row[6])
            ));
        }
        return warriors;
    }

    private List<Hero> loadPaladins() {
        List<Hero> paladins = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Paladins.txt");
        for (String[] row : data) {
            paladins.add(new Paladin(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4]),
                    Integer.parseInt(row[5]), Integer.parseInt(row[6])
            ));
        }
        return paladins;
    }

    private List<Hero> loadSorcerers() {
        List<Hero> sorcerers = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Sorcerers.txt");
        for (String[] row : data) {
            sorcerers.add(new Sorcerer(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4]),
                    Integer.parseInt(row[5]), Integer.parseInt(row[6])
            ));
        }
        return sorcerers;
    }

    private List<Monster> loadDragons() {
        List<Monster> dragons = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Dragons.txt");
        for (String[] row : data) {
            dragons.add(new Dragon(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Double.parseDouble(row[4])
            ));
        }
        return dragons;
    }

    private List<Monster> loadSpirits() {
        List<Monster> spirits = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Spirits.txt");
        for (String[] row : data) {
            spirits.add(new Spirit(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Double.parseDouble(row[4])
            ));
        }
        return spirits;
    }

    private List<Monster> loadExoskeletons() {
        List<Monster> exos = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Exoskeletons.txt");
        for (String[] row : data) {
            exos.add(new Exoskeleton(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Double.parseDouble(row[4])
            ));
        }
        return exos;
    }

    /**
     * Helper method to set hero HP (for respawn).
     */
    private void setHeroHP(Hero hero, int hp) {
        // Access through package-private method
        while (hero.getHP() < hp) {
            hero.regen();
        }
    }

    /**
     * Helper method to set hero Mana (for respawn).
     */
    private void setHeroMana(Hero hero, int mana) {
        // Access through package-private method
        while (hero.getMana() < mana) {
            hero.regen();
        }
    }
}