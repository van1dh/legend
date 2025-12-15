package character;


/**
 * Spirit is a type of Monster with no additional behavior.
 */
class Spirit extends Monster {
    public Spirit(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }
}