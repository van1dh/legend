# Legends - Complete Game Implementation

## Project Overview

Legends consists of two games: **Legends: Monsters & Heroes** and **Legends of Valor**.

**Legends: Monsters & Heroes** is a traditional turn-based RPG where heroes explore a randomly generated world, engage in tactical battles, visit markets to buy equipment, and grow stronger through experience. Unlike the objective-based Valor mode, this classic mode focuses on character progression, resource management, and tactical combat mastery.

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
find src/main/java -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out Main
```

 If needed clear the out first:
```bash
rm -rf out
mkdir out
```

### Game Selection
```
1) Legends: Monsters & Heroes (Classic RPG)
2) Legends of Valor (MOBA-style)
Q) Quit
```

---

## Legends: Monsters & Heroes - Complete Features

### Map Layout

- **Size:** 8×8 grid (configurable)
- **Generation:** Randomized each game
- **Cell Types:**
  - **Common (C):** Walkable terrain with 30% battle chance
  - **Market (M):** Safe zones for shopping
  - **Inaccessible (X):** Blocked terrain (mountains, walls, water)

### Cell Distribution
```
20% Inaccessible (X)
30% Market (M)
50% Common (C)
```

### Hero Display
- **H1:** First hero in party
- **H2:** Second hero in party
- **H3:** Third hero in party

### Example Map
```
H1 C  M  X  C  M  C  C
C  M  C  C  X  C  M  C
M  C  C  M  C  X  C  C
C  X  M  C  C  C  M  X
C  C  C  C  M  C  C  C
M  C  X  C  C  M  X  C
C  C  C  M  C  C  C  M
C  M  C  C  X  C  M  C
```

---

### Controls & Movement

#### Exploration Controls
```
W/w - Move Up (North)
A/a - Move Left (West)
S/s - Move Down (South)
D/d - Move Right (East)
I/i - View Hero Stats
P/p - View Map
V/v - View Inventory
M/m - Enter Market (when on market cell)
Q/q - Quit Game
```

#### Movement Rules
- Can move to Common cells (may trigger battle)
- Can move to Market cells (safe, no battles)
- Cannot move to Inaccessible cells
- Cannot move outside map boundaries
- Each hero moves individually per turn

---

### Battle System

#### Battle Trigger
- **Location:** Common cells (marked 'C')
- **Chance:** 30% when entering a common cell
- **Monsters:** Equal to number of heroes in party
- **Level:** Near highest hero level

#### Turn Structure

```
┌─────────────────────────┐
│ ROUND N                 │
├─────────────────────────┤
│ 1. Heroes' Turn         │
│    - Hero 1 acts        │
│    - Hero 2 acts        │
│    - Hero 3 acts        │
│                         │
│ 2. Monsters' Turn       │
│    - Monster 1 attacks  │
│    - Monster 2 attacks  │
│    - Monster 3 attacks  │
│                         │
│ 3. End of Round         │
│    - Regen 10% HP       │
│    - Regen 10% Mana     │
│                         │
│ Repeat until victory    │
│ or defeat               │
└─────────────────────────┘
```

#### Hero Actions in Battle

**1. Attack with Weapon (Action 1)**
- Uses equipped weapon
- Costs: 0 mana
- Formula: `(Strength + Weapon Damage) × 0.05`
- Modified by: Monster defense, dodge chance

**2. Cast Spell (Action 2)**
- Uses mana from pool
- Costs: Varies by spell (70-600 mana)
- Formula: `Base Damage + (Dexterity / 10000) × Base Damage`
- Effects:
  - **Fire:** Damage + reduce defense 10%
  - **Ice:** Damage + reduce attack 10%
  - **Lightning:** Damage + reduce dodge 10%
-  **Single Use:** Spells consumed after casting!

**3. Use Potion (Action 3)**
- Instant effect
- Consumes potion from inventory
- Types:
  - Health: Restore HP
  - Mana: Restore mana
  - Strength: Permanently increase strength
  - Agility: Permanently increase agility

**4. View Stats (Action 4)**
- **FREE ACTION** - Doesn't use turn
- Shows all heroes and monsters
- Displays HP/Mana bars
- Shows damage and defense stats

**Q. Quit Battle**
- Forfeit battle with confirmation
- Returns to previous state
- Can quit anytime

#### Combat Formulas

```java
// Physical Attack Damage
Attack = (Hero Strength + Weapon Damage) × 0.05
Actual Damage = max(0, Attack - Monster Defense)

