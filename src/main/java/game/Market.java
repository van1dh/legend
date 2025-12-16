package game;

import character.hero.Hero;
import item.*;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles market interactions where heroes can buy and sell items.
 */
public class Market {
    private Scanner scanner;
    private List<Item> inventory;

    public Market(Scanner scanner) {
        this.scanner = scanner;
        this.inventory = new ArrayList<>();
        loadMarketInventory();
    }

    private void loadMarketInventory() {
        inventory.addAll(loadWeapons());
        inventory.addAll(loadArmor());
        inventory.addAll(loadPotions());
        inventory.addAll(loadSpells());
    }

    public void enter(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  WELCOME TO THE MARKET");
        System.out.println("========================================");
        System.out.println("Hero: " + hero.getName());
        System.out.println("Gold: " + hero.getMoney());
        System.out.println();

        while (true) {
            System.out.println("\n--- MARKET MENU ---");
            System.out.println("1) Buy items");
            System.out.println("2) Sell items");
            System.out.println("3) Equip weapon");
            System.out.println("4) Equip armor");
            System.out.println("5) View inventory");
            System.out.println("6) Exit market");
            System.out.println("Q) Quit game");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "1":
                    buyMenu(hero);
                    break;
                case "2":
                    sellMenu(hero);
                    break;
                case "3":
                    equipWeaponMenu(hero);
                    break;
                case "4":
                    equipArmorMenu(hero);
                    break;
                case "5":
                    viewInventory(hero);
                    break;
                case "6":
                    System.out.println("Thanks for visiting! Come back soon!");
                    return;
                case "Q":
                    System.out.println("Leaving market...");
                    return;
                default:
                    System.out.println("[ERROR] Invalid choice!");
                    break;
            }
        }
    }

    private void buyMenu(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  BUY ITEMS");
        System.out.println("========================================");
        System.out.println("Your Gold: " + hero.getMoney());
        System.out.println("Your Level: " + hero.getLevel());
        System.out.println();

        displayItemsByCategory(hero);

        System.out.print("\nEnter item number to buy (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;
            if (choice < 1 || choice > inventory.size()) {
                System.out.println("[ERROR] Invalid item number!");
                return;
            }

            Item item = inventory.get(choice - 1);

            if (!hero.canBuy(item)) {
                if (hero.getMoney() < item.getCost()) {
                    System.out.println("[ERROR] Not enough gold! Need " + item.getCost() +
                            " but only have " + hero.getMoney());
                } else {
                    System.out.println("[ERROR] Level too low! Need level " + item.getRequiredLevel() +
                            " but you are level " + hero.getLevel());
                }
                return;
            }

            hero.buyItem(item);
            System.out.println("[SUCCESS] Purchase successful!");

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
        }
    }

    private void displayItemsByCategory(Hero hero) {
        int index = 1;

        System.out.println("--- WEAPONS ---");
        for (Item item : inventory) {
            if (item instanceof Weapon) {
                displayItem(index++, item, hero);
            }
        }

        System.out.println("\n--- ARMOR ---");
        for (Item item : inventory) {
            if (item instanceof Armor) {
                displayItem(index++, item, hero);
            }
        }

        System.out.println("\n--- POTIONS ---");
        for (Item item : inventory) {
            if (item instanceof Potion) {
                displayItem(index++, item, hero);
            }
        }

        System.out.println("\n--- SPELLS ---");
        for (Item item : inventory) {
            if (item instanceof Spell) {
                displayItem(index++, item, hero);
            }
        }
    }

    private void displayItem(int index, Item item, Hero hero) {
        String affordable = hero.canBuy(item) ? "[OK]" : "[X]";
        System.out.println(index + ") " + affordable + " " + item);
    }

    private void sellMenu(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  SELL ITEMS");
        System.out.println("========================================");
        System.out.println("(Items sell for half their purchase price)");
        System.out.println();

        List<Item> heroItems = getAllHeroItems(hero);

        if (heroItems.isEmpty()) {
            System.out.println("[ERROR] You have no items to sell!");
            return;
        }

        for (int i = 0; i < heroItems.size(); i++) {
            Item item = heroItems.get(i);
            System.out.println((i + 1) + ") " + item.getName() +
                    " - Sell for: " + (item.getCost() / 2) + " gold");
        }

        System.out.print("\nEnter item number to sell (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;
            if (choice < 1 || choice > heroItems.size()) {
                System.out.println("[ERROR] Invalid item number!");
                return;
            }

            Item item = heroItems.get(choice - 1);
            hero.sellItem(item);
            System.out.println("[SUCCESS] Item sold successfully!");

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
        }
    }

    private void equipWeaponMenu(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("[ERROR] You have no weapons!");
            return;
        }

        System.out.println("\n--- EQUIP WEAPON ---");
        System.out.println("Currently equipped: " +
                (hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getName() : "None"));
        System.out.println();

        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            String equipped = (w == hero.getEquippedWeapon()) ? " [EQUIPPED]" : "";
            System.out.println((i + 1) + ") " + w + equipped);
        }

        System.out.print("Select weapon (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;
            if (choice >= 1 && choice <= weapons.size()) {
                hero.equipWeapon(weapons.get(choice - 1));
                System.out.println("[SUCCESS] Weapon equipped!");
            } else {
                System.out.println("[ERROR] Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
        }
    }

    private void equipArmorMenu(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmors();

        if (armors.isEmpty()) {
            System.out.println("[ERROR] You have no armor!");
            return;
        }

        System.out.println("\n--- EQUIP ARMOR ---");
        System.out.println("Currently equipped: " +
                (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
        System.out.println();

        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            String equipped = (a == hero.getEquippedArmor()) ? " [EQUIPPED]" : "";
            System.out.println((i + 1) + ") " + a + equipped);
        }

        System.out.print("Select armor (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;
            if (choice >= 1 && choice <= armors.size()) {
                hero.equipArmor(armors.get(choice - 1));
                System.out.println("[SUCCESS] Armor equipped!");
            } else {
                System.out.println("[ERROR] Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input!");
        }
    }

    private void viewInventory(Hero hero) {
        System.out.println("\n========================================");
        System.out.println("  " + hero.getName() + "'s INVENTORY");
        System.out.println("========================================");
        hero.getInventory().printInventory();
        System.out.println();
    }

    private List<Item> getAllHeroItems(Hero hero) {
        List<Item> items = new ArrayList<>();
        items.addAll(hero.getInventory().getWeapons());
        items.addAll(hero.getInventory().getArmors());
        items.addAll(hero.getInventory().getPotions());
        items.addAll(hero.getInventory().getSpells());
        return items;
    }

    private List<Weapon> loadWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Weaponry.txt");
        for (String[] row : data) {
            weapons.add(new Weapon(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4])
            ));
        }
        return weapons;
    }

    private List<Armor> loadArmor() {
        List<Armor> armors = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Armory.txt");
        for (String[] row : data) {
            armors.add(new Armor(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3])
            ));
        }
        return armors;
    }

    private List<Potion> loadPotions() {
        List<Potion> potions = new ArrayList<>();
        List<String[]> data = FileLoader.loadFile("src/data/Potions.txt");
        for (String[] row : data) {
            java.util.Set<String> attributes = new java.util.HashSet<>();
            for (int i = 4; i < row.length; i++) {
                attributes.add(row[i]);
            }
            potions.add(new Potion(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), attributes
            ));
        }
        return potions;
    }

    private List<Spell> loadSpells() {
        List<Spell> spells = new ArrayList<>();

        List<String[]> fireData = FileLoader.loadFile("src/data/FireSpells.txt");
        for (String[] row : fireData) {
            spells.add(new FireSpell(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4])
            ));
        }

        List<String[]> iceData = FileLoader.loadFile("src/data/IceSpells.txt");
        for (String[] row : iceData) {
            spells.add(new IceSpell(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4])
            ));
        }

        List<String[]> lightData = FileLoader.loadFile("src/data/LightningSpells.txt");
        for (String[] row : lightData) {
            spells.add(new LightningSpell(
                    row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Integer.parseInt(row[4])
            ));
        }

        return spells;
    }
}
