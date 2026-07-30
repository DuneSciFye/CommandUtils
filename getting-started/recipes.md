---
icon: book-open
description: Worked examples that combine several commands
---

# Recipes

Each recipe is a command line you can hand to whatever plugin triggers it. `%player%`, `%block_x%` and friends are filled in by that plugin before CommandUtils sees them.

## An excavator that smelts what it mines

```
/selectblocksfacing %world% %block_x% %block_y% %block_z% %player% 1 1 pickaxe ; % true BLOCK:BREAK;ITEM:SMELT;ITEM:AUTO_PICKUP;ITEM:DROP
```

A 3×3 wall in front of the player. See [Select Blocks](../commandutils/commands/select-blocks.md) for the full function list.

## A harvester hoe

```
/selectblocks %world% %block_x% %block_y% %block_z% %player% 2 "#crops" ; % true BLOCK:CONDITION:FULLY_GROWN;BLOCK:AUTO_REPLANT;ITEM:AUTO_PICKUP;ITEM:DROP
```

Only fully grown crops are taken, each is replanted, and one seed is paid out of the drops.

## An ability on a cooldown

```
/cooldowncommand run %player% dash 15s "modifyvelocity %player% multiply 3,,silentparticle cloud %player_x% %player_y% %player_z% 0.3 0.3 0.3 0 20"
```

While the cooldown is running, the player gets a message in their action bar instead. Use `silent` to say nothing at all.

## A channelled cast

```
/loop add cast-%player% 60 0t 1t sendbossbar %player% cast BLUE 1.0 3 &bChannelling...,,silentparticle enchant $player_x$ $player_y$ $player_z$ 0.5 1 0.5 0 5
/stun %player% 3s
```

Three seconds of particles and a boss bar while the player is held in place. Cancel it early with `/loop cancel cast-%player%`.

## A weighted loot drop

```
/weightedrandom run <60::give %player% coal 1><30::give %player% iron_ingot 1><9::give %player% diamond 1><1::give %player% netherite_scrap 1|broadcastmessage &6%player% struck it rich!>
```

Exactly one entry runs, with the chance set by its weight against the total.

## A buff that doesn't clobber potions

```
/effect give %player% strength infinite 1 armor_set
```

and when the armour comes off:

```
/effect remove %player% armor_set
```

Any strength the player already had from a potion takes back over, with the time it had left. See [Effect](../commandutils/commands/effect.md).

## An arrow that explodes where it lands

```
/execute as %player% run launchprojectile ARROW 30s ,, spawnnodamagelightning %world% {projectile_x} {projectile_y} {projectile_z}
```

## Temporary cover

```
/settempblock %world% %block_x% %block_y% %block_z% cobblestone 5s true
```

A wall that cracks apart over five seconds and puts the original block back — nothing to clean up.

## A drop-rate event

```
/mobdropmultiplier set @a 2 "WITHER ENDER_DRAGON WARDEN"
/xpdropmultiplier set @a 2
```

and to end it:

```
/mobdropmultiplier clear @a
/xpdropmultiplier clear @a
```
