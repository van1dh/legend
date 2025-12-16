package map;

/**
 * Abstract representation of a map cell.
 * Each cell type defines whether it is accessible, whether it's a market,
 * and what symbol it displays on the map.
 */
public abstract class Cell {

    /**
     * Whether the cell can be entered by heroes.
     */
    public abstract boolean isAccessible();

    /**
     * Whether the cell is a market (default: false).
     */
    public boolean isMarket() {
        return false;
    }

    /**
     * Symbol used when printing the map.
     */
    public abstract char toChar();
}

