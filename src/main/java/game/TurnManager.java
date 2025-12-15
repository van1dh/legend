package game;

import map.GameMap;
import map.Position;
import character.hero.Hero;
import character.monster.Monster;
import character.monster.MonsterFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Controls each turn, including movement, market/battle trigger.
 */
public class TurnManager {

    private GameMap gameMap;
    private List<Hero> heroes;
    private Scanner scanner;

    public TurnManager(GameMap gameMap, List<Hero> heroes, Scanner scanner) {
        this.gameMap = gameMap;
        this.heroes = heroes;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            gameMap.display(heroes);

            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                System.out.println(hero.getName() + "'s turn:");
                System.out.print("Enter move (WASD or Q to quit): ");
                String input = scanner.nextLine().toUpperCase();

                if (input.equals("Q")) return;

                Position newPos = gameMap.moveHero(hero, input);
                if (newPos == null) {
                    System.out.println("Invalid move!");
                    continue;
                }

                if (gameMap.isMarket(newPos)) {
                    new Market(scanner).enter(hero);
                }

                if (gameMap.isBattleZone(newPos)) {
                    List<Monster> monsters = MonsterFactory.generate(heroes.size());
                    BattleManager battle = new BattleManager(scanner, List.of(hero), monsters);
                    battle.fight();
                }
            }
        }
    }
}
