package valor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.character.hero.Hero;
import core.character.hero.HeroFactory;
import core.character.monster.Monster;
import core.character.monster.MonsterFactory;

/**
 * Main controller for Legends of Valor.
 * Sets up the 8x8 three-lane map, heroes, monsters, and runs
 * the turn-based MOBA-style game loop.
 */
public class ValorGame {

    private static final int MAP_SIZE = 8;
    private final Scanner scanner = new Scanner(System.in);

    private ValorMap map;
    private List<Hero> heroes;
    private List<Monster> monsters;

    public void start() {
        System.out.println("=== Legends of Valor ===");
        setupGame();
        mainLoop();
    }

    private void setupGame() {
        this.map = new ValorMap(MAP_SIZE);

        System.out.println("Select your heroes (up to 3):");
        heroes = HeroFactory.selectHeroes(scanner, 3);

        map.placeInitialHeroes(heroes);

        monsters = MonsterFactory.generateInitialValorMonsters(heroes);
        map.placeInitialMonsters(monsters);
    }

    private void mainLoop() {
        int round = 1;
        while (true) {
            System.out.println();
            System.out.println("===== ROUND " + round + " =====");
            map.printMap(heroes, monsters);

            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;
                handleHeroTurn(hero);
            }

            MonsterAI.processMonsters(map, monsters, heroes);

            endOfRound();

            if (map.anyHeroReachedMonsterNexus(heroes)) {
                System.out.println("Heroes reached the enemy Nexus! You win!");
                break;
            }
            if (map.anyMonsterReachedHeroNexus(monsters)) {
                System.out.println("Monsters reached your Nexus! You lose!");
                break;
            }

            round++;
        }

        System.out.println("Game Over (Valor mode).");
    }

    private void handleHeroTurn(Hero hero) {
        while (true) {
            System.out.println();
            System.out.println("Hero: " + hero.getName());
            System.out.println("Choose action:");
            System.out.println("1) Move");
            System.out.println("2) Attack");
            System.out.println("3) Cast Spell");
            System.out.println("4) Use Potion");
            System.out.println("5) Teleport");
            System.out.println("6) Recall");
            System.out.println("7) Remove Obstacle");
            System.out.println("Q) Skip / End Turn");
            System.out.print("Action: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "1" -> {
                    if (Actions.moveHero(scanner, map, hero)) return;
                }
                case "2" -> {
                    if (Actions.heroAttack(scanner, map, hero, monsters)) return;
                }
                case "3" -> {
                    if (Actions.heroCastSpell(scanner, map, hero, monsters)) return;
                }
                case "4" -> {
                    if (Actions.heroUsePotion(scanner, hero)) return;
                }
                case "5" -> {
                    if (Actions.teleportHero(scanner, map, hero)) return;
                }
                case "6" -> {
                    Actions.recallHero(map, hero);
                    return;
                }
                case "7" -> {
                    if (Actions.removeObstacle(scanner, map, hero)) return;
                }
                case "Q" -> {
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void endOfRound() {
        for (Hero hero : heroes) {
            hero.regen();
            if (!hero.isAlive()) {
                map.respawnHeroAtNexus(hero);
                hero.reviveAtHalfHP();
            }
        }
    }
}