// Spell Damage
Spell Damage = Base + (Hero Dexterity / 10000) × Base

// Hero Dodge Chance
Dodge % = Hero Agility × 0.002  // 0.2% per point

// Monster Dodge Chance
Dodge % = From data file (10-90%)

// Regeneration (End of Round)
HP Regen = Current HP × 1.1 (capped at Max HP)
Mana Regen = Current Mana × 1.1 (capped at Max Mana)
```

#### Battle Outcomes

##### Victory (All Monsters Defeated)
**Rewards (to ALL heroes):**
- Gold: `100 × monster_level` per monster
- Experience: `2 points` per monster

**Example:**
```
3 Monsters defeated:
- Level 3 Dragon: 300 gold, 2 exp
- Level 3 Spirit: 300 gold, 2 exp
- Level 2 Exo: 200 gold, 2 exp
Total: 800 gold, 6 exp (to each hero)
```

**Fallen Heroes:**
- Revive at 50% HP
- Revive at 50% Mana

##### Defeat (All Heroes at 0 HP)
- **Game Over**
- Display final statistics
- Return to main menu

---

### Market System

#### Accessing Markets

**Location:** Any cell marked 'M' on the map

**Entry:**
1. Move hero to market cell
2. Press 'M' to enter

**Features:**
- Safe zone (no random battles)
- Unlimited shopping time
- Actions don't consume hero turns
- All transactions instant

#### Market Menu

```
╔════════════════════════════════╗
║   WELCOME TO THE MARKET        ║
╚════════════════════════════════╝
Hero: [Name]
Gold: [Amount]

--- MARKET MENU ---
1) Buy items
2) Sell items
3) Equip weapon
4) Equip armor
5) View inventory
6) Exit market
Q) Quit game

Choice:
```

#### 1. Buy Items

**Categories Available:**
- Weapons
- Armor
- Potions
- Spells (Fire, Ice, Lightning)

**Requirements:**
- Sufficient gold
- Required level met

**Display Format:**
```
[OK] Item Name (Cost: X, Level: Y, Stats...)
[X]  Item Name (Cost: X, Level: Y, Stats...)
     ↑ Cannot afford or level too low
```

#### 2. Sell Items

**Sell Price:** 50% of purchase cost

**Example:**
```
Purchase: Sword for 500 gold
Sell: Sword for 250 gold
```

**All Item Types Can Be Sold:**
- Weapons
- Armor
- Potions (even if partially consumed)
- Spells (even if learned)

#### 3. Equip Weapon

- View owned weapons
- Select to equip
- Changes attack damage immediately
- Shows currently equipped with [EQUIPPED] tag

#### 4. Equip Armor

- View owned armor pieces
- Select to equip
- Changes damage reduction immediately
- Shows currently equipped with [EQUIPPED] tag

#### 5. View Inventory

Complete organized listing:
```
Weapons: [count]
- Weapon 1 (Damage: X, Hands: Y)
- Weapon 2 (Damage: X, Hands: Y)

Armors: [count]
- Armor 1 (Defense: X)
- Armor 2 (Defense: X)

Potions: [count]
- Potion 1 (Effect: +X to Attribute)
- Potion 2 (Effect: +X to Attribute)

