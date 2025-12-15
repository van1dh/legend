package item;

/**
 * Abstract base class representing any item that a hero can own or use,
 * including weapons, armor, potions, and spells.
 *
 * Common properties include:
 * - name: The display name of the item
 * - cost: Gold required to purchase the item
 * - requiredLevel: Minimum level the hero must reach to use the item
 *
 * This class is extended by more specific item classes such as Weapon,
 * Armor, Potion, and Spell.
 */
public abstract class Item {
    protected String name;
    protected int cost;
    protected int requiredLevel;

    public Item(String name, int cost, int requiredLevel) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public String toString() {
        return name + " (Cost: " + cost + ", Level: " + requiredLevel + ")";
    }
}
