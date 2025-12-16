package valor.cells;

import valor.ValorCell;
import character.hero.Hero;

/**
 * Bush cell: increases hero's dexterity while standing in this cell.
 */
public class BushCell extends ValorCell {

    private static final double BUFF_MULTIPLIER = 1.1;

    @Override
    public char symbol() {
        return 'B';
    }

    @Override
    public void onHeroEnter(Hero hero) {
        hero.applyTemporaryDexterityBuff(BUFF_MULTIPLIER);
    }

    @Override
    public void onHeroLeave(Hero hero) {
        hero.removeTemporaryDexterityBuff(BUFF_MULTIPLIER);
    }
}