Spells: [count]
- Spell 1 (Damage: X, Mana: Y)
- Spell 2 (Damage: X, Mana: Y)
```

#### Available Items

##### Weapons (Weaponry.txt)
| Name | Cost | Level | Damage | Hands |
|------|------|-------|--------|-------|
| Dagger | 200 | 1 | 250 | 1 |
| Sword | 500 | 1 | 800 | 1 |
| Bow | 300 | 2 | 500 | 2 |
| Axe | 550 | 5 | 850 | 1 |
| Scythe | 1000 | 6 | 1100 | 2 |
| TSwords | 1400 | 8 | 1600 | 2 |

##### Armor (Armory.txt)
| Name | Cost | Level | Defense |
|------|------|-------|---------|
| Platinum_Shield | 150 | 1 | 200 |
| Breastplate | 350 | 3 | 600 |
| Full_Body_Armor | 1000 | 8 | 1100 |
| Wizard_Shield | 1200 | 10 | 1500 |
| Guardian_Angel | 1000 | 10 | 1000 |

##### Potions (Potions.txt)
| Name | Cost | Level | Effect | Attribute |
|------|------|-------|--------|-----------|
| Healing_Potion | 250 | 1 | +100 | Health |
| Strength_Potion | 200 | 1 | +75 | Strength |
| Magic_Potion | 350 | 2 | +100 | Mana |
| Luck_Elixir | 500 | 4 | +65 | Agility |
| Mermaid_Tears | 850 | 5 | +100 | Health/Mana/Str/Agi |
| Ambrosia | 1000 | 8 | +150 | All Stats |

##### Fire Spells (FireSpells.txt)
| Name | Cost | Level | Damage | Mana | Effect |
|------|------|-------|--------|------|--------|
| Breath_of_Fire | 350 | 1 | 450 | 100 | -10% DEF |
| Heat_Wave | 450 | 2 | 600 | 150 | -10% DEF |
| Hell_Storm | 600 | 3 | 950 | 600 | -10% DEF |
| Flame_Tornado | 700 | 4 | 850 | 300 | -10% DEF |
| Lava_Comet | 800 | 7 | 1000 | 550 | -10% DEF |

##### Ice Spells (IceSpells.txt)
| Name | Cost | Level | Damage | Mana | Effect |
|------|------|-------|--------|------|--------|
| Ice_Blade | 250 | 1 | 450 | 100 | -10% DMG |
| Snow_Cannon | 500 | 2 | 650 | 250 | -10% DMG |
| Frost_Blizzard | 750 | 5 | 850 | 350 | -10% DMG |
| Arctic_Storm | 700 | 6 | 800 | 300 | -10% DMG |

##### Lightning Spells (LightningSpells.txt)
| Name | Cost | Level | Damage | Mana | Effect |
|------|------|-------|--------|------|--------|
| Lightning_Dagger | 400 | 1 | 500 | 150 | -10% Dodge |
| Spark_Needles | 500 | 2 | 600 | 200 | -10% Dodge |
| Thunder_Blast | 750 | 4 | 950 | 400 | -10% Dodge |
| Electric_Arrows | 550 | 5 | 650 | 200 | -10% Dodge |

---

### Hero Classes

#### Warrior
**From:** Warriors.txt

**Base Statistics:**
- Mana: 100-600 (Lower range)
- Strength: 700-900 (Highest)
- Agility: 400-800 (High)
- Dexterity: 500-750 (Moderate)
- Money: 1354-2546 gold
- Experience: 6-8

**Level Up Bonuses:**
- Base: +5% all stats
- Favored: +10% Strength
- Favored: +10% Agility

**Playstyle:**
- Front-line fighter
- High physical damage
- Tank role
- Best for melee combat

**Available Heroes:**
- Gaerdal_Ironhand
- Sehanine_Monnbow
- Muamman_Duathall
- Flandal_Steelskin
- Undefeated_Yoj
- Eunoia_Cyn

**Starter Equipment:**
- Weapon: Legendary Greatsword (2500 DMG, 2-handed)
- Armor: Titanium Heavy Armor (800 DEF)

#### Paladin
**From:** Paladins.txt

**Base Statistics:**
- Mana: 100-500 (Moderate)
- Strength: 500-750 (Good)
- Agility: 500-700 (Good)
- Dexterity: 350-700 (Varied)
- Money: 2500 gold
- Experience: 4-8

**Level Up Bonuses:**
- Base: +5% all stats
- Favored: +10% Strength
- Favored: +10% Dexterity

**Playstyle:**
- Balanced fighter
- Good spell caster
- Versatile
- Best for mixed strategy

**Available Heroes:**
- Parzival
- Sehanine_Moonbow
- Skoraeus_Stonebones
- Garl_Glittergold
- Amaryllis_Astra
- Caliber_Heist

**Starter Equipment:**
- Weapon: Holy Longsword (2200 DMG, 1-handed)
- Armor: Divine Plate Armor (700 DEF)

#### Sorcerer
**From:** Sorcerers.txt

**Base Statistics:**
- Mana: 800-1300 (Highest)
- Strength: 700-850 (Moderate)
- Agility: 400-800 (High)
- Dexterity: 400-800 (High)
- Money: 2500 gold
- Experience: 5-9

**Level Up Bonuses:**
- Base: +5% all stats
- Favored: +10% Dexterity
- Favored: +10% Agility

**Playstyle:**
- Spell damage focus
- High mana pool
- Glass cannon
- Best for magical combat

**Available Heroes:**
- Rillifane_Rallathil
- Segojan_Earthcaller
- Reign_Havoc
- Reverie_Ashels
- Kalabar
- Skye_Soar

**Starter Equipment:**
- Weapon: Arcane Staff (2000 DMG, 1-handed)
- Armor: Enchanted Mage Robe (600 DEF)

#### Enhanced Starter Inventory

**All Heroes Start With:**

**Consumables:**
- 10× Super Healing Potions (+300 HP each)
- 10× Super Mana Potions (+300 Mana each)
- 5× Greater Strength Potions (+200 STR each)
- 5× Greater Agility Potions (+200 AGI each)

**Spells:**
- Warriors/Paladins: 7× each spell type
- Sorcerers: 10× each spell type

Spells included:
- Mega Fireballs (1200 DMG, 80 mana)
- Frozen Nova (1100 DMG, 75 mana)
- Chain Lightning (1150 DMG, 70 mana)

---

### Monster Types

#### Dragon
**From:** Dragons.txt

**Special Trait:** +10% Base Damage (favored stat)

**Characteristics:**
- High offensive capability
- Moderate defense
- Lower dodge chance

#### Exoskeleton
**From:** Exoskeletons.txt

**Special Trait:** +10% Defense (favored stat)

**Characteristics:**
- Very high defense
- Moderate damage
- Battles take longer

#### Spirit
**From:** Spirits.txt

**Special Trait:** +10% Dodge Chance (favored stat)

**Characteristics:**
- Very evasive
- Moderate damage
- Frustrating to hit

---

### Progression System

#### Experience & Leveling

**XP Formula:**
```
Level 1 → 2: 10 XP needed
Level 2 → 3: 20 XP needed (30 total)
Level 3 → 4: 30 XP needed (60 total)
Level N → N+1: N × 10 XP needed
```

**Sources of Experience:**
- Monster defeats: 2 XP per monster
- Given to ALL heroes in party
- Level up when threshold reached

**Example Progression:**
```
Start: Level 1 (0/10 XP)
Kill 5 monsters: Level 1 (10/10 XP) → Level Up!
Now: Level 2 (0/20 XP)
Kill 10 monsters: Level 2 (20/20 XP) → Level Up!
Now: Level 3 (0/30 XP)
```

#### Level Up Benefits

**All Heroes:**
- HP: Recalculated as `Level × 300`, fully healed
- Mana: Increases by 10%, fully restored
- All Stats: Increase by 5%

**Class-Specific Bonuses:**
- Warriors: +10% Strength, +10% Agility
- Paladins: +10% Strength, +10% Dexterity
- Sorcerers: +10% Dexterity, +10% Agility

**Example:**
```
Warrior Level 5:
- Base Strength: 1000
- Level up to 6:
  - All stats +5%: 1000 → 1050
  - Warrior bonus +10%: 1050 → 1155
  - New Strength: 1155
