---
description: Stops chosen mob types from targeting a player
---

# Mob Target Team

Usage: /mobtargetteam \<set> \<Entity Types>

Usage: /mobtargetteam \<remove>

* Entity Types - The mob types that will ignore the player, e.g. `"ZOMBIE SKELETON"`

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

While set, any listed mob that tries to target that player loses its target instead. Other players are still fair game, so the mobs stay dangerous to everyone else.

`remove` clears the list for that player.

{% hint style="warning" %}
The list is held in memory and cleared on restart.
{% endhint %}

### Examples

Make undead ignore a player, for a disguise item:

```
/mobtargetteam set "ZOMBIE SKELETON HUSK DROWNED WITHER_SKELETON"
```

Make a player invisible to every mob type they might meet in the nether:

```
/mobtargetteam set "BLAZE GHAST PIGLIN HOGLIN MAGMA_CUBE"
```

Clear it:

```
/mobtargetteam remove
```
