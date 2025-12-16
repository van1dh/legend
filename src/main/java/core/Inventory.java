package core;

import item.*;

import java.util.*;

/**
 * Inventory class manages a hero's owned items: weapons, armor, potions, and spells.
 * It supports adding, retrieving, and listing items.
 */
public class Inventory {
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Armor> armors = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();

    public void addItem(Item item) {
        if (item instanceof Weapon) weapons.add((Weapon) item);
        else if (item instanceof Armor) armors.add((Armor) item);
        else if (item instanceof Potion) potions.add((Potion) item);
        else if (item instanceof Spell) spells.add((Spell) item);
    }

    public List<Weapon> getWeapons() { return weapons; }
    public List<Armor> getArmors() { return armors; }
    public List<Potion> getPotions() { return potions; }
    public List<Spell> getSpells() { return spells; }

    public void printInventory() {
        System.out.println("Weapons: " + (weapons.isEmpty() ? "None" : ""));
        weapons.forEach(w -> System.out.println("- " + w));

        System.out.println("Armors: " + (armors.isEmpty() ? "None" : ""));
        armors.forEach(a -> System.out.println("- " + a));

        System.out.println("Potions: " + (potions.isEmpty() ? "None" : ""));
        potions.forEach(p -> System.out.println("- " + p));

        System.out.println("Spells: " + (spells.isEmpty() ? "None" : ""));
        spells.forEach(s -> System.out.println("- " + s));
    }
}