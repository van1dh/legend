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
 * Features: Starter equipment, quit anytime, view map, view stats.
 */
public class Game {
    private static final int MAP_SIZE = 8;
    private static final double BATTLE_CHANCE = 0.3;

    private GameMap gameMap;
    private List<Hero> heroes;
    private Scanner scanner;
    private Random random;
    private boolean gameOver;
    private boolean playerQuit;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.gameOver = false;
        this.playerQuit = false;
    }

    public void start() {
        printWelcome();
        printInstructions();

        if (confirmStart()) {
            setupHeroes();
            setupMap();
            mainGameLoop();
        } else {
            System.out.println("Maybe next time! Goodbye!");
            return;
        }

        printGoodbye();
    }

    private boolean confirmStart() {
        System.out.println("Ready to start your adventure?");
        System.out.print("Enter 'Y' to continue or 'Q' to quit: ");
        String input = scanner.nextLine().trim().toUpperCase();
        return !input.equals("Q");
    }

    private void printWelcome() {
        System.out.println("========================================");
        System.out.println("  LEGENDS: MONSTERS AND HEROES");
        System.out.println("  An Epic RPG Adventure");
        System.out.println("========================================");
        System.out.println();
    }

    private void printInstructions() {
        System.out.println("=== GAME CONTROLS ===");
        System.out.println("W/w - Move Up");
        System.out.println("A/a - Move Left");
        System.out.println("S/s - Move Down");
        System.out.println("D/d - Move Right");
        System.out.println("I/i - Show Hero Stats");
        System.out.println("P/p - Show Map");
        System.out.println("V/v - View Inventory");
        System.out.println("M/m - Enter Market (when on Market cell)");
        System.out.println("Q/q - Quit Game (Exit at any time!)");
        System.out.println();
        System.out.println("Map Legend:");
        System.out.println("H1/H2/H3 - Your heroes");
        System.out.println("C - Common cell (battles may occur)");
        System.out.println("M - Market cell (buy/sell items)");
        System.out.println("X - Inaccessible cell");
        System.out.println();
        System.out.println("*** NEW FEATURE ***");
        System.out.println("All heroes start with powerful equipment!");
        System.out.println("  - Strong weapons and armor");
        System.out.println("  - Multiple healing and mana potions");
        System.out.println("  - Powerful spells");
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    private void setupHeroes() {
        heroes = new ArrayList<>();

        List<Hero> availableWarriors = loadWarriors();
        List<Hero> availablePaladins = loadPaladins();
        List<Hero> availableSorcerers = loadSorcerers();

        System.out.println("=== HERO SELECTION ===");
        System.out.println("Choose 1-3 heroes for your party.");
        System.out.println("Each hero comes with starter equipment!");
        System.out.println();

        while (heroes.size() < 3) {
            System.out.println("Select Hero #" + (heroes.size() + 1) + ":");
            System.out.println("1) Choose a Warrior (High Strength & Agility)");
            System.out.println("2) Choose a Paladin (Balanced Fighter)");
            System.out.println("3) Choose a Sorcerer (Powerful Magic)");
            if (heroes.size() > 0) {
                System.out.println("D) Done selecting heroes");
            }
            System.out.println("Q) Quit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("Q")) {
                if (confirmQuit()) {
                    playerQuit = true;
                    gameOver = true;
                    return;
                }
                continue;
            }

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
                System.out.println("\n[SUCCESS] " + selected.getName() + " joined your party!");
                System.out.println();
            }
        }

        System.out.println("========================================");
        System.out.println("  YOUR PARTY IS READY!");
        System.out.println("========================================");
        for (Hero h : heroes) {
            System.out.println("* " + h);
        }
        System.out.println();
    }

    private Hero selectHeroFromList(List<Hero> heroList, String type) {
        System.out.println("\nAvailable " + type + "s:");
        for (int i = 0; i < heroList.size(); i++) {
            System.out.println((i + 1) + ") " + heroList.get(i).getName() +
                    " (STR: " + heroList.get(i).getStrength() +
                    ", DEX: " + heroList.get(i).getDexterity() +
                    ", AGI: " + heroList.get(i).getAgility() + ")");
        }
        System.out.print("Select (1-" + heroList.size() + ") or Q to cancel: ");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("Q")) {
            return null;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= heroList.size()) {
                return heroList.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
        return null;
    }

    private void setupMap() {
        gameMap = new GameMap(MAP_SIZE, MAP_SIZE, heroes.size());

        for (int i = 0; i < heroes.size(); i++) {
            gameMap.setHeroPosition(i, 0, i * 2);
        }
    }

    private void mainGameLoop() {
        while (!gameOver) {
            System.out.println("\n=== CURRENT MAP ===");
            gameMap.displayMap();
            System.out.println();

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
    }

    private void processTurn(Hero hero, int heroIndex) {
        System.out.println("\n--- AVAILABLE ACTIONS ---");
        System.out.println("W/A/S/D - Move (Up/Left/Down/Right)");
        System.out.println("I - Show Hero Stats");
        System.out.println("P - Show Map");
        System.out.println("V - View Inventory");
        System.out.println("M - Enter Market (if on market cell)");
        System.out.println("Q - Quit Game");
        System.out.print("\nYour choice: ");

        String action = scanner.nextLine().trim().toUpperCase();

        int[] currentPos = gameMap.getHeroPosition(heroIndex);
        int x = currentPos[0];
        int y = currentPos[1];

        switch (action) {
            case "W":
                attemptMove(hero, heroIndex, x - 1, y);
                break;
            case "S":
                attemptMove(hero, heroIndex, x + 1, y);
                break;
            case "A":
                attemptMove(hero, heroIndex, x, y - 1);
                break;
            case "D":
                attemptMove(hero, heroIndex, x, y + 1);
                break;
            case "I":
                showDetailedHeroStats(hero);
                processTurn(hero, heroIndex);
                break;
            case "P":
                System.out.println("\n=== MAP VIEW ===");
                gameMap.displayMap();
                System.out.println();
                processTurn(hero, heroIndex);
                break;
            case "V":
                showInventoryDetail(hero);
                processTurn(hero, heroIndex);
                break;
            case "M":
                if (gameMap.getCell(x, y).isMarket()) {
                    Market market = new Market(scanner);
                    market.enter(hero);
                } else {
                    System.out.println("[ERROR] You're not on a market cell!");
                }
                processTurn(hero, heroIndex);
                break;
            case "Q":
                if (confirmQuit()) {
                    playerQuit = true;
                    gameOver = true;
                } else {
                    processTurn(hero, heroIndex);
                }
                break;
            default:
                System.out.println("[ERROR] Invalid action!");
                processTurn(hero, heroIndex);
                break;
        }
    }

    private void showDetailedHeroStats(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  DETAILED HERO STATS");
        System.out.println("========================================");

        System.out.println("\nHero: " + hero.getName());
        System.out.println("Class: " + hero.getHeroClass());
        System.out.println("Level: " + hero.getLevel() + " (EXP: " + hero.getExperience() + "/" + (hero.getLevel() * 10) + ")");

        System.out.println("\nHit Points:");
        String hpBar = createProgressBar(hero.getHP(), hero.getMaxHP(), 30);
        System.out.println("  [" + hpBar + "] " + hero.getHP() + "/" + hero.getMaxHP());

        System.out.println("\nMana:");
        String manaBar = createProgressBar(hero.getMana(), hero.getMaxMana(), 30);
        System.out.println("  [" + manaBar + "] " + hero.getMana() + "/" + hero.getMaxMana());

        System.out.println("\nAttributes:");
        System.out.println("  Strength:  " + hero.getStrength());
        System.out.println("  Dexterity: " + hero.getDexterity());
        System.out.println("  Agility:   " + hero.getAgility());

        System.out.println("\nGold: " + hero.getMoney());

        System.out.println("\nEquipment:");
        System.out.println("  Weapon: " +
                (hero.getEquippedWeapon() != null ?
                        hero.getEquippedWeapon().getName() + " (DMG: " + hero.getEquippedWeapon().getDamage() + ")" :
                        "None"));
        System.out.println("  Armor:  " +
                (hero.getEquippedArmor() != null ?
                        hero.getEquippedArmor().getName() + " (DEF: " + hero.getEquippedArmor().getDamageReduction() + ")" :
                        "None"));

        System.out.println("\nInventory Summary:");
        System.out.println("  Weapons: " + hero.getInventory().getWeapons().size());
        System.out.println("  Armors:  " + hero.getInventory().getArmors().size());
        System.out.println("  Potions: " + hero.getInventory().getPotions().size());
        System.out.println("  Spells:  " + hero.getInventory().getSpells().size());

        System.out.println("\nCombat Stats:");
        int attackDamage = hero.getEquippedWeapon() != null ?
                (int)((hero.getStrength() + hero.getEquippedWeapon().getDamage()) * 0.05) : 0;
        int defense = hero.getEquippedArmor() != null ?
                hero.getEquippedArmor().getDamageReduction() : 0;
        double dodgeChance = hero.getAgility() * 0.002 * 100;

        System.out.println("  Attack Damage: " + attackDamage);
        System.out.println("  Defense: " + defense);
        System.out.println("  Dodge Chance: " + String.format("%.1f", dodgeChance) + "%");
        System.out.println();
    }

    private void showInventoryDetail(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  " + hero.getName() + "'s INVENTORY");
        System.out.println("========================================");
        hero.getInventory().printInventory();
        System.out.println();
    }

    private String createProgressBar(int current, int max, int length) {
        if (max == 0) return "=".repeat(length);
        int filled = (int)((double)current / max * length);
        filled = Math.max(0, Math.min(length, filled));
        return "=".repeat(filled) + "-".repeat(length - filled);
    }

    private boolean confirmQuit() {
        System.out.println("\n[WARNING] Are you sure you want to quit?");
        System.out.println("Your progress will not be saved.");
        System.out.print("Enter 'Y' to quit or 'N' to continue: ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

    private void attemptMove(Hero hero, int heroIndex, int newX, int newY) {
        if (!gameMap.inBounds(newX, newY)) {
            System.out.println("[ERROR] Can't move outside the map!");
            return;
        }

        if (!gameMap.getCell(newX, newY).isAccessible()) {
            System.out.println("[ERROR] That cell is inaccessible!");
            return;
        }

        gameMap.setHeroPosition(heroIndex, newX, newY);
        System.out.println("[SUCCESS] " + hero.getName() + " moved successfully!");

        if (!gameMap.getCell(newX, newY).isMarket()) {
            if (random.nextDouble() < BATTLE_CHANCE) {
                initiateBattle();
            }
        }
    }

    private void initiateBattle() {
        System.out.println("\n========================================");
        System.out.println("  *** BATTLE STARTED! ***");
        System.out.println("========================================\n");

        List<Monster> monsters = generateMonsters();

        BattleManager battle = new BattleManager(scanner, new ArrayList<>(heroes), monsters);
        boolean victory = battle.fight();

        if (battle.isPlayerQuit()) {
            playerQuit = true;
            gameOver = true;
            return;
        }

        if (victory) {
            System.out.println("\n========================================");
            System.out.println("  *** VICTORY! ***");
            System.out.println("========================================");

            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    int goldReward = monsters.get(0).getLevel() * 100;
                    int expReward = monsters.size() * 2;
                    hero.gainGold(goldReward);
                    hero.gainExperience(expReward);
                }
            }

            for (Hero hero : heroes) {
                if (!hero.isAlive()) {
                    hero.revive();
                }
            }
        } else {
            System.out.println("\n========================================");
            System.out.println("  *** DEFEAT! ***");
            System.out.println("========================================");
            System.out.println("All heroes have fallen! Game Over!");
            gameOver = true;
        }
    }

    private List<Monster> generateMonsters() {
        List<Monster> monsters = new ArrayList<>();

        int highestLevel = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > highestLevel) {
                highestLevel = hero.getLevel();
            }
        }

        List<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(loadDragons());
        allMonsters.addAll(loadSpirits());
        allMonsters.addAll(loadExoskeletons());

        for (int i = 0; i < heroes.size(); i++) {
            Monster selected = allMonsters.get(random.nextInt(allMonsters.size()));
            monsters.add(selected);
        }

        return monsters;
    }

    private void printGoodbye() {
        if (playerQuit) {
            System.out.println("\n========================================");
            System.out.println("  Thanks for playing!");
            System.out.println("  See you next time, brave adventurer!");
            System.out.println("========================================");
        }
    }

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