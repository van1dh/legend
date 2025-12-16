
package valor.cells;

import valor.ValorCell;

/**
 * Plain cell: basic walkable terrain with no special effects.
 */
public class PlainCell extends ValorCell {

    @Override
    public char symbol() {
        return 'P';
    }
}