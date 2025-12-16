# Legends: Monsters and Heroes

A simple, extensible, turn-based RPG played in the terminal.

You control a team of heroes (Warriors, Sorcerers, Paladins), move around a grid-based world, visit markets to buy/sell items, and fight monsters (Dragons, Spirits, Exoskeletons).

The goal of the assignment is **object-oriented design**: clean class relationships, scalability, and extendibility to new content.

---

## 0. Copyrights

Author: Yongyi Xie

Email: xyy0208@bu.edu

## 1. Requirements

- **JDK**: 17+
- **Build tool**: Gradle (optional – IntelliJ can also run directly)
- **IDE**: IntelliJ IDEA recommended

---

## 2. Project Structure

```text

legend/
├── build.gradle
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── game/
│   │   │   │   ├── Game.java          # Main game controller (entry point)
│   │   │   │   ├── TurnManager.java   # Turn loop, movement and triggers
│   │   │   │   ├── BattleManager.java # Battle flow (heroes vs monsters)
│   │   │   │   └── Market.java        # Market interaction logic
│   │   │   ├── character/
│   │   │   │   ├── hero/
│   │   │   │   │   ├── Hero.java
│   │   │   │   │   ├── Warrior.java
│   │   │   │   │   ├── Paladin.java
│   │   │   │   │   └── Sorcerer.java
│   │   │   │   └── monster/
│   │   │   │       ├── Monster.java
│   │   │   │       ├── Dragon.java
│   │   │   │       ├── Spirit.java
│   │   │   │       └── Exoskeleton.java
│   │   │   ├── item/
│   │   │   │   ├── Item.java
│   │   │   │   ├── Weapon.java
│   │   │   │   ├── Armor.java
│   │   │   │   ├── Potion.java
│   │   │   │   ├── Spell.java
│   │   │   │   ├── FireSpell.java
│   │   │   │   ├── IceSpell.java
│   │   │   │   ├── LightningSpell.java
│   │   │   │   ├── ItemFactory.java
│   │   │   │   └── SpellFactory.java
│   │   │   ├── core/
│   │   │   │   └── Inventory.java     # Hero inventory (weapons/armor/potions/spells)
│   │   │   ├── map/
│   │   │   │   ├── GameMap.java       # Main map grid
│   │   │   │   ├── Cell.java
│   │   │   │   ├── CommonCell.java
│   │   │   │   ├── MarketCell.java
│   │   │   │   └── InaccessibleCell.java
│   │   │   └── util/
│   │   │       ├── Position.java      # (x,y) helper for map positions
│   │   │       └── FileLoader.java    # Utility for reading data text files
│   └── data/
│       ├── Armory.txt
│       ├── Dragons.txt
│       ├── Exoskeletons.txt
│       ├── FireSpells.txt
│       ├── IceSpells.txt
│       ├── LightningSpells.txt
│       ├── Paladins.txt
│       ├── Potions.txt
│       ├── Sorcerers.txt
│       ├── Spirits.txt
│       ├── Warriors.txt
│       └── Weaponry.txt
```

---

## 3. How to Run

### Using Gradle from command line
```text
# First do wrapper
gradle wrapper

# Windows (PowerShell)
gradlew.bat run

# macOS / Linux
./gradlew run
```


## 4. Controls (How to Play)

The game is entirely text-based and controlled via keyboard input in the console.

### 4.1 Start of the game

Game loads hero data from: Warriors.txt, Paladins.txt, Sorcerers.txt

You will be prompted to choose 1–3 heroes for your team.

A map (e.g., 8×8) is generated and heroes are placed at the starting side.

### 4.2 World Map Movement

On your turn, you control each hero in order.

Movement keys:

| Key | Action     |
|-----|------------|
| W   | Move up    |
| A   | Move left  |
| S   | Move down  |
| D   | Move right |
| Q   | Quit game  |


Map cell types:

- C – Common cell (walkable, may trigger battles)

- M – Market cell (enter shop)

- X – Inaccessible cell (blocked)

If you try to move out of bounds or into an X cell, the move is rejected.

### 4.3 Market Interaction

When you move onto a MarketCell, the Market window (text menu) will appear:

| Option | Meaning                   |
|--------|---------------------------|
| B      | Buy items                 |
| S      | Sell items from inventory |
| E      |  Exit market              |


- Buy flow:

    - Items are loaded from: Weaponry.txt, Armory.txt, Potions.txt, FireSpells.txt, IceSpells.txt, LightningSpells.txt

    - You will see a numbered list of items with: Name, cost, required level, and specific stats.

    - If: Hero level ≥ required level, and Hero has enough money
      - then purchase succeeds and item is added to the hero’s Inventory.

