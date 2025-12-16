package item;

/**
 * Represents a weapon that can be used by a hero in battle.
 * Each weapon has a base damage value and may hit multiple times.
 *
 * Example weapons include swords, axes, and magical staffs.
 */
public class Weapon extends Item {
    private int damage;
    private int handsRequired;

    /**
     * Constructor for Weapon.
     *
     * @param name          the name of the weapon
     * @param cost          the cost to purchase the weapon
     * @param requiredLevel the required level to equip the weapon
     * @param damage        the damage this weapon deals
     * @param handsRequired the number of hands required to wield this weapon
     */
    public Weapon(String name, int cost, int requiredLevel, int damage, int handsRequired) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public int getDamage() {
        return damage;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Damage: " + damage +
                ", Hands: " + handsRequired;
    }
}
