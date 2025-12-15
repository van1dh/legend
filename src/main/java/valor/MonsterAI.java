package valor;

import java.util.List;

import core.hero.Hero;
import core.monster.Monster;

/**
 * Very simple monster AI for Valor:
 * - If any hero is within attack range (same lane, distance <= 1), attack.
 * - Otherwise move one step forward (towards heroes' nexus) if possible.
 */
public class MonsterAI {

    public static void processMonsters(ValorMap map, List<Monster> monsters, List<Hero> heroes) {
        for (Monster m : monsters) {
            if (!m.isAlive()) continue;

            // 1) Check if any hero in attack range
            Hero target = findAttackableHero(m, heroes);
            if (target != null) {
                m.attack(target); // 使用你已有的 attack 逻辑
                System.out.println("Monster " + m.getName() + " attacks " + target.getName());
                continue;
            }

            // 2) Move one step down (towards hero nexus)
            int row = m.getRow();
            int col = m.getCol();
            int newRow = row + 1;
            if (map.isAccessibleForMonster(newRow, col)) {
                m.setPosition(newRow, col);
            }
        }
    }

    private static Hero findAttackableHero(Monster m, List<Hero> heroes) {
        int mr = m.getRow();
        int mc = m.getCol();
        for (Hero h : heroes) {
            if (!h.isAlive()) continue;
            int hr = h.getRow();
            int hc = h.getCol();
            if (Math.abs(mr - hr) + Math.abs(mc - hc) <= 1) {
                return h;
            }
        }
        return null;
    }
}
