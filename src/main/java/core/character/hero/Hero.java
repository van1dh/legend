package character;

import item.Armor;
import item.Potion;
import item.Weapon;
import spell.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a Hero character in the game.
 * Encapsulates common stats, inventory, and actions.
 */
public abstract class Hero {
    protected String name;
    protected int level;
    protected int maxHP;
    protected int currentHP;
    protected int mana;
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected int money;
    protected int experience;

    protected Weapon equippedWeapon;
    protected Armor equippedArmor;
    protected List<Spell> spellbook;
    protected List<Potion> potions;

    public Hero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        this.name = name;
        this.level = 1;
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.mana = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.money = money;
        this.experience = experience;
        this.spellbook = new ArrayList<>();
        this.potions = new ArrayList<>();
    }

    public abstract void levelUp();

    public String getName() {
        return name;
    }

    public int getHP() {
        return currentHP;
    }

    public void receiveDamage(int damage) {
        currentHP = Math.max(currentHP - damage, 0);
    }

    public void heal() {
        this.currentHP = maxHP;
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
    }

    public void castSpell(Spell spell) {
        if (mana >= spell.getManaCost()) {
            mana -= spell.getManaCost();
            // Apply spell effects externally
        }
    }

    public void usePotion(Potion potion) {
        potion.apply(this);
        potions.remove(potion);
    }

    public void gainXP(int xp) {
        experience += xp;
        if (experience >= level * 10) {
            level++;
            levelUp();
        }
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getDexterity() {
        return dexterity;
    }
}

