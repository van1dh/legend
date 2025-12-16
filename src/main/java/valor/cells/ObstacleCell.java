package valor.cells;

import valor.ValorCell;

/**
 * Obstacle cell: blocks heroes and monsters until removed by hero action.
 */
public class ObstacleCell extends ValorCell {

    @Override
    public char symbol() {
        return 'O';
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
