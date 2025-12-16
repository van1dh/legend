# Legends of Valor - Complete Game Implementation

## Project Overview

**Legends of Valor** is a MOBA (Multiplayer Online Battle Arena)-style game implemented in Java. Players control a team of 3 heroes battling through 3 lanes to reach the enemy's Nexus while defending their own fortress from waves of monsters.

This implementation fully complies with the CS 611 assignment specifications and includes all required features plus quality-of-life enhancements.

---

## Game Modes

### 1. Legends: Monsters & Heroes (Classic RPG)
Traditional RPG-style gameplay with heroes exploring a map, engaging in random battles, and visiting markets.

### 2. Legends of Valor (MOBA-Style)
Strategic lane-based combat with:
- 3 heroes across 3 lanes
- Terrain bonuses and strategic positioning
- Real-time monster spawning
- Win/loss conditions based on Nexus control

---

## Project Structure

```
src/
├── data/                          # Game data files
│   ├── Armory.txt                # Armor items
│   ├── Dragons.txt               # Dragon monsters
│   ├── Exoskeletons.txt          # Exoskeleton monsters
│   ├── FireSpells.txt            # Fire spells
│   ├── IceSpells.txt             # Ice spells
│   ├── LightningSpells.txt       # Lightning spells
│   ├── Paladins.txt              # Paladin heroes
│   ├── Potions.txt               # Potion items
│   ├── Sorcerers.txt             # Sorcerer heroes
│   ├── Spirits.txt               # Spirit monsters
│   ├── Warriors.txt              # Warrior heroes
│   └── Weaponry.txt              # Weapon items
│
└── main/java/
    ├── Main.java                 # Entry point
    │
    ├── character/                # Character classes
    │   ├── hero/
    │   │   ├── Hero.java         # Abstract hero class
    │   │   ├── Warrior.java      # Warrior hero
    │   │   ├── Paladin.java      # Paladin hero
    │   │   └── Sorcerer.java     # Sorcerer hero
    │   └── monster/
    │       ├── Monster.java      # Abstract monster class
    │       ├── Dragon.java       # Dragon monster
    │       ├── Exoskeleton.java  # Exoskeleton monster
    │       └── Spirit.java       # Spirit monster
    │
    ├── core/                     # Core systems
    │   └── Inventory.java        # Item management
    │
    ├── game/                     # Classic game mode
    │   ├── Game.java             # Main game controller
    │   ├── BattleManager.java    # Battle system
    │   └── Market.java           # Market interactions
    │
    ├── item/                     # Item classes
    │   ├── Item.java             # Abstract item
    │   ├── Weapon.java           # Weapons
    │   ├── Armor.java            # Armor
    │   ├── Potion.java           # Potions
    │   ├── Spell.java            # Abstract spell
    │   ├── FireSpell.java        # Fire spells
    │   ├── IceSpell.java         # Ice spells
    │   └── LightningSpell.java   # Lightning spells
    │
    ├── map/                      # Classic RPG map
    │   ├── Cell.java             # Abstract cell
    │   ├── CommonCell.java       # Common terrain
    │   ├── MarketCell.java       # Market terrain
    │   ├── InaccessibleCell.java # Blocked terrain
    │   └── GameMap.java          # Map controller
    │
    ├── util/                     # Utilities
    │   ├── FileLoader.java       # File loading
    │   └── Position.java         # Position tracking
    │
    └── valor/                    # Valor game mode
        ├── ValorGame.java        # Main controller
        ├── ValorMap.java         # 8×8 lane map
        ├── ValorActions.java     # Hero actions
        ├── ValorMonsterAI.java   # Monster behavior
        ├── ValorCell.java        # Abstract cell
        └── cells/
            ├── NexusCell.java    # Spawn points
            ├── InaccessibleCell.java # Walls
            ├── PlainCell.java    # Basic terrain
            ├── BushCell.java     # +Dexterity
            ├── CaveCell.java     # +Agility
            ├── KoulouCell.java   # +Strength
            └── ObstacleCell.java # Removable obstacles
```

---

## Quick Start

### Prerequisites
- Java 17 or higher
- Terminal/Command Prompt


### Running the Game

First, make sure you are under legend/ folder, then run following commands:

```bash
mkdir out
find src/main/java -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out Main
```

### Game Selection
```
1) Legends: Monsters & Heroes (Classic RPG)
2) Legends of Valor (MOBA-style)
Q) Quit
```

