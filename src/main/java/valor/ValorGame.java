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
 * MOBA-style gameplay with 3 lanes.
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
     * Print welcome message.
     */
    private void printWelcome() {
        System.out.println("\n========================================");
        System.out.println("  LEGENDS OF VALOR");
        System.out.println("  MOBA-Style Battle Arena");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Objective: Reach the enemy Nexus!");
        System.out.println("  - You control 3 heroes in 3 lanes");
        System.out.println("  - Defeat monsters and push forward");
        System.out.println("  - Win by reaching row 0 (Monster Nexus)");
        System.out.println("  - Lose if any monster reaches row 7 (Hero Nexus)");
        System.out.println();
        System.out.print("Press Enter to begin...");
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

        // Create initial monsters
        monsters = new ArrayList<>();
        spawnNewMonsters();

        System.out.println("\n[GAME START] All heroes and monsters are in position!");
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

        String[] laneNames = {"Top", "Mid", "Bot"};

        for (int i = 0; i < 3; i++) {
            System.out.println("\n=== Select Hero for " + laneNames[i] + " Lane ===");
            System.out.println("1) Choose a Warrior");
            System.out.println("2) Choose a Paladin");
            System.out.println("3) Choose a Sorcerer");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();
            Hero hero = null;

            switch (choice) {
                case "1":
                    if (!warriors.isEmpty()) {
                        hero = warriors.remove(0);
                    }
                    break;
                case "2":
                    if (!paladins.isEmpty()) {
                        hero = paladins.remove(0);
                    }
                    break;
                case "3":
                    if (!sorcerers.isEmpty()) {
                        hero = sorcerers.remove(0);
                    }
                    break;
            }

            if (hero != null) {
                selected.add(hero);
                System.out.println("[SUCCESS] " + hero.getName() + " selected for " + laneNames[i] + " lane!");
            } else {
                System.out.println("[ERROR] Invalid choice, selecting default...");
                selected.add(new Warrior("DefaultWarrior" + i, 100, 700, 500, 600, 1000, 0));
            }
        }

        return selected;
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        while (!gameOver) {
            currentRound++;
            System.out.println("\n========================================");
            System.out.println("  ROUND " + currentRound);
            System.out.println("========================================");

            // Display map
            map.display(heroes, monsters);

            // Heroes' turn
            heroTurn();

            // Check win condition
            if (map.anyHeroReachedMonsterNexus(heroes)) {
                System.out.println("\n*** HEROES WIN! ***");
                System.out.println("A hero has reached the Monster Nexus!");
                gameOver = true;
                break;
            }

            // Monsters' turn
            ValorMonsterAI.processMonsters(map, monsters, heroes);

            // Check lose condition
            if (map.anyMonsterReachedHeroNexus(monsters)) {
                System.out.println("\n*** MONSTERS WIN! ***");
                System.out.println("A monster has reached your Nexus!");
                gameOver = true;
                break;
            }

            // End of round
            endOfRound();

            // Spawn new monsters every 8 rounds
            if (currentRound % MONSTER_SPAWN_INTERVAL == 0) {
                System.out.println("\n[SPAWN] New monsters have appeared!");
                spawnNewMonsters();
            }
        }
    }

    /**
     * Process all heroes' turns.
     */
    private void heroTurn() {
        for (Hero hero : heroes) {
            if (!hero.isAlive()) {
                continue;
            }

            System.out.println("\n--- " + hero.getName() + "'s Turn (Lane " + (hero.getLaneIndex() + 1) + ") ---");
            System.out.println("Position: (" + hero.getRow() + "," + hero.getCol() + ")");
            System.out.println("HP: " + hero.getHP() + "/" + hero.getMaxHP() +
                    " | Mana: " + hero.getMana() + "/" + hero.getMaxMana());

            boolean actionTaken = false;
            while (!actionTaken) {
                actionTaken = processHeroAction(hero);
            }
        }
    }

    /**
     * Process a single hero's action.
     */
    private boolean processHeroAction(Hero hero) {
        System.out.println("\nChoose action:");
        System.out.println("1) Move (W/A/S/D)");
        System.out.println("2) Attack");
        System.out.println("3) Cast Spell");
        System.out.println("4) Use Potion");
        System.out.println("5) Teleport");
        System.out.println("6) Recall (return to Nexus)");
        System.out.println("7) View Map");
        System.out.println("8) Enter Market (if at Nexus)");
        System.out.println("Q) Quit Game");
        System.out.print("Choice: ");

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
                return ValorActions.teleport(scanner, map, hero, heroes, monsters);
            case "6":
                return ValorActions.recall(map, hero);
            case "7":
                map.display(heroes, monsters);
                return false; // Don't consume turn
            case "8":
                if (hero.getRow() == 7) {
                    Market market = new Market(scanner);
                    market.enter(hero);
                } else {
                    System.out.println("[ERROR] Not at Nexus!");
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
     * End of round processing.
     */
    private void endOfRound() {
        System.out.println("\n--- End of Round " + currentRound + " ---");

        // Regenerate heroes
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
            } else {
                // Respawn fallen heroes
                map.respawnHeroAtNexus(hero);
                hero.reviveAtHalfHP();
                System.out.println(hero.getName() + " has respawned at Nexus!");
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
            for (Monster m : defeated) {
                int gold = 500 * m.getLevel();
                int exp = 2 * m.getLevel();

                System.out.println("\n[REWARD] Defeated " + m.getName() + "!");
                for (Hero h : heroes) {
                    h.gainGold(gold);
                    h.gainExperience(exp);
                }
            }

            // Remove defeated monsters
            monsters.removeAll(defeated);
        }
    }

    /**
     * Spawn 3 new monsters (one per lane).
     */
    private void spawnNewMonsters() {
        // Find highest hero level
        int highestLevel = 1;
        for (Hero h : heroes) {
            if (h.getLevel() > highestLevel) {
                highestLevel = h.getLevel();
            }
        }

        // Load monster data
        List<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(loadDragons());
        allMonsters.addAll(loadSpirits());
        allMonsters.addAll(loadExoskeletons());

        // Create 3 monsters
        List<Monster> newMonsters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Monster m = allMonsters.get((int)(Math.random() * allMonsters.size()));
            newMonsters.add(m);
        }

        map.spawnMonsters(newMonsters);
        monsters.addAll(newMonsters);
    }

    /**
     * Confirm quit.
     */
    private boolean confirmQuit() {
        System.out.print("\nAre you sure you want to quit? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y");
    }

    /**
     * Print game over message.
     */
    private void printGameOver() {
        System.out.println("\n========================================");
        System.out.println("  GAME OVER");
        System.out.println("========================================");
        System.out.println("Total rounds played: " + currentRound);
        System.out.println("\nFinal Hero Stats:");
        for (Hero h : heroes) {
            System.out.println("- " + h.getName() + ": Level " + h.getLevel() +
                    ", Gold: " + h.getMoney());
        }
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
}
