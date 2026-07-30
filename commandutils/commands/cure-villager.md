---
description: Starts curing zombie villagers
---

# Cure Villager

Usage: /curevillager \<Villagers> \[\<Conversion Time>] \[\<Player>]

* Villagers - The zombie villagers to cure
* Conversion Time _(optional)_ - Ticks until they finish converting. `0` cures them instantly. Defaults to `0`
* Player _(optional)_ - Who gets the discount for curing them

No golden apple or weakness potion is needed. Entities that aren't zombie villagers are ignored.

Naming a player gives the standard permanent trade discount towards them, exactly as a normal cure would.

### Examples

Cure a zombie villager instantly:

```
/curevillager @e[type=zombie_villager,limit=1,sort=nearest]
```

Cure it over 10 seconds, with the shaking animation, and credit the player:

```
/curevillager @e[type=zombie_villager,limit=1,sort=nearest] 200 Steve
```
