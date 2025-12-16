package character.hero;

import core.Inventory;
import item.*;
import character.monster.Monster;

/**
 * Abstract class representing a Hero character in the game.
 * Heroes start with powerful starter equipment!
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

        // 大幅提高基础属性（3倍）
        this.maxHP = level * 300;  // 原来是 level * 100
        this.currentHP = maxHP;
        this.maxMana = mana * 3;   // 3倍魔法值
        this.currentMana = maxMana;
        this.strength = strength * 2;    // 2倍力量
        this.agility = agility * 2;      // 2倍敏捷
        this.dexterity = dexterity * 2;  // 2倍灵巧
        this.money = money * 3;           // 3倍金钱
        this.experience = experience;
        this.inventory = new Inventory();
        this.heroClass = "Hero";

        // Give starter equipment
        giveStarterEquipment();
    }

    /**
     * Give hero starter equipment (powerful weapons, armor, potions and spells)
     */
    private void giveStarterEquipment() {
        giveStarterWeapon();
        giveStarterArmor();
        giveStarterPotions();
        giveStarterSpells();

        System.out.println("[EQUIPMENT] " + name + " received starter equipment!");
    }

    /**
     * Give starter weapon (based on hero type)
     */
    private void giveStarterWeapon() {
        Weapon weapon = null;

        if (this instanceof Warrior) {
            // 战士：超强双手剑
            weapon = new Weapon("Legendary Greatsword", 0, 1, 2500, 2);
        } else if (this instanceof Paladin) {
            // 圣骑士：强力单手剑
            weapon = new Weapon("Holy Longsword", 0, 1, 2200, 1);
        } else if (this instanceof Sorcerer) {
            // 法师：魔法法杖
            weapon = new Weapon("Arcane Staff", 0, 1, 2000, 1);
        }

        if (weapon != null) {
            inventory.addItem(weapon);
            equipWeapon(weapon);
            System.out.println("  -> Equipped: " + weapon.getName());
        }
    }

    /**
     * Give starter armor
     */
    private void giveStarterArmor() {
        Armor armor = null;

        if (this instanceof Warrior) {
            // 战士：超重型盔甲
            armor = new Armor("Titanium Heavy Armor", 0, 1, 800);
        } else if (this instanceof Paladin) {
            // 圣骑士：强化板甲
            armor = new Armor("Divine Plate Armor", 0, 1, 700);
        } else if (this instanceof Sorcerer) {
            // 法师：魔法长袍
            armor = new Armor("Enchanted Mage Robe", 0, 1, 600);
        }

        if (armor != null) {
            inventory.addItem(armor);
            equipArmor(armor);
            System.out.println("  -> Equipped: " + armor.getName());
        }
    }

    /**
     * Give starter potions (multiple types, large quantity)
     */
    private void giveStarterPotions() {
        // 10 healing potions (加倍)
        for (int i = 0; i < 10; i++) {
            java.util.Set<String> healthAttr = new java.util.HashSet<>();
            healthAttr.add("Health");
            Potion healthPotion = new Potion("Super Healing Potion", 0, 1, 300, healthAttr);
            inventory.addItem(healthPotion);
        }

        // 10 mana potions (加倍)
        for (int i = 0; i < 10; i++) {
            java.util.Set<String> manaAttr = new java.util.HashSet<>();
            manaAttr.add("Mana");
            Potion manaPotion = new Potion("Super Mana Potion", 0, 1, 300, manaAttr);
            inventory.addItem(manaPotion);
        }

        // 5 strength potions
        for (int i = 0; i < 5; i++) {
            java.util.Set<String> strAttr = new java.util.HashSet<>();
            strAttr.add("Strength");
            Potion strPotion = new Potion("Greater Strength Potion", 0, 1, 200, strAttr);
            inventory.addItem(strPotion);
        }

        // 5 agility potions
        for (int i = 0; i < 5; i++) {
            java.util.Set<String> agiAttr = new java.util.HashSet<>();
            agiAttr.add("Agility");
            Potion agiPotion = new Potion("Greater Agility Potion", 0, 1, 200, agiAttr);
            inventory.addItem(agiPotion);
        }

        System.out.println("  -> Received: 10x Super Healing, 10x Super Mana, 5x Greater Strength, 5x Greater Agility Potions");
    }

    /**
     * Give starter spells (based on hero type, large quantity)
     */
    private void giveStarterSpells() {
        // All heroes get spells, sorcerers get even more
        int spellCount = (this instanceof Sorcerer) ? 10 : 7;

        // Give powerful fire spells
        for (int i = 0; i < spellCount; i++) {
            FireSpell fireSpell = new FireSpell("Mega Fireball", 0, 1, 1200, 80);
            inventory.addItem(fireSpell);
        }

        // Give powerful ice spells
        for (int i = 0; i < spellCount; i++) {
            IceSpell iceSpell = new IceSpell("Frozen Nova", 0, 1, 1100, 75);
            inventory.addItem(iceSpell);
        }

        // Give powerful lightning spells
        for (int i = 0; i < spellCount; i++) {
            LightningSpell lightningSpell = new LightningSpell("Chain Lightning", 0, 1, 1150, 70);
            inventory.addItem(lightningSpell);
        }

        System.out.println("  -> Received: " + spellCount + "x Mega Fire, Ice, and Lightning Spells");
    }

    private int calculateLevel(int exp) {
        int lvl = 1;
        int expNeeded = 10;
        while (exp >= expNeeded) {
            lvl++;
            expNeeded += (lvl * 10);
        }
        return lvl;
    }

    public void levelUp() {
        level++;

        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);

        // 使用新的HP计算公式
        maxHP = level * 300;
        currentHP = maxHP;

        maxMana = (int) (maxMana * 1.1);
        currentMana = maxMana;

        System.out.println(name + " leveled up to level " + level + "!");
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void attack(Monster monster) {
        if (equippedWeapon == null) {
            System.out.println(name + " has no weapon equipped!");
            return;
        }

        if (Math.random() < monster.getDodgeChance() * 0.01) {
            System.out.println(monster.getName() + " dodged the attack!");
            return;
        }

        int damage = (int) ((strength + equippedWeapon.getDamage()) * 0.05);
        monster.takeDamage(damage);
        System.out.println(name + " dealt " + damage + " damage to " + monster.getName());
    }

    public void castSpell(Spell spell, Monster monster) {
        if (currentMana < spell.getManaCost()) {
            System.out.println(name + " doesn't have enough mana!");
            return;
        }

        if (Math.random() < monster.getDodgeChance() * 0.01) {
            System.out.println(monster.getName() + " dodged the spell!");
            return;
        }

        int damage = (int) (spell.getBaseDamage() + (dexterity / 10000.0) * spell.getBaseDamage());
        monster.takeDamage(damage);

        spell.applySpellEffect(monster);

        currentMana -= spell.getManaCost();

        // 法术使用后消失（一次性物品）
        inventory.getSpells().remove(spell);

        System.out.println(name + " cast " + spell.getName() + " dealing " + damage + " damage!");
        System.out.println("[INFO] " + spell.getName() + " consumed (single-use item)");
    }

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

    public void regen() {
        if (isAlive()) {
            currentHP = Math.min(maxHP, (int) (currentHP * 1.1));
            currentMana = Math.min(maxMana, (int) (currentMana * 1.1));
        }
    }

    public void takeDamage(int damage) {
        double dodgeChance = agility * 0.002;
        if (Math.random() < dodgeChance) {
            System.out.println(name + " dodged the attack!");
            return;
        }

        int reduction = equippedArmor != null ? equippedArmor.getDamageReduction() : 0;
        int actualDamage = Math.max(0, damage - reduction);
        currentHP = Math.max(0, currentHP - actualDamage);

        System.out.println(name + " took " + actualDamage + " damage! HP: " + currentHP);

        if (currentHP == 0) {
            System.out.println(name + " has fainted!");
        }
    }

    public void revive() {
        currentHP = maxHP / 2;
        currentMana = maxMana / 2;
        System.out.println(name + " has been revived!");
    }

    public void reviveAtHalfHP() {
        revive();
    }

    public void gainExperience(int exp) {
        experience += exp;
        int expNeeded = level * 10;

        if (experience >= expNeeded) {
            levelUp();
        }
    }

    public void gainGold(int amount) {
        money += amount;
        System.out.println(name + " gained " + amount + " gold!");
    }

    public boolean canBuy(Item item) {
        return money >= item.getCost() && level >= item.getRequiredLevel();
    }

    public void buyItem(Item item) {
        if (!canBuy(item)) {
            System.out.println("Cannot buy " + item.getName());
            return;
        }

        money -= item.getCost();
        inventory.addItem(item);
        System.out.println(name + " bought " + item.getName() + " for " + item.getCost() + " gold");
    }

    public void sellItem(Item item) {
        int sellPrice = item.getCost() / 2;
        money += sellPrice;
        if (item instanceof Weapon) inventory.getWeapons().remove(item);
        else if (item instanceof Armor) inventory.getArmors().remove(item);
        else if (item instanceof Potion) inventory.getPotions().remove(item);
        else if (item instanceof Spell) inventory.getSpells().remove(item);

        System.out.println(name + " sold " + item.getName() + " for " + sellPrice + " gold");
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(name + " equipped " + weapon.getName());
    }

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

    // Valor mode methods
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getLaneIndex() { return laneIndex; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public void setLaneIndex(int index) { this.laneIndex = index; }

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