package item;

import util.FileLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for loading items (Weapons, Armors, Potions, and Spells) from file.
 */
public class ItemFactory {

    public static List<Weapon> loadWeapons(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<Weapon> weapons = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int damage = Integer.parseInt(row[3]);
            int hands = Integer.parseInt(row[4]);
            weapons.add(new Weapon(name, cost, level, damage, hands));
        }
        return weapons;
    }

    public static List<Armor> loadArmors(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<Armor> armors = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int reduction = Integer.parseInt(row[3]);
            armors.add(new Armor(name, cost, level, reduction));
        }
        return armors;
    }

    public static List<Potion> loadPotions(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<Potion> potions = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int increase = Integer.parseInt(row[3]);
            List<String> attributes = new ArrayList<>();
            for (int i = 4; i < row.length; i++) {
                attributes.add(row[i]);
            }
            potions.add(new Potion(name, cost, level, increase, attributes));
        }
        return potions;
    }

    public static List<FireSpell> loadFireSpells(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<FireSpell> spells = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int damage = Integer.parseInt(row[3]);
            int mana = Integer.parseInt(row[4]);
            spells.add(new FireSpell(name, cost, level, damage, mana));
        }
        return spells;
    }

    public static List<IceSpell> loadIceSpells(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<IceSpell> spells = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int damage = Integer.parseInt(row[3]);
            int mana = Integer.parseInt(row[4]);
            spells.add(new IceSpell(name, cost, level, damage, mana));
        }
        return spells;
    }

    public static List<LightningSpell> loadLightningSpells(String path) {
        List<String[]> rows = FileLoader.loadFile(path);
        List<LightningSpell> spells = new ArrayList<>();
        for (String[] row : rows) {
            String name = row[0];
            int cost = Integer.parseInt(row[1]);
            int level = Integer.parseInt(row[2]);
            int damage = Integer.parseInt(row[3]);
            int mana = Integer.parseInt(row[4]);
            spells.add(new LightningSpell(name, cost, level, damage, mana));
        }
        return spells;
    }
}
