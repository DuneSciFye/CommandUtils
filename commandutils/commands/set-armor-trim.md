---
description: Applies or clears an armour trim
---

# Set Armor Trim

Usage: /setarmortrim \<Player> \<[Slot](../arguments/slot-argument.md)> \<Material> \<Pattern>

Usage: /setarmortrim \<Player> \<[Slot](../arguments/slot-argument.md)> \<none>

* Player - The player holding the armour
* Slot - Slot of the armour piece
* Material - Trim material, e.g. `gold`, `diamond`, `netherite`, `amethyst`
* Pattern - Trim pattern, e.g. `sentry`, `vex`, `wild`, `spire`
* none - Removes the trim

No smithing table, template or ingot is consumed. Items that aren't armour are ignored.

Both arguments are suggested from the server's registries, so modded and datapack trims work too.

### Examples

Gold sentry trim on the held helmet:

```
/setarmortrim Steve mainhand gold sentry
```

Trim the chestplate the player is wearing:

```
/setarmortrim Steve 102 netherite spire
```

Clear the trim:

```
/setarmortrim Steve mainhand none
```
