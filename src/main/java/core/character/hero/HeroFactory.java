package factory;

import character.hero.*;
import utils.FileLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to load and create Hero objects from data files.
 */
public class HeroFactory {

    /**
     * Load heroes from a given file and return a list of Hero instances.
     *
     * @param filePath   path to the hero data file
     * @param heroType   type of the hero ("Warrior", "Paladin", "Sorcerer")
     * @return list of Hero objects
     */
    public static List<Hero> loadHeroes(String filePath, String heroType) {
        List<String[]> data = FileLoader.loadFile(filePath);
        List<Hero> heroes = new ArrayList<>();

        for (String[] line : data) {
            String name = line[0];
            int mana = Integer.parseInt(line[1]);
            int strength = Integer.parseInt(line[2]);
            int agility = Integer.parseInt(line[3]);
            int dexterity = Integer.parseInt(line[4]);
            int money = Integer.parseInt(line[5]);
            int experience = Integer.parseInt(line[6]);

            Hero hero;
            switch (heroType.toLowerCase()) {
                case "warrior" -> hero = new Warrior(name, mana, strength, agility, dexterity, money, experience);
                case "paladin" -> hero = new Paladin(name, mana, strength, agility, dexterity, money, experience);
                case "sorcerer" -> hero = new Sorcerer(name, mana, strength, agility, dexterity, money, experience);
                default -> throw new IllegalArgumentException("Unknown hero type: " + heroType);
            }

            heroes.add(hero);
        }

        return heroes;
    }
}
