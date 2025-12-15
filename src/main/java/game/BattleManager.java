package game;

import character.hero.Hero;
import character.monster.Monster;

import java.util.List;
import java.util.Scanner;

/**
 * Manages hero vs. monster battle logic.
 */
public class BattleManager {

    private List<Hero> heroes;
    private List<Monster> monsters;
    private Scanner scanner;

    public BattleManager(Scanner scanner, List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.scanner = scanner;
    }

    public void fight() {
        System.out.println("Battle begins!");
        while (!heroes.isEmpty() && !monsters.isEmpty()) {
            for (int i = 0; i < Math.min(heroes.size(), monsters.size()); i++) {
                Hero h = heroes.get(i);
                Monster m = monsters.get(i);

                h.attack(m);
                if (!m.isAlive()) {
                    System.out.println(m.getName() + " defeated!");
                    monsters.remove(i);
                    i--;
                    continue;
                }

                m.attack(h);
                if (!h.isAlive()) {
                    System.out.println(h.getName() + " has fallen.");
                    heroes.remove(i);
                    i--;
                }
            }
        }

        System.out.println(heroes.isEmpty() ? "Monsters win!" : "Heroes win!");
    }
}
