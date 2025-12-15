package item;

import character.Monster;

/**
 * IceSpell is a type of Spell that reduces the target monster's damage when cast.
 */
public class IceSpell extends Spell {

    /**
     * Constructs an IceSpell.
     *
     * @param name          the name of the spell
     * @param cost          the cost to buy the spell
     * @param requiredLevel the minimum level required to use the spell
     * @param baseDamage    the base damage the spell deals
     * @param manaCost      the mana required to cast the spell
     */
    public IceSpell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel, baseDamage, manaCost);
    }

    /**
     * Applies the IceSpell effect: reduces the monster's damage stat.
     *
     * @param monster the monster being attacked
     */
    @Override
    public void applySpellEffect(Monster monster) {
        int oldDamage = monster.getDamage();
        monster.setDamage((int) (oldDamage * 0.9)); // Reduce damage by 10%
    }
}
