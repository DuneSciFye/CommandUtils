---
description: Multiplies the experience players get from the mobs they kill
---

# Xp Drop Multiplier

Usage: /xpdropmultiplier \<set> \<Players> \<Multiplier> \[\<Blacklist>]

Usage: /xpdropmultiplier \<clear> \<Players>

* Players - The players the multiplier applies to
* Multiplier - How much experience they get. Minimum `1`
* Blacklist _(optional)_ - Entity types the multiplier skips, e.g. `"WITHER ENDER_DRAGON"`

Works like [Mob Drop Multiplier](mob-drop-multiplier.md), but on the experience orbs, and only when that player lands the kill.

{% hint style="warning" %}
Multipliers are held in memory and are cleared on restart. Re-apply them on join for a persistent perk.
{% endhint %}

### Examples

Double experience for one player:

```
/xpdropmultiplier set Steve 2
```

A triple-XP weekend for everyone:

```
/xpdropmultiplier set @a 3
```

End it:

```
/xpdropmultiplier clear @a
```
