---
description: Turns villagers into zombie villagers
---

# Zombify Villager

Usage: /zombifyvillager \<Entities>

* Entities - The villagers to zombify

The villager is replaced with a zombie villager that keeps its profession and trades, so curing it later restores the same shop. Entities that aren't villagers are ignored.

Reverse it with [Cure Villager](cure-villager.md).

### Examples

Zombify the nearest villager:

```
/zombifyvillager @e[type=villager,limit=1,sort=nearest]
```

Zombify a whole village during a raid event:

```
/zombifyvillager @e[type=villager,distance=..30]
```