---

## Legends of Valor - Complete Features

### Map Layout (8×8 Grid)

```
Row 0: Monster Nexus (Enemy Base)
├── Columns 0-1: Top Lane
├── Column 2: Wall (Impassable)
├── Columns 3-4: Mid Lane
├── Column 5: Wall (Impassable)
├── Columns 6-7: Bot Lane
└── Row 7: Hero Nexus (Your Base)
```

### Terrain Types

| Symbol | Type | Effect |
|--------|------|--------|
| N | Nexus | Spawn point & Market access |
| I | Inaccessible | Impassable wall |
| P | Plain | No special effect |
| B | Bush | +10% Dexterity |
| C | Cave | +10% Agility |
| K | Koulou | +10% Strength |
| O | Obstacle | Removable by heroes |

**Distribution:** 20% Bush, 20% Cave, 20% Koulou, 40% Plain

### Hero Actions (All 9 Implemented)

#### 1. Move (W/A/S/D)
- **North (W):** Move towards Monster Nexus
- **South (S):** Move towards Hero Nexus
- **West (A) / East (D):** Lateral movement
- **Restrictions:**
  - Cannot move through walls (columns 2, 5)
  - Cannot share cell with another hero
  - Cannot move behind monsters without killing them
  - No diagonal movement

#### 2. Attack
- **Range:** Current cell + adjacent cells (Manhattan distance ≤ 1)
- **Requirements:** Must have equipped weapon
- **Damage:** `(Strength + Weapon Damage) × 0.05`
- **Defense:** Reduced by monster's defense stat
- **Dodge:** Monsters can dodge based on dodge chance

#### 3. Cast Spell
- **Range:** Same as attack (current + adjacent cells)
- **Requirements:** Sufficient mana
- **Types:**
  - **Fire:** Reduces monster defense by 10%
  - **Ice:** Reduces monster damage by 10%
  - **Lightning:** Reduces monster dodge by 10%
- **Damage:** `Base Damage + (Dexterity / 10000) × Base Damage`
- **Note:** Spells are single-use (consumed after casting)

#### 4. Use Potion
- **Types:**
  - Health Potions: Restore HP
  - Mana Potions: Restore mana
  - Strength Potions: Permanently increase strength
  - Agility Potions: Permanently increase agility
- **Effect:** Instant application
- **Note:** Potions are consumed after use

#### 5. Change Weapon
- **Effect:** Equip different weapon from inventory
- **Action Cost:** Uses full turn
- **Impact:** Changes attack damage

#### 6. Change Armor
- **Effect:** Equip different armor from inventory
- **Action Cost:** Uses full turn
- **Impact:** Changes damage reduction

#### 7. Teleport
- **Effect:** Move to space adjacent to hero in different lane
- **Restrictions:**
  - Only between different lanes (not within same lane)
  - Cannot teleport ahead of target hero
  - Cannot teleport behind monsters
  - Cannot teleport to occupied space
- **Note:** Hero maintains original lane identity (H1/H2/H3)
- 
#### 8. Recall
- **Effect:** Instantly return to original Nexus
- **Position:** Always returns to spawn location
- **Lane:** Returns to hero's original lane (not current lane)

#### 9. Pass Turn
- **Effect:** Skip action
- **Use Case:** Strategic positioning or waiting

### Combat Mechanics

#### Hero Combat
```
Attack Damage = (Hero Strength + Weapon Damage) × 0.05
Actual Damage = max(0, Attack Damage - Monster Defense)

Spell Damage = Spell Base + (Hero Dexterity / 10000) × Spell Base

Hero Dodge Chance = Hero Agility × 0.002 (0.2% per agility point)
```

#### Monster AI
1. **Check for hero in attack range** (current + adjacent cells)
  - If yes → Attack hero
2. **If no hero in range**
  - Move one space south (towards Hero Nexus)
3. **Cannot:**
  - Move behind other monsters
  - Move to cell with another monster
  - Move through walls

### Game Flow

#### Round Structure
1. **Heroes' Turn**
  - Each hero performs one action
  - Market transactions don't count as actions
2. **Monsters' Turn**
  - Each monster attacks (if hero in range) or moves south
3. **End of Round**
  - Living heroes regenerate 10% HP and 10% Mana
  - Dead heroes respawn at full HP and full Mana
  - Defeated monsters award gold and experience

