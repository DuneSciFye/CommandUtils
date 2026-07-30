---
description: Gives a specific mob custom drops, or takes its vanilla ones away
---

# Mob Drops

Usage: /mobdrops \<set | add> \<Entity> \<Material List>

Usage: /mobdrops \<set2 | add2> \<Entity> \<Command Separator> \<Drops>

Usage: /mobdrops \<clear | clearcustom | clearcustomdrops | novanilladrops> \<Entity>

* Entity - The entities to change
* Material List - The items to drop, e.g. `"diamond emerald emerald"`. Duplicates are allowed for weighting
* Command Separator - String that separates the commands
* Drops - Commands run on death instead of, or as well as, item drops

| Function | Effect |
| --- | --- |
| `set` | Replaces the vanilla drops with the listed items |
| `add` | Adds the listed items on top of the vanilla drops |
| `set2` | Replaces the vanilla drops with commands |
| `add2` | Adds commands on top of the vanilla drops |
| `clearcustom` / `clearcustomdrops` | Removes the custom items, keeping the vanilla setting |
| `novanilladrops` | Turns off the vanilla drops, keeping any custom ones |
| `clear` | Removes everything this command set |

The setup is stored on the mob itself, so it survives restarts and chunk unloads. This targets individual mobs — for a server-wide change, use [Mob Drop Multiplier](mob-drop-multiplier.md).

### Examples

A boss that drops a fixed loot set instead of its usual drops:

```
/mobdrops set @e[type=wither_skeleton,limit=1,sort=nearest] "netherite_ingot diamond diamond emerald"
```

Add a rare bonus drop to a mob without touching its normal loot:

```
/mobdrops add @e[type=zombie,limit=1,sort=nearest] diamond
```

Run commands when it dies instead of dropping anything:

```
/mobdrops set2 @e[type=zombie,limit=1,sort=nearest] ,, broadcastmessage &6The champion has fallen!,,weightedrandom run <9::say nothing><1::say jackpot>
```

Make a mob drop nothing at all:

```
/mobdrops novanilladrops @e[type=zombie,distance=..10]
```
