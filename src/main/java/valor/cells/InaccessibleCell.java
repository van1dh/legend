package valor.cells;

import valor.ValorCell;

/**
 * Hard wall between lanes.
 */
public class InaccessibleCell extends ValorCell {

    @Override
    public char symbol() {
        return 'I';
    }

    @Override
    public boolean isPassableForHero() {
        return false;
    }

    @Override
    public boolean isPassableForMonster() {
        return false;
    }
}
