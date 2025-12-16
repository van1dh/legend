package item;

/**
 * Abstract base class for all types of spells in the game.
 * Spells have attributes like damage, mana cost, and may reduce enemy attributes upon casting.
 */
public abstract class Spell extends Item {
    private int baseDamage;
    private int manaCost;

    /**
     * Constructs a generic spell.
     *
     * @param name          the name of the spell
     * @param cost          the price of the spell
     * @param requiredLevel the level required to use this spell
     * @param baseDamage    the base damage the spell deals
     * @param manaCost      the mana cost to cast this spell
     */
    public Spell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel);
        this.baseDamage = baseDamage;
        this.manaCost = manaCost;
    }

    /**
     * Returns the base damage of the spell.
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Returns the mana cost of the spell.
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Applies spell effect (subclass-specific) to the monster.
     * Subclasses must override this method to apply effects (like reducing dodge, defense, etc.)
     *
     * @param monster the monster being targeted by the spell
     */
    public abstract void applySpellEffect(character.Monster monster);

    @Override
    public String toString() {
        return super.toString()
                + ", Damage: " + baseDamage
                + ", Mana Cost: " + manaCost;
    }
}
