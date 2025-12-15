package character;

/**
 * Monster is the superclass for all monsters in the RPG.
 * It contains the basic attributes and methods common to all monsters,
 * including name, level, damage, defense, and dodge chance.
 */
public class Monster {
    private String name;
    private int level;
    private int baseDamage;
    private int defense;
    private double dodgeChance;
    private int currentHP;

    public Monster(String name, int level, int baseDamage, int defense, double dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
        this.currentHP = level * 100; // arbitrary scaling
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getDefense() {
        return defense;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage - defense);
        currentHP = Math.max(0, currentHP - effectiveDamage);
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void resetHP() {
        this.currentHP = level * 100;
    }

    @Override
    public String toString() {
        return name + " [Level: " + level + ", Damage: " + baseDamage + ", Defense: " + defense
                + ", Dodge Chance: " + dodgeChance + ", HP: " + currentHP + "]";
    }
}