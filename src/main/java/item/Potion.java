package item;

import java.util.Set;

/**
 * Represents a Potion item that temporarily boosts one or more hero attributes.
 * Potions can enhance strength, agility, dexterity, or health depending on the type.
 */
public class Potion extends Item {
    private int effectAmount;
    private Set<String> affectedAttributes;

    /**
     * Constructs a Potion.
     *
     * @param name               the name of the potion
     * @param cost               the cost of the potion
     * @param requiredLevel      the required hero level to use the potion
     * @param effectAmount       the strength of the potion’s effect
     * @param affectedAttributes the set of attributes that this potion boosts
     */
    public Potion(String name, int cost, int requiredLevel, int effectAmount, Set<String> affectedAttributes) {
        super(name, cost, requiredLevel);
        this.effectAmount = effectAmount;
        this.affectedAttributes = affectedAttributes;
    }

    /**
     * Gets the numerical strength of the potion's effect.
     */
    public int getEffectAmount() {
        return effectAmount;
    }

    /**
     * Gets the set of attributes affected by this potion.
     */
    public Set<String> getAffectedAttributes() {
        return affectedAttributes;
    }

    @Override
    public String toString() {
        return super.toString()
                + ", Effect Amount: " + effectAmount
                + ", Affects: " + affectedAttributes;
    }
}
