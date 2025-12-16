package game;

import map.GameMap;
import character.hero.Hero;
import character.hero.Warrior;
import character.hero.Paladin;
import character.hero.Sorcerer;
import character.monster.Monster;
import character.monster.Dragon;
import character.monster.Spirit;
import character.monster.Exoskeleton;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * Main game controller for Legends: Monsters and Heroes.
 * Manages game initialization, hero selection, and main game loop.
 */
public class Game {
    private static final int MAP_SIZE = 8;
    private static final double BATTLE_CHANCE = 0.3; // 30% chance of battle on common cell

    private GameMap gameMap;
    private List<Hero> heroes;
    private Scanner scanner;
    private Random random;
    private boolean gameOver;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.gameOver = false;
    }

    /**
     * Start the game.
     */
    public void start() {
        printWelcome();
        printInstructions();
        setupHeroes();
        setupMap();
        mainGameLoop();
        scanner.close();
    }

    /**
     * Print welcome message.
     */
    private void printWelcome() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  LEGENDS: MONSTERS AND HEROES                  ║");
        System.out.println("║  An Epic RPG Adventure                         ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Print game instructions.
     */
    private void printInstructions() {
        System.out.println("=== GAME CONTROLS ===");
        System.out.println("W/w - Move Up");
        System.out.println("A/a - Move Left");
        System.out.println("S/s - Move Down");
        System.out.println("D/d - Move Right");
        System.out.println("I/i - Show Hero Information");
        System.out.println("M/m - Enter Market (when on Market cell)");
        System.out.println("Q/q - Quit Game");
        System.out.println();
        System.out.println("Map Legend:");
        System.out.println("H - Your heroes");
        System.out.println("C - Common cell (battles may occur)");
        System.out.println("M - Market cell (buy/sell items)");
        System.out.println("X - Inaccessible cell");
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    /**
     * Setup heroes - allow player to select 1-3 heroes.
     */
    private void setupHeroes() {
        heroes = new ArrayList<>();

        // Load available heroes from data files
        List<Hero> availableWarriors = loadWarriors();
        List<Hero> availablePaladins = loadPaladins();
        List<Hero> availableSorcerers = loadSorcerers();

        System.out.println("=== HERO SELECTION ===");
        System.out.println("Choose 1-3 heroes for your party.");
        System.out.println();

        while (heroes.size() < 3) {
            System.out.println("Select Hero #" + (heroes.size() + 1) + ":");
            System.out.println("1) Choose a Warrior");
            System.out.println("2) Choose a Paladin");
            System.out.println("3) Choose a Sorcerer");
            if (heroes.size() > 0) {
                System.out.println("D) Done selecting heroes");
            }
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("D") && heroes.size() > 0) {
                break;
            }

            Hero selected = null;
            switch (choice) {
                case "1":
                    selected = selectHeroFromList(availableWarriors, "Warrior");
                    break;
                case "2":
                    selected = selectHeroFromList(availablePaladins, "Paladin");
                    break;
                case "3":
                    selected = selectHeroFromList(availableSorcerers, "Sorcerer");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }

            if (selected != null) {
                heroes.add(selected);
                System.out.println(selected.getName() + " joined your party!");
                System.out.println();
            }
        }

        System.out.println("Your party is ready!");
        for (Hero h : heroes) {
            System.out.println("- " + h);
        }
        System.out.println();
    }

    /**
     * Let player select a specific hero from a list.
     */
    private Hero selectHeroFromList(List<Hero> heroList, String type) {
        System.out.println("\nAvailable " + type + "s:");
        for (int i = 0; i < heroList.size(); i++) {
            System.out.println((i + 1) + ") " + heroList.get(i));
        }
        System.out.print("Select (1-" + heroList.size() + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= heroList.size()) {
                return heroList.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
        return null;
    }

    /**
     * Setup the game map.
     */
    private void setupMap() {
        gameMap = new GameMap(MAP_SIZE, MAP_SIZE, heroes.size());

        // Place heroes at starting positions (top row)
        for (int i = 0; i < heroes.size(); i++) {
            gameMap.setHeroPosition(i, 0, i * 2);
        }
    }

    /**
     * Main game loop.
     */
    private void mainGameLoop() {
        while (!gameOver) {
            // Display map
            gameMap.displayMap();
            System.out.println();

            // Process each hero's turn
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);

                if (!hero.isAlive()) {
                    continue;
                }

                System.out.println("=== " + hero.getName() + "'s Turn ===");
                processTurn(hero, i);

                if (gameOver) {
                    break;
                }
            }
        }

        System.out.println("Game Over! Thanks for playing!");
    }

    /**
     * Process a single hero's turn.
     */
    private void processTurn(Hero hero, int heroIndex) {
        System.out.print("Action (W/A/S/D to move, I for info, M for market, Q to quit): ");
        String action = scanner.nextLine().trim().toUpperCase();

        int[] currentPos = gameMap.getHeroPosition(heroIndex);
        int x = currentPos[0];
        int y = currentPos[1];

        switch (action) {
            case "W": // Move up
                attemptMove(hero, heroIndex, x - 1, y);
                break;
            case "S": // Move down
                attemptMove(hero, heroIndex, x + 1, y);
                break;
            case "A": // Move left
                attemptMove(hero, heroIndex, x, y - 1);
                break;
            case "D": // Move right
                attemptMove(hero, heroIndex, x, y + 1);
                break;
            case "I": // Show info
                showHeroInfo();
                processTurn(hero, heroIndex); // Don't consume turn
                break;
            case "M": // Enter market
                if (gameMap.getCell(x, y).isMarket()) {
                    Market market = new Market(scanner);
                    market.enter(hero);
                } else {
                    System.out.println("You're not on a market cell!");
                }
                processTurn(hero, heroIndex); // Don't consume turn
                break;
            case "Q": // Quit
                gameOver = true;
                break;
            default:
                System.out.println("Invalid action!");
                processTurn(hero, heroIndex); // Retry
                break;
        }
    }

    /**
     * Attempt to move hero to new position.
     */
    private void attemptMove(Hero hero, int heroIndex, int newX, int newY) {
        if (!gameMap.inBounds(newX, newY)) {
            System.out.println("Can't move outside the map!");
            return;
        }

        if (!gameMap.getCell(newX, newY).isAccessible()) {
            System.out.println("That cell is inaccessible!");
            return;
        }

        // Move successful
        gameMap.setHeroPosition(heroIndex, newX, newY);
        System.out.println(hero.getName() + " moved successfully!");

        // Check for battle on common cells
        if (!gameMap.getCell(newX, newY).isMarket()) {
            if (random.nextDouble() < BATTLE_CHANCE) {
                initiateBattle();
            }
        }
    }

    /**
     * Show information about all heroes.
     */
    private void showHeroInfo() {
        System.out.println("\n=== HERO PARTY INFO ===");
        for (Hero hero : heroes) {
            System.out.println(hero);
            System.out.println("  Equipped Weapon: " +
                    (hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getName() : "None"));
            System.out.println("  Equipped Armor: " +
                    (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
            System.out.println();
        }
    }

    /**
     * Initiate a battle with monsters.
     */
    private void initiateBattle() {
        System.out.println("\n*** BATTLE STARTED! ***\n");

        // Create monsters matching hero count and level
        List<Monster> monsters = generateMonsters();

        BattleManager battle = new BattleManager(scanner, new ArrayList<>(heroes), monsters);
        boolean victory = battle.fight();

        if (victory) {
            System.out.println("\n*** VICTORY! ***");
            // Award gold and experience
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    int goldReward = monsters.get(0).getLevel() * 100;
                    int expReward = monsters.size() * 2;
                    hero.gainGold(goldReward);
                    hero.gainExperience(expReward);
                }
            }

            // Revive fainted heroes
            for (Hero hero : heroes) {
                if (!hero.isAlive()) {
                    hero.revive();
                }
            }
        } else {
            System.out.println("\n*** DEFEAT! ***");
            System.out.println("All heroes have fallen! Game Over!");
            gameOver = true;
        }
    }

    /**
     * Generate monsters for battle.
     */
    private List<Monster> generateMonsters() {
        List<Monster> monsters = new ArrayList<>();

        // Find highest level hero
        int highestLevel = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > highestLevel) {
                highestLevel = hero.getLevel();
            }
        }

        // Load monster data
        List<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(loadDragons());
        allMonsters.addAll(loadSpirits());
        allMonsters.addAll(loadExoskeletons());

        // Select random monsters matching hero count
        for (int i = 0; i < heroes.size(); i++) {
            Monster selected = allMonsters.get(random.nextInt(allMonsters.size()));
            monsters.add(selected);
        }

        return monsters;
    }

    // ===== Data Loading Methods =====

    private List<Hero> loadWarriors() {
        List<Hero> warriors = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Warriors.txt");
        for (String[] row : data) {
            warriors.add(new Warrior(
                    row[0], // name
                    Integer.parseInt(row[1]), // mana
                    Integer.parseInt(row[2]), // strength
                    Integer.parseInt(row[3]), // agility
                    Integer.parseInt(row[4]), // dexterity
                    Integer.parseInt(row[5]), // money
                    Integer.parseInt(row[6])  // experience
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
                    row[0], // name
                    Integer.parseInt(row[1]), // level
                    Integer.parseInt(row[2]), // damage
                    Integer.parseInt(row[3]), // defense
                    Double.parseDouble(row[4]) // dodge
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
