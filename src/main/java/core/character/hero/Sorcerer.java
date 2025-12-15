package character.hero;

/**
 * Represents a Sorcerer hero in the game.
 * Sorcerers are powerful magic users with high dexterity and mana.
 */
public class Sorcerer extends Hero {

    /**
     * Constructs a Sorcerer with the specified attributes.
     *
     * @param name       name of the Sorcerer
     * @param mana       initial mana
     * @param strength   initial strength
     * @param agility    initial agility
     * @param dexterity  initial dexterity
     * @param money      starting money
     * @param experience starting experience
     */
    public Sorcerer(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience);
        this.heroClass = "Sorcerer";
    }

    /**
     * Level up boosts dexterity and agility.
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.dexterity *= 1.1;
        this.agility *= 1.1;
    }

    @Override
    public String toString() {
        return "[Sorcerer] " + super.toString();
    }
}