```

#### Gold Economy

**Earning:**
- Monster kills: `100 × monster_level` per monster
- Selling items: 50% of purchase price
- Rewards given to ALL heroes

**Early Game Budget (Level 1-3):**
```
Starting Gold: ~2500-7500 (starter equipment)
First Upgrade: 500-800 (better weapon)
Essential Items: 1000-1500 (potions, basic spells)
Reserve: 1000+ (for emergencies)
```

**Mid Game Budget (Level 4-6):**
```
Target Gold: 5000+
Upgrades: 1500-2000 (level 5-6 equipment)
Supplies: 2000-3000 (potions, spells stock)
Reserve: 1500+
```

**Late Game Budget (Level 7-10):**
```
Target Gold: 10000+
Best Equipment: 1000-1400 per piece
Full Supply: 3000-5000 (all consumables)
Comfortable Reserve: 2000+
```

---

### Quick Reference

#### Damage Formulas
```
Physical: (STR + Weapon) × 0.05 - Monster DEF
Magical: Base + (DEX / 10000) × Base
Dodge: AGI × 0.002 (heroes), Data% (monsters)
```

#### Regen Formula
```
HP: Current × 1.1 (max at Max HP)
Mana: Current × 1.1 (max at Max Mana)
```

#### Rewards
```
Gold: 100 × monster_level (per monster, all heroes)
EXP: 2 (per monster, all heroes)
```

#### Level Requirements
```
Level N→N+1: N × 10 XP
Example: Level 5→6 requires 50 XP
```

#### Sell Prices
```
All Items: 50% of purchase cost
Example: Sword (500g) sells for 250g
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

