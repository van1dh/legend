package character.hero;

import core.Inventory;
import item.*;
import character.monster.Monster;

/**
 * Abstract class representing a Hero character in the game.
 * Encapsulates common stats, inventory, and actions.
 */
public abstract class Hero {
    protected String name;
    protected String heroClass;
    protected int level;
    protected int maxHP;
    protected int currentHP;
    protected int maxMana;
    protected int currentMana;
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected int money;
    protected int experience;

    protected Weapon equippedWeapon;
    protected Armor equippedArmor;
    protected Inventory inventory;

    // Valor mode fields
    protected int row;
    protected int col;
    protected int laneIndex;

    public Hero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        this.name = name;
        this.level = calculateLevel(experience);
        this.maxHP = level * 100;
        this.currentHP = maxHP;
        this.maxMana = mana;
        this.currentMana = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.money = money;
        this.experience = experience;
        this.inventory = new Inventory();
        this.heroClass = "Hero";
    }

    /**
     * Calculate level based on experience points.
     */
    private int calculateLevel(int exp) {
        int lvl = 1;
        int expNeeded = 10;
        while (exp >= expNeeded) {
            lvl++;
            expNeeded += (lvl * 10);
        }
        return lvl;
    }

    /**
     * Level up increases all skills by 5% and favored skills by additional 5%.
     * Subclasses override to specify which skills are favored.
     */
    public void levelUp() {
        level++;

        // Base increases for all skills (5%)
        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);

        // Reset HP based on new level
        maxHP = level * 100;
        currentHP = maxHP;

        // Increase mana by 10%
        maxMana = (int) (maxMana * 1.1);
        currentMana = maxMana;

        System.out.println(name + " leveled up to level " + level + "!");
    }

    /**
     * Check if hero is alive.
     */
    public boolean isAlive() {
        return currentHP > 0;
    }

    /**
     * Hero attacks a monster with equipped weapon.
     */
    public void attack(Monster monster) {
        if (equippedWeapon == null) {
            System.out.println(name + " has no weapon equipped!");
            return;
        }

        // Check if monster dodges
        if (Math.random() < monster.getDodgeChance() * 0.01) {
            System.out.println(monster.getName() + " dodged the attack!");
            return;
        }

        // Calculate damage: (strength + weapon_damage) * 0.05
        int damage = (int) ((strength + equippedWeapon.getDamage()) * 0.05);
        monster.takeDamage(damage);
        System.out.println(name + " dealt " + damage + " damage to " + monster.getName());
    }

    /**
     * Cast a spell on a monster.
     */
    public void castSpell(Spell spell, Monster monster) {
        if (currentMana < spell.getManaCost()) {
            System.out.println(name + " doesn't have enough mana!");
            return;
        }

        // Check if monster dodges
        if (Math.random() < monster.getDodgeChance() * 0.01) {
            System.out.println(monster.getName() + " dodged the spell!");
            return;
        }

        // Calculate spell damage
        int damage = (int) (spell.getBaseDamage() + (dexterity / 10000.0) * spell.getBaseDamage());
        monster.takeDamage(damage);

        // Apply spell effect
        spell.applySpellEffect(monster);

        currentMana -= spell.getManaCost();
        System.out.println(name + " cast " + spell.getName() + " dealing " + damage + " damage!");
    }

    /**
     * Use a potion from inventory.
     */
    public void usePotion(Potion potion) {
        for (String attr : potion.getAffectedAttributes()) {
            switch (attr.toLowerCase()) {
                case "health":
                case "hp":
                    currentHP = Math.min(maxHP, currentHP + potion.getEffectAmount());
                    System.out.println(name + " restored " + potion.getEffectAmount() + " HP");
                    break;
                case "mana":
                case "mp":
                    currentMana = Math.min(maxMana, currentMana + potion.getEffectAmount());
                    System.out.println(name + " restored " + potion.getEffectAmount() + " Mana");
                    break;
                case "strength":
                    strength += potion.getEffectAmount();
                    System.out.println(name + " gained " + potion.getEffectAmount() + " Strength");
                    break;
                case "dexterity":
                    dexterity += potion.getEffectAmount();
                    System.out.println(name + " gained " + potion.getEffectAmount() + " Dexterity");
                    break;
                case "agility":
                    agility += potion.getEffectAmount();
                    System.out.println(name + " gained " + potion.getEffectAmount() + " Agility");
                    break;
            }
        }
        inventory.getPotions().remove(potion);
    }

    /**
     * Regenerate HP and Mana at end of round (10% each).
     */
    public void regen() {
        if (isAlive()) {
            currentHP = Math.min(maxHP, (int) (currentHP * 1.1));
            currentMana = Math.min(maxMana, (int) (currentMana * 1.1));
        }
    }

    /**
     * Take damage from a monster, reduced by armor.
     */
    public void takeDamage(int damage) {
        // Check dodge chance
        double dodgeChance = agility * 0.002;
        if (Math.random() < dodgeChance) {
            System.out.println(name + " dodged the attack!");
            return;
        }

        // Reduce damage by armor
        int reduction = equippedArmor != null ? equippedArmor.getDamageReduction() : 0;
        int actualDamage = Math.max(0, damage - reduction);
        currentHP = Math.max(0, currentHP - actualDamage);

        System.out.println(name + " took " + actualDamage + " damage! HP: " + currentHP);

        if (currentHP == 0) {
            System.out.println(name + " has fainted!");
        }
    }

    /**
     * Revive hero with half HP and mana.
     */
    public void revive() {
        currentHP = maxHP / 2;
        currentMana = maxMana / 2;
        System.out.println(name + " has been revived!");
    }

    /**
     * Revive at half HP (for Valor mode).
     */
    public void reviveAtHalfHP() {
        revive();
    }

    /**
     * Gain experience and potentially level up.
     */
    public void gainExperience(int exp) {
        experience += exp;
        int expNeeded = level * 10;

        if (experience >= expNeeded) {
            levelUp();
        }
    }

    /**
     * Gain gold reward.
     */
    public void gainGold(int amount) {
        money += amount;
        System.out.println(name + " gained " + amount + " gold!");
    }

    /**
     * Check if hero can buy an item.
     */
    public boolean canBuy(Item item) {
        return money >= item.getCost() && level >= item.getRequiredLevel();
    }

    /**
     * Buy an item from market.
     */
    public void buyItem(Item item) {
        if (!canBuy(item)) {
            System.out.println("Cannot buy " + item.getName());
            return;
        }

        money -= item.getCost();
        inventory.addItem(item);
        System.out.println(name + " bought " + item.getName() + " for " + item.getCost() + " gold");
    }

    /**
     * Sell an item for half price.
     */
    public void sellItem(Item item) {
        int sellPrice = item.getCost() / 2;
        money += sellPrice;
        // Remove from inventory based on type
        if (item instanceof Weapon) inventory.getWeapons().remove(item);
        else if (item instanceof Armor) inventory.getArmors().remove(item);
        else if (item instanceof Potion) inventory.getPotions().remove(item);
        else if (item instanceof Spell) inventory.getSpells().remove(item);

        System.out.println(name + " sold " + item.getName() + " for " + sellPrice + " gold");
    }

    /**
     * Equip a weapon from inventory.
     */
    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(name + " equipped " + weapon.getName());
    }

    /**
     * Equip armor from inventory.
     */
    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
        System.out.println(name + " equipped " + armor.getName());
    }

    // Getters
    public String getName() { return name; }
    public String getHeroClass() { return heroClass; }
    public int getLevel() { return level; }
    public int getHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public int getMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getAgility() { return agility; }
    public int getMoney() { return money; }
    public int getExperience() { return experience; }
    public Inventory getInventory() { return inventory; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }

    // Valor mode position methods
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getLaneIndex() { return laneIndex; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public void setLaneIndex(int index) { this.laneIndex = index; }

    // Valor mode buff methods
    public void applyTemporaryStrengthBuff(double multiplier) {
        strength = (int) (strength * multiplier);
    }
    public void removeTemporaryStrengthBuff(double multiplier) {
        strength = (int) (strength / multiplier);
    }
    public void applyTemporaryDexterityBuff(double multiplier) {
        dexterity = (int) (dexterity * multiplier);
    }
    public void removeTemporaryDexterityBuff(double multiplier) {
        dexterity = (int) (dexterity / multiplier);
    }
    public void applyTemporaryAgilityBuff(double multiplier) {
        agility = (int) (agility * multiplier);
    }
    public void removeTemporaryAgilityBuff(double multiplier) {
        agility = (int) (agility / multiplier);
    }

    @Override
    public String toString() {
        return name + " [" + heroClass + "] Lv." + level +
                " | HP: " + currentHP + "/" + maxHP +
                " | Mana: " + currentMana + "/" + maxMana +
                " | STR: " + strength + " DEX: " + dexterity + " AGI: " + agility +
                " | Gold: " + money + " | EXP: " + experience;
    }
}

