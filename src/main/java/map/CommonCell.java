package map;

/**
 * A walkable, non-market terrain cell.
 */
public class CommonCell extends Cell {

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public char toChar() {
        return 'C';   // Common terrain cell
    }
}