#### Spawning Rules
- **Hero Spawning:**
  - Location: Row 7, left column of each lane
    - Top Lane: (7, 0)
    - Mid Lane: (7, 3)
    - Bot Lane: (7, 6)

- **Monster Spawning:**
  - Location: Row 0, right column of each lane
    - Top Lane: (0, 1)
    - Mid Lane: (0, 4)
    - Bot Lane: (0, 7)
  - Frequency: Every 8 rounds
  - Level: Equals highest hero level

#### Rewards (Per Monster Defeated)
- **Gold:** 500 × monster_level (given to ALL heroes)
- **Experience:** 2 × monster_level (given to ALL heroes)

### Win/Loss Conditions

#### Victory
**ANY hero reaches Monster Nexus (Row 0)**
- Triggers immediately upon entry
- Game ends instantly

#### Defeat
**ANY monster reaches Hero Nexus (Row 7)**
- Triggers immediately upon entry
- Game ends instantly

---

## Map Display Format

The game displays a detailed 8×8 grid matching the PDF specification:

```
N - N - N  N - N - N  I - I - I  N - N - N  N - N - N  I - I - I  N - N - N  N - N - N
|       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       |
N - N - N  N - N - N  I - I - I  N - N - N  N - N - N  I - I - I  N - N - N  N - N - N

P - P - P  P - P - P  I - I - I  C - C - C  P - P - P  I - I - I  B - B - B  B - B - B
|       |  |  H2   |  | X X X |  |       |  |       |  | X X X |  |       |  |       |
P - P - P  P - P - P  I - I - I  C - C - C  P - P - P  I - I - I  B - B - B  B - B - B
```

**Display Elements:**
- **H1/H2/H3:** Heroes (numbered by original lane, not current position)
- **M1/M2/M3:** Monsters (numbered by current lane)
- **Cell Borders:** Show terrain type
- **Shared Spaces:** Heroes and monsters can occupy same cell

---

## Hero Classes

### Warrior
- **Strengths:** High Strength & Agility
- **Favored Stats:** +10% Strength, +10% Agility per level
- **Best For:** Front-line combat, high physical damage
- **Starting Equipment:**
  - Legendary Greatsword (2500 DMG)
  - Titanium Heavy Armor (800 DEF)

### Paladin
- **Strengths:** Balanced Fighter
- **Favored Stats:** +10% Strength, +10% Dexterity per level
- **Best For:** Versatile combat, spell casting
- **Starting Equipment:**
  - Holy Longsword (2200 DMG)
  - Divine Plate Armor (700 DEF)

### Sorcerer
- **Strengths:** Powerful Magic User
- **Favored Stats:** +10% Dexterity, +10% Agility per level
- **Best For:** Spell damage, hit-and-run tactics
- **Starting Equipment:**
  - Arcane Staff (2000 DMG)
  - Enchanted Mage Robe (600 DEF)

### Starter Equipment (All Heroes)
- **Weapons:** Class-appropriate legendary weapon
- **Armor:** Class-appropriate heavy armor
- **Potions:**
  - 10× Super Healing Potions (+300 HP each)
  - 10× Super Mana Potions (+300 Mana each)
  - 5× Greater Strength Potions (+200 STR each)
  - 5× Greater Agility Potions (+200 AGI each)
- **Spells:**
  - 7-10× Mega Fireballs (1200 DMG, 80 mana)
  - 7-10× Frozen Nova (1100 DMG, 75 mana)
  - 7-10× Chain Lightning (1150 DMG, 70 mana)

---

## Monster Types

### Dragon
- **Special:** +10% Base Damage
- **Characteristics:** High damage output, fierce attackers

### Exoskeleton
- **Special:** +10% Defense
- **Characteristics:** Tough armor, damage resistant

### Spirit
- **Special:** +10% Dodge Chance
- **Characteristics:** Elusive, hard to hit

---

## Controls & Commands

### Movement
- **W:** Move North (towards Monster Nexus)
- **A:** Move West
- **S:** Move South (towards Hero Nexus)
- **D:** Move East

### Actions
- **1:** Move
- **2:** Attack
- **3:** Cast Spell
- **4:** Use Potion
- **5:** Change Weapon
- **6:** Change Armor
- **7:** Teleport
- **8:** Recall
- **9:** Pass Turn

### Information
- **I:** View Hero Info
- **M:** View Map
- **S:** Enter Market (if at Nexus)
- **Q:** Quit Game

---

## Technical Features

