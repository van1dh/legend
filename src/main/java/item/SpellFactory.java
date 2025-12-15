package item;

import core.FileLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to load and generate all types of Spells.
 * Supports Fire, Ice, and Lightning Spells from text data.
 */
public class SpellFactory {
    private static final String FIRE_SPELLS_FILE = "src/data/FireSpells.txt";
    private static final String ICE_SPELLS_FILE = "src/data/IceSpells.txt";
    private static final String LIGHTNING_SPELLS_FILE = "src/data/LightningSpells.txt";

    /**
     * Loads all spells from all categories (Fire, Ice, Lightning).
     * @return A list of all spells.
     */
    public static List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        spells.addAll(getFireSpells());
        spells.addAll(getIceSpells());
        spells.addAll(getLightningSpells());
        return spells;
    }

    public static List<Spell> getFireSpells() {
        List<String[]> rawData = FileLoader.load(FIRE_SPELLS_FILE, 6);
        List<Spell> spells = new ArrayList<>();
        for (String[] line : rawData) {
            spells.add(new FireSpell(
                    line[0],                        // name
                    Integer.parseInt(line[1]),      // cost
                    Integer.parseInt(line[2]),      // required level
                    Integer.parseInt(line[3]),      // damage
                    Integer.parseInt(line[4]),      // mana cost
                    Integer.parseInt(line[5])       // reduction (defense decrease)
            ));
        }
        return spells;
    }

    public static List<Spell> getIceSpells() {
        List<String[]> rawData = FileLoader.load(ICE_SPELLS_FILE, 6);
        List<Spell> spells = new ArrayList<>();
        for (String[] line : rawData) {
            spells.add(new IceSpell(
                    line[0],
                    Integer.parseInt(line[1]),
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]),
                    Integer.parseInt(line[4]),
                    Integer.parseInt(line[5])
            ));
        }
        return spells;
    }

    public static List<Spell> getLightningSpells() {
        List<String[]> rawData = FileLoader.load(LIGHTNING_SPELLS_FILE, 6);
        List<Spell> spells = new ArrayList<>();
        for (String[] line : rawData) {
            spells.add(new LightningSpell(
                    line[0],
                    Integer.parseInt(line[1]),
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]),
                    Integer.parseInt(line[4]),
                    Integer.parseInt(line[5])
            ));
        }
        return spells;
    }
}
