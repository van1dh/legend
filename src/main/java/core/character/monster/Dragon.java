package character.monster;

/**
 * Dragon is a type of Monster with increased base damage.
 * Dragons are fierce and deal more damage than other monster types.
 */
public class Dragon extends Monster {

    public Dragon(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
        // Dragons have favored damage - increase by 10%
        this.baseDamage = (int) (this.baseDamage * 1.1);
    }

    @Override
    public String toString() {
        return "[Dragon] " + super.toString();
    }
}