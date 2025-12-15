package valor;

/**
 * Base class for all cells in Legends of Valor map.
 */
public abstract class ValorCell {

    /**
     * Symbol for printing on the map.
     */
    public abstract char symbol();

    /**
     * Whether a hero can enter this cell.
     */
    public boolean isPassableForHero() {
        return true;
    }

    /**
     * Whether a monster can enter this cell.
     */
    public boolean isPassableForMonster() {
        return true;
    }

    /**
     * Called when hero enters this cell (for buffs etc.).
     */
    public void onHeroEnter(core.hero.Hero hero) {
        // default no-op
    }

    /**
     * Called when hero leaves this cell (remove buffs).
     */
    public void onHeroLeave(core.hero.Hero hero) {
        // default no-op
    }
}
