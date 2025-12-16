package character.monster;

/**
 * Spirit is a type of Monster with increased dodge ability.
 * Spirits are elusive and harder to hit.
 */
public class Spirit extends Monster {

    public Spirit(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
        // Spirits have favored dodge - increase by 10%
        this.dodgeChance = this.dodgeChance * 1.1;
    }

    @Override
    public String toString() {
        return "[Spirit] " + super.toString();
    }
}