### Enhanced Hero Stats
All heroes start with **3× base stats** for balanced gameplay:
- HP: `Level × 300` (originally 100)
- Mana: `Base Mana × 3`
- Strength: `Base Strength × 2`
- Agility: `Base Agility × 2`
- Dexterity: `Base Dexterity × 2`
- Money: `Base Money × 3`

### Terrain Buff System
- **Dynamic Application:** Buffs applied on entry, removed on exit
- **Multiplier:** 1.1× (10% increase) to relevant stat
- **Temporary:** Only active while hero remains in cell
- **Stackable:** Moving between different terrain types updates buffs

### Market System
- **Location:** Hero Nexus (Row 7) for heroes
- **Actions:** Buy, Sell, Equip (doesn't consume turn)
- **Pricing:** Sell items for half purchase price
- **Requirements:** Level and gold restrictions apply

### Monster AI Behavior
```python
for each monster:
    if hero_in_attack_range():
        attack(hero)
    else:
        move_south_one_cell()
```

---

## Known Issues & Fixes

### Fixed: Teleport Identity Bug
**Problem:** Heroes changed identity (H1→H2) after teleporting

**Solution:**
- `laneIndex` now represents permanent hero identity
- Teleport only changes position, not identity
- Heroes always display as their original lane number (H1/H2/H3)

**Details:** See `BUG_FIX_TELEPORT.md` for complete analysis

---

## Testing Guide

### Test Case 1: Basic Movement
```
1. Start game, select 3 heroes
2. Move H1 north (W) - should succeed
3. Try to move H1 through wall (column 2/5) - should fail
4. Try to move H1 to cell with H2 - should fail
Pass if all restrictions work correctly
```

### Test Case 2: Combat System
```
1. Move hero adjacent to monster
2. Attack monster - should deal damage
3. Cast spell - should apply effect
4. Verify monster stats changed
Pass if damage and effects apply correctly
```

### Test Case 3: Teleport & Recall
```
1. H1 starts in Top Lane (row 7, col 0)
2. Teleport H1 to Mid Lane
3. Verify H1 still displays as "H1" (not H2)
4. Recall H1
5. Verify H1 returns to (7, 0) not Mid Lane
Pass if identity preserved and recall correct
```

### Test Case 4: Win Conditions
```
1. Move any hero to row 0 (Monster Nexus)
2. Game should end with "HEROES WIN"
3. Let monster reach row 7 (Hero Nexus)
4. Game should end with "MONSTERS WIN"
Pass if both conditions trigger correctly
```

### Test Case 5: Terrain Effects
```
1. Move hero to Bush cell (B)
2. Check dexterity increased by 10%
3. Move hero away from Bush
4. Check dexterity returned to normal
Pass if buffs apply and remove correctly
```

---

## Design Decisions

### 1. Powerful Starter Equipment
**Rationale:** Allows heroes to compete immediately against monsters without excessive grinding. Makes early game more engaging and strategic.

### 2. Simplified Monster Spawning
**Rationale:** Spawns based on highest hero level ensures appropriate challenge throughout game. Every 8 rounds provides steady pressure without overwhelming players.

### 3. Full Respawn for Heroes
**Rationale:** Heroes respawn with full HP/Mana (not half) to maintain momentum and reduce frustration. No gold penalty encourages aggressive play.

### 4. Terrain Distribution
**Rationale:** 40% Plain ensures adequate pathways while 60% special terrain (20% each type) provides strategic positioning opportunities.

### 5. Immutable Lane Identity
**Rationale:** Heroes maintain original lane number (H1/H2/H3) even after teleporting. This preserves identity and prevents confusion about which nexus to recall to.

---

## Future Enhancement

Potential achievements:
- **First Blood:** Kill first monster
- **Lane Dominator:** Clear entire lane
- **Nexus Breaker:** Win by reaching Monster Nexus
- **Last Stand:** Defend against 10+ monsters
- **Spell Master:** Cast 50 spells
- **Tank:** Absorb 10,000 damage

---

## Version History

### v2.0 - Legends of Valor (Current)
-  Complete MOBA-style gameplay
-  All 9 hero actions implemented
-  Terrain system with buffs
-  Monster AI with proper behavior
-  PDF-matching map display
-  Teleport bug fixed
-  Enhanced starter equipment

### v1.0 - Legends: Monsters & Heroes
- Classic RPG gameplay
- Random battles
- Market system
- Hero progression

---
