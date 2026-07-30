---
description: Detailed information on the Whitelisted Blocks Argument
---

# Whitelisted Blocks

Every block command that only affects _some_ blocks takes a **Whitelisted Blocks** argument. It is a filter, not just a list: it can whitelist and blacklist at the same time.

## Formats

Pass either the name of a whitelist defined in [config.yml](../config.md):

```
/breakinradius world 0 100 0 @p 2 pickaxe
```

Or an inline list, space separated and wrapped in quotes:

```
/breakinradius world 0 100 0 @p 2 "#mineable/pickaxe !spawner !#shulker_boxes"
```

## Entries

| Entry | Meaning | Example |
| --- | --- | --- |
| `MATERIAL` | Matches that block | `oak_log` |
| `#tag` | Matches any block in the [block tag](https://minecraft.wiki/w/Tag#Block_tags) | `#mineable/axe` |
| `!MATERIAL` | Never affect that block | `!barrel` |
| `!#tag` | Never affect blocks in that tag | `!#all_signs` |

## How matching works

* A block is affected when it matches **at least one** whitelist entry and **no** blacklist entry.
* If the list contains only blacklist entries, everything except them is affected.
* If the argument is omitted (where optional), every block is affected.

{% hint style="info" %}
Blocks inside a WorldGuard region, or in a GriefPrevention claim or Factions land the player can't build in, are skipped regardless of the whitelist.
{% endhint %}
