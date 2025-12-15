package character.hero;

/**
 * Represents a Warrior hero in the game.
 * Warriors are known for their superior strength and durability.
 * They gain more strength with each level.
 */
public class Warrior extends Hero {

    /**
     * Constructs a Warrior with the specified attributes.
     *
     * @param name       name of the Warrior
     * @param mana       initial mana
     * @param strength   initial strength
     * @param agility    initial agility
     * @param dexterity  initial dexterity
     * @param money      starting money
     * @param experience starting experience
     */
    public Warrior(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience);
        this.heroClass = "Warrior";
    }

    /**
     * Level up boosts strength and agility.
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.strength *= 1.1;
        this.agility *= 1.1;
    }

    @Override
    public String toString() {
        return "[Warrior] " + super.toString();
    }
}

