package character.monster;

/**
 * Exoskeleton is a type of Monster with increased defense.
 * Exoskeletons have tough armor and take less damage.
 */
public class Exoskeleton extends Monster {

    public Exoskeleton(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
        // Exoskeletons have favored defense - increase by 10%
        this.defense = (int) (this.defense * 1.1);
    }

    @Override
    public String toString() {
        return "[Exoskeleton] " + super.toString();
    }
}