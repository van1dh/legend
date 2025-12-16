package character.monster;

import character.hero.Hero;

/**
 * Monster is the superclass for all monsters in the RPG.
 * It contains the basic attributes and methods common to all monsters,
 * including name, level, damage, defense, and dodge chance.
 */
public class Monster {
    protected String name;
    protected int level;
    protected int baseDamage;
    protected int defense;
    protected double dodgeChance;
    protected int maxHP;
    protected int currentHP;

    // Valor mode position
    protected int row;
    protected int col;

    public Monster(String name, int level, int baseDamage, int defense, double dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
        this.maxHP = level * 100;
        this.currentHP = maxHP;
    }

    /**
     * Monster attacks a hero.
     */
    public void attack(Hero hero) {
        if (!isAlive()) {
            System.out.println(name + " is dead and cannot attack!");
            return;
        }

        hero.takeDamage(baseDamage);
        System.out.println(name + " attacks " + hero.getName() + " for " + baseDamage + " damage!");
    }

    /**
     * Take damage from hero attack or spell, reduced by defense.
     */
    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage - defense);
        currentHP = Math.max(0, currentHP - effectiveDamage);

        if (currentHP == 0) {
            System.out.println(name + " has been defeated!");
        }
    }

    /**
     * Check if monster is alive.
     */
    public boolean isAlive() {
        return currentHP > 0;
    }

    /**
     * Reset HP to maximum (for new battles).
     */
    public void resetHP() {
        this.currentHP = maxHP;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getDamage() {
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

    public int getMaxHP() {
        return maxHP;
    }

    // Setters (needed for spell effects)
    public void setBaseDamage(int damage) {
        this.baseDamage = damage;
    }

    public void setDamage(int damage) {
        this.baseDamage = damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    // Valor mode position methods
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return name + " [Level " + level + "] " +
                "HP: " + currentHP + "/" + maxHP +
                " | DMG: " + baseDamage +
                " | DEF: " + defense +
                " | Dodge: " + dodgeChance + "%";
    }
}