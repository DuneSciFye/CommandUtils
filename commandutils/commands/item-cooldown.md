---
description: Puts a use cooldown on an item or a material
---

# Item Cooldown

Usage: /itemcooldown \<setcooldown> \<Player> \<[Slot](../arguments/slot-argument.md)> \<Duration>

Usage: /itemcooldown \<setmaterialcooldown> \<Player> \<Material> \<Duration>

Usage: /itemcooldown \<setcooldowngroup> \<Player> \<[Slot](../arguments/slot-argument.md)> \<Key> \[\<Duration>]

* Player - The player to affect
* Slot - Slot of the item
* Material - Applies to every item of this type the player has
* Duration - How long the cooldown lasts, e.g. `5s`, `1m`, `100t`
* Key - A namespaced key such as `myplugin:abilities`, grouping items that share one cooldown

| Function | Scope |
| --- | --- |
| `setcooldown` | Just that item |
| `setmaterialcooldown` | Every item of that material |
| `setcooldowngroup` | Writes a cooldown group onto the item, so all items in the group share one timer |

The player sees the usual white sweep across the item icon and cannot use it until the timer runs out.

{% hint style="warning" %}
Requires Minecraft 1.21.1 or newer. On older versions the command is not registered.
{% endhint %}

### Examples

Put the held item on a 10 second cooldown:

```
/itemcooldown setcooldown Steve mainhand 10s
```

Put every ender pearl the player has on cooldown:

```
/itemcooldown setmaterialcooldown Steve ender_pearl 30s
```

Make several ability items share one 1 minute cooldown:

```
/itemcooldown setcooldowngroup Steve mainhand myplugin:abilities 1m
```
