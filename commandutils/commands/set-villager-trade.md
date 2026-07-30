---
description: Changes the stock and pricing of a villager's trades
---

# Set Villager Trade

Usage: /setvillagertrade \<Villagers> \<uses | max_uses | price_multiplier> \<Amount>

* Villagers - The villagers to change
* Amount - The new value, applied to every one of their trades

| Function | Effect |
| --- | --- |
| `uses` | How many times each trade has already been used. `0` restocks them |
| `max_uses` | How many times each trade can be used before running out |
| `price_multiplier` | How strongly demand and reputation change the price. `0` freezes prices |

Villagers with no profession or nitwits are skipped.

### Examples

Give every trade 999 uses, for a shop that never runs out:

```
/setvillagertrade @e[type=villager,limit=1,sort=nearest] max_uses 999
```

Lock prices so reputation and demand stop affecting them:

```
/setvillagertrade @e[type=villager,distance=..20] price_multiplier 0
```

Restock, the same as [Refresh Villager Trades](refresh-villager-trades.md):

```
/setvillagertrade @e[type=villager,limit=1,sort=nearest] uses 0
```
