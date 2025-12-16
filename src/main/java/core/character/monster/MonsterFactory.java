package factory;

import character.monster.*;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to load and create Monster objects from data files.
 */
public class MonsterFactory {

    /**
     * Load monsters from a given file and return a list of Monster instances.
     *
     * @param filePath    path to the monster data file
     * @param monsterType type of the monster ("Dragon", "Exoskeleton", "Spirit")
     * @return list of Monster objects
     */
    public static List<Monster> loadMonsters(String filePath, String monsterType) {
        List<String[]> data = FileLoader.loadFile(filePath);
        List<Monster> monsters = new ArrayList<>();

        for (String[] line : data) {
            String name = line[0];
            int level = Integer.parseInt(line[1]);
            int damage = Integer.parseInt(line[2]);
            int defense = Integer.parseInt(line[3]);
            int dodge = Integer.parseInt(line[4]);

            Monster monster;
            switch (monsterType.toLowerCase()) {
                case "dragon" -> monster = new Dragon(name, level, damage, defense, dodge);
                case "exoskeleton" -> monster = new Exoskeleton(name, level, damage, defense, dodge);
                case "spirit" -> monster = new Spirit(name, level, damage, defense, dodge);
                default -> throw new IllegalArgumentException("Unknown monster type: " + monsterType);
            }

            monsters.add(monster);
        }

        return monsters;
    }
}