### Map Display Format

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

### Hero Classes

#### Warrior
- **Strengths:** High Strength & Agility
- **Favored Stats:** +10% Strength, +10% Agility per level
- **Best For:** Front-line combat, high physical damage
- **Starting Equipment:**
  - Legendary Greatsword (2500 DMG)
  - Titanium Heavy Armor (800 DEF)

#### Paladin
- **Strengths:** Balanced Fighter
- **Favored Stats:** +10% Strength, +10% Dexterity per level
- **Best For:** Versatile combat, spell casting
- **Starting Equipment:**
  - Holy Longsword (2200 DMG)
  - Divine Plate Armor (700 DEF)

#### Sorcerer
- **Strengths:** Powerful Magic User
- **Favored Stats:** +10% Dexterity, +10% Agility per level
- **Best For:** Spell damage, hit-and-run tactics
- **Starting Equipment:**
  - Arcane Staff (2000 DMG)
  - Enchanted Mage Robe (600 DEF)

#### Starter Equipment (All Heroes)
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

### Monster Types

#### Dragon
- **Special:** +10% Base Damage
- **Characteristics:** High damage output, fierce attackers

#### Exoskeleton
- **Special:** +10% Defense
- **Characteristics:** Tough armor, damage resistant

#### Spirit
- **Special:** +10% Dodge Chance
- **Characteristics:** Elusive, hard to hit

---

### Controls & Commands

#### Movement
- **W:** Move North (towards Monster Nexus)
- **A:** Move West
- **S:** Move South (towards Hero Nexus)
- **D:** Move East

#### Actions
- **1:** Move
- **2:** Attack
- **3:** Cast Spell
- **4:** Use Potion
- **5:** Change Weapon
- **6:** Change Armor
- **7:** Teleport
- **8:** Recall
- **9:** Pass Turn

#### Information
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

