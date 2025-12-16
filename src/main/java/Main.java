import java.util.Scanner;

/**
 * Main entry point for the Legends games.
 * Allows player to choose between:
 * 1) Legends: Monsters & Heroes (classic RPG)
 * 2) Legends of Valor (MOBA-style)
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   WELCOME TO LEGENDS GAMES         ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        System.out.println("Select a game mode:");
        System.out.println("1) Legends: Monsters & Heroes (Classic RPG)");
        System.out.println("2) Legends of Valor (MOBA-style)");
        System.out.println("Q) Quit");
        System.out.println();
        System.out.print("Enter your choice: ");

        String input = scanner.nextLine().trim().toUpperCase();

        switch (input) {
            case "1":
                System.out.println("\nStarting Legends: Monsters & Heroes...\n");
                new game.Game().start();
                break;

            case "2":
                System.out.println("\nStarting Legends of Valor...\n");
                new valor.ValorGame().start();
                break;

            case "Q":
                System.out.println("Thanks for playing! Goodbye!");
                break;

            default:
                System.out.println("Invalid choice. Please run the program again.");
                break;
        }

        scanner.close();
    }
}
