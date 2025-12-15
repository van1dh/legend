package item;

import character.Monster;

/**
 * LightningSpell is a type of Spell that reduces the target monster's dodge chance.
 */
public class LightningSpell extends Spell {

    /**
     * Constructs a LightningSpell.
     *
     * @param name          the name of the spell
     * @param cost          the cost to buy the spell
     * @param requiredLevel the minimum level required to use the spell
     * @param baseDamage    the base damage the spell deals
     * @param manaCost      the mana required to cast the spell
     */
    public LightningSpell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel, baseDamage, manaCost);
    }

    /**
     * Applies the LightningSpell effect: reduces the monster's dodge chance.
     *
     * @param monster the monster being attacked
     */
    @Override
    public void applySpellEffect(Monster monster) {
        double oldDodge = monster.getDodgeChance();
        monster.setDodgeChance(oldDodge * 0.9); // Reduce dodge chance by 10%
    }
}
