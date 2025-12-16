package map;

/**
 * Represents a market cell that is accessible and has special function.
 */
public class MarketCell extends Cell {

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public boolean isMarket() {
        return true;
    }

    @Override
    public char toChar() {
        return 'M';
    }
}
