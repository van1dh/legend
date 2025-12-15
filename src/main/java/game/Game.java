package game;

import map.GameMap;
import map.Position;
import character.hero.Hero;
import character.hero.HeroFactory;
import character.monster.MonsterFactory;
import character.monster.Monster;
import core.Inventory;

import java.util.List;
import java.util.Scanner;

/**
 * Main game loop. Initializes map, players, and dispatches turns.
 */
public class Game {

    private GameMap gameMap;
    private List<Hero> heroes;
    private TurnManager turnManager;
    private Scanner scanner;

    public void start() {
        scanner = new Scanner(System.in);
        System.out.println("Welcome to Legends: Monsters and Heroes!");

        setupMap();
        setupHeroes();
        turnManager = new TurnManager(gameMap, heroes, scanner);
        turnManager.run();
    }

    private void setupMap() {
        gameMap = new GameMap(8, 8); // default 8x8 map
        gameMap.generateRandom();   // or load predefined
    }

    private void setupHeroes() {
        System.out.println("Choose your heroes (up to 3):");
        heroes = HeroFactory.selectHeroes(scanner);
        gameMap.placeHeroes(heroes); // place top row
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
