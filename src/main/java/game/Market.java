package game;

import character.hero.Hero;
import item.Item;
import item.ItemFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Handles item purchase/sale interaction in market cells.
 */
public class Market {

    private Scanner scanner;

    public Market(Scanner scanner) {
        this.scanner = scanner;
    }

    public void enter(Hero hero) {
        while (true) {
            System.out.println("Market: Buy(B), Sell(S), Exit(E)?");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "B" -> buy(hero);
                case "S" -> sell(hero);
                case "E" -> { return; }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void buy(Hero hero) {
        List<Item> shop = ItemFactory.getAllItems();
        for (int i = 0; i < shop.size(); i++) {
            System.out.println(i + ": " + shop.get(i));
        }

        System.out.print("Choose item to buy: ");
        int index = Integer.parseInt(scanner.nextLine());
        Item item = shop.get(index);
        if (hero.canBuy(item)) {
            hero.buyItem(item);
            System.out.println("Purchased " + item.getName());
        } else {
            System.out.println("Cannot buy: level or gold insufficient.");
        }
    }

    private void sell(Hero hero) {
        List<Item> inventory = hero.getInventory().getAllItems();
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(i + ": " + inventory.get(i));
        }

        System.out.print("Choose item to sell: ");
        int index = Integer.parseInt(scanner.nextLine());
        Item item = inventory.get(index);
        hero.sellItem(item);
        System.out.println("Sold " + item.getName());
    }
}
