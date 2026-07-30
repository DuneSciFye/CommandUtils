---
description: Restocks villager trades without waiting for the workday
---

# Refresh Villager Trades

Usage: /refreshvillagertrades \<Villagers>

* Villagers - The villagers to restock

Every trade's use count is reset to zero, so sold-out offers are available again. The trades themselves are unchanged — same items, same prices, same experience.

Villagers with no profession or nitwits are skipped, as are villagers with no job site.

### Examples

Restock the nearest villager:

```
/refreshvillagertrades @e[type=villager,limit=1,sort=nearest]
```

Restock a whole shop, on a timer:

```
/loop add restock 999999 0t 5m refreshvillagertrades @e[type=villager,x=100,y=64,z=-30,distance=..20]
```
