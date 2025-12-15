package valor.cells;

import valor.ValorCell;
import core.hero.Hero;

/**
 * Cave cell: increases hero's agility while standing in this cell.
 */
public class CaveCell extends ValorCell {

    private static final double BUFF_MULTIPLIER = 1.1;

    @Override
    public char symbol() {
        return 'C';
    }

    @Override
    public void onHeroEnter(Hero hero) {
        hero.applyTemporaryAgilityBuff(BUFF_MULTIPLIER);
    }

    @Override
    public void onHeroLeave(Hero hero) {
        hero.removeTemporaryAgilityBuff(BUFF_MULTIPLIER);
    }
}
