---
description: Multiplies the loot players get from the mobs they kill
---

# Mob Drop Multiplier

Usage: /mobdropmultiplier \<set> \<Players> \<Multiplier> \[\<Blacklist>]

Usage: /mobdropmultiplier \<clear> \<Players>

* Players - The players the multiplier applies to
* Multiplier - How much loot they get. Minimum `1`
* Blacklist _(optional)_ - Entity types the multiplier skips, e.g. `"WITHER ENDER_DRAGON"`

The multiplier applies to whatever the mob would normally drop, and only when that player lands the kill.

Fractional multipliers work by chance: `1.5` always adds nothing extra for half of the kills and one extra copy for the other half, averaging out over time.

{% hint style="warning" %}
Multipliers are held in memory and are cleared on restart. Re-apply them on join for a persistent perk.
{% endhint %}

### Examples

Double loot for one player:

```
/mobdropmultiplier set Steve 2
```

A server-wide 1.5× loot event, excluding bosses:

```
/mobdropmultiplier set @a 1.5 "WITHER ENDER_DRAGON WARDEN"
```

End the event:

```
/mobdropmultiplier clear @a
```
