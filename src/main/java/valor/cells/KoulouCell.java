package valor.cells;

import valor.ValorCell;
import core.character.hero.Hero;

/**
 * Koulou cell: increases hero's strength while standing in this cell.
 */
public class KoulouCell extends ValorCell {

    private static final double BUFF_MULTIPLIER = 1.1;

    @Override
    public char symbol() {
        return 'K';
    }

    @Override
    public void onHeroEnter(Hero hero) {
        hero.applyTemporaryStrengthBuff(BUFF_MULTIPLIER);
    }

    @Override
    public void onHeroLeave(Hero hero) {
        hero.removeTemporaryStrengthBuff(BUFF_MULTIPLIER);
    }
}
