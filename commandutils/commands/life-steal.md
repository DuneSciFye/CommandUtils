---
description: Damages entities and heals the attacker for what lands
---

# Life Steal

Usage: /lifesteal \<Target> \<Amount>

* Target - The entities to damage
* Amount - Damage to deal, in half hearts

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block. That player is credited with the damage and healed.

Healing uses the damage that actually landed, after armour, resistance and absorption — hitting an armoured target heals less than hitting a naked one.

{% hint style="warning" %}
Requires Minecraft 1.21.1 or newer. On older versions the command is not registered.
{% endhint %}

### Examples

Steal 4 half-hearts from the nearest mob:

```
/execute as Steve run lifesteal @e[type=!player,limit=1,sort=nearest] 4
```

A weapon that drains everything nearby:

```
/lifesteal @e[type=!player,distance=..4] 2
```
