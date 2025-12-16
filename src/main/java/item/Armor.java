package item;

/**
 * Represents armor that a hero can equip to reduce incoming damage.
 * Each armor piece provides a specific amount of damage reduction.
 *
 * Examples: Breastplate, Shield, Full Body Armor, etc.
 */
public class Armor extends Item {
    private int damageReduction;

    /**
     * Constructor for Armor.
     *
     * @param name             the name of the armor
     * @param cost             the cost of the armor
     * @param requiredLevel    the minimum level required to equip the armor
     * @param damageReduction  the amount of damage this armor reduces
     */
    public Armor(String name, int cost, int requiredLevel, int damageReduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = damageReduction;
    }

    /**
     * Returns the amount of damage this armor can reduce.
     */
    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Damage Reduction: " + damageReduction;
    }
}
