package map;

/**
 * A cell that cannot be entered, such as mountains or blocked terrain.
 */
public class InaccessibleCell extends Cell {

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public char toChar() {
        return 'X';
    }
}

