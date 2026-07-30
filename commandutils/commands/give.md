---
description: Gives an item to a player, handling a full inventory
---

# Give

Usage: /give \<Player> \<Item> \[\<Amount>] \[\<Drop Excess>]

* Player - The player to give to
* Item - The item, with optional NBT such as `diamond_sword[enchantments={sharpness:5}]`
* Amount _(optional)_ - How many. Defaults to `1`
* Drop Excess _(optional)_ - Whether items that don't fit drop on the ground. Defaults to `true`

With `Drop Excess` set to `false`, leftovers go to the player's LeafAPI overflow storage if that plugin is installed, and are otherwise discarded — useful when dropping loot on the floor would be worse than holding it back.

### Examples

```
/give Steve diamond 64
```

An enchanted item:

```
/give Steve diamond_pickaxe[enchantments={efficiency:5,unbreaking:3}] 1
```

Give without ever littering the floor:

```
/give Steve emerald 16 false
```
