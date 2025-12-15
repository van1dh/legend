package character.hero;

/**
 * Represents a Paladin hero in the game.
 * Paladins are versatile fighters with a balance of strength and spellcasting.
 */
public class Paladin extends Hero {

    /**
     * Constructs a Paladin with the specified attributes.
     *
     * @param name       name of the Paladin
     * @param mana       initial mana
     * @param strength   initial strength
     * @param agility    initial agility
     * @param dexterity  initial dexterity
     * @param money      starting money
     * @param experience starting experience
     */
    public Paladin(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience);
        this.heroClass = "Paladin";
    }

    /**
     * Level up boosts strength and dexterity.
     */
    @Override
    public void levelUp() {
        super.levelUp();
        this.strength *= 1.1;
        this.dexterity *= 1.1;
    }

    @Override
    public String toString() {
        return "[Paladin] " + super.toString();
    }
}


