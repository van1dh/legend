public class Main {
    public static void main(String[] args) {
        System.out.println("1) Legends: Monsters & Heroes");
        System.out.println("2) Legends of Valor");

        if (choice == 1) {
            new game.Game().start();
        } else {
            new valor.ValorGame().start();
        }
    }
}
