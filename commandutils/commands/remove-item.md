---
description: Takes items out of a player's inventory and reports how many it found
---

# Remove Item

Usage: /removeitem \<Player> \<Item> \[\<Max Amount>] \[\<Strict>] \[\<Check Chest>] \[\<Commands>] \[\<No Commands If Zero>] \[\<Command Separator>]

Usage: /removeitem \<Material List> \[\<Min Amount>] \[\<Max Amount>] \[\<Vanilla>] \[\<Check Chest>] \[\<Command Separator>] \[\<Commands>]

* Player - The player to take from
* Item - The item to match
* Material List - Materials to match, for the second form. That form runs as the sender, so use `/execute as <player>`
* Max Amount _(optional)_ - Stop after removing this many. Defaults to no limit
* Min Amount _(optional)_ - Remove nothing unless at least this many were found
* Strict _(optional)_ - `true` requires an exact match including name, lore and NBT. `false` matches the material, and the potion type for potions. Defaults to `false`
* Vanilla _(optional)_ - `true` skips any item that has custom NBT. Defaults to `false`
* Check Chest _(optional)_ - Also take from the container the player currently has open. Defaults to `false`
* Commands _(optional)_ - Commands run afterwards, separated by `,,`
* No Commands If Zero _(optional)_ - Skip the commands when nothing was found. Defaults to `false`
* Command Separator _(optional)_ - Replaces `,,`

`{amount}` in the commands is replaced with how many items were actually removed, which is what makes "sell all" and "pay per item" flows possible in one command. PlaceholderAPI placeholders are parsed for the player as well.

The cursor slot is checked along with the inventory.

### Examples

Take up to 64 diamonds and pay for each one:

```
/removeitem Steve diamond 64 false false "eco give Steve {amount}00" true
```

A sell-all sign that only accepts plain, uncustomised ores:

```
/execute as Steve run removeitem "iron_ingot gold_ingot" 1 999 true true ,, eco give Steve {amount}0
```

Take one exact custom item, matching its name and NBT:

```
/removeitem Steve "paper[custom_name='\"Raid Ticket\"']" 1 true
```