- Sell flow:

    - Lists hero’s current items.

    - Selling usually returns some fraction of original cost (as implemented in Hero / Inventory).

## 4.4 Battle System

Entering certain cells can trigger battles (controlled by BattleManager):

- Heroes and monsters take turns.

- Monsters are generated from: Dragons.txt, Spirits.txt, Exoskeletons.txt.

Typical hero actions in battle:

| Action key | Description                                |
|------------|--------------------------------------------|
| A          | Normal attack with current weapon          |
| S          | Cast a spell (Fire/Ice/Lightning)          |
| P          |   Use a potion from inventory              |
| Q          | Attempt to flee (implementation-dependent) |

Spells apply additional effects:

- FireSpell: reduce monster defense

- IceSpell: reduce monster damage

- LightningSpell: reduce monster dodge chance

Monsters attack back based on their damage and hero defense.

Battle ends when: All monsters are dead (heroes win), or All heroes are dead (game over / defeat for that party).

Rewards such as gold and experience are granted on victory. Level ups are handled in the Hero subclasses and affect stats differently per hero type.

## 5. Data Files and Format

All .txt files are under src/data/ and are loaded by FileLoader.

Example formats:

### 5.1 Weapons (Weaponry.txt)
```text
Name              Cost  Level  Damage  Hands
Sword             500   1      800     1
Axe               550   2      850     2
```



Mapped to:
```text
new Weapon(name, cost, level, damage, hands);
```


### 5.2 Armor (Armory.txt)
```text
Name               Cost  Level  DamageReduction
Platinum_Shield    150   1      200
Breastplate        350   3      600
```



Mapped to:
```text
new Armor(name, cost, level, damageReduction);
```


### 5.3 Potions (Potions.txt)
```text
Name             Cost  Level  EffectAmount   AffectedAttributes...
Healing_Potion   100   1      100            Health
Strength_Potion  200   2      75             Strength Agility
```



Mapped to:
```text
new Potion(name, cost, level, effectAmount, Set<String> affectedAttributes);
```


### 5.4 Spells (FireSpells.txt, IceSpells.txt, LightningSpells.txt)
```text
Name          Cost  Level  Damage  ManaCost  SpecialValue
Flame_Tornado 300   2      400     100       50
```



Mapped to:
```text
new FireSpell(name, cost, level, damage, manaCost);
```

(similarly for IceSpell / LightningSpell)

### 5.5 Heroes (Warriors.txt, Paladins.txt, Sorcerers.txt)
```text
Name       Mana  Strength  Agility  Dexterity  Money  Experience
Gaerdal    100   700       500      600        1354   7
```

Mapped to:
```text
new Warrior(name, mana, strength, agility, dexterity, money, experience);
```


### 5.6 Monsters (Dragons.txt, Spirits.txt, Exoskeletons.txt)
```text
Name       Level  Damage  Defense  DodgeChance
Natsunomeryu   1      100     200      35
```



Mapped to:
```text
new Dragon(name, level, damage, defense, dodgeChance);
```


## 6. Design Notes (Scalability & Extensibility)

### Heroes and Monsters

Common behavior is captured in abstract base classes:

- Hero and Monster

    - Specific types (Warrior, Paladin, Sorcerer, Dragon, Spirit, Exoskeleton) extend these base classes and override:

- Level-up logic

    - Special stats or behaviors

- Items and Spells

    - Item is the base type for Weapon, Armor, Potion, Spell.

    - Spells use polymorphism (Spell → FireSpell, IceSpell, LightningSpell) so new spell types can be added easily.

- Map & Cells

    - GameMap holds a 2D array of Cell references.

    - Each specific cell type (CommonCell, MarketCell, InaccessibleCell) decides: isAccessible(), isMarket(), toChar() for display. This makes it easy to add new map cell types later (e.g., “Inn”, “Portal”).

- Game Flow

    - Game creates and wires up core components.

    - TurnManager controls turn order, movement, and triggers.

    - BattleManager encapsulates combat.

    - Market encapsulates all buying/selling logic.

This structure keeps responsibilities separated and makes it easier to add:

- New hero/monster/item types

- New cell types or map rules

- Additional game modes or victory conditions

without rewriting core logic.

7. Known Limitations / Possible Extensions

- Basic battle logic (no complex AI).

- No save/load system.

- Limited validation of user input (can be further hardened).

- Balancing of stats (damage/hp/money) is simple and can be improved for fairness.