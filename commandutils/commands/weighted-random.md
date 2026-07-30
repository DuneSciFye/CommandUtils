---
description: Picks one entry from a weighted list and runs its commands
---

# Weighted Random

Usage: /weightedrandom \<run> \<Arguments>

Usage: /weightedrandom \<cache> \<ID> \<Command Separator> \<Placeholder Surrounder> \<Arguments>

Usage: /weightedrandom \<removecache> \<ID>

* ID - Name the parsed list is cached under
* Command Separator - String that separates commands inside one entry. Defaults to `|`
* Placeholder Surrounder - Character used in place of `%`. Defaults to `$`
* Arguments - The weighted list

Entries are written as `<weight::commands>`, one after another:

```
<10::command one><5::command two><1::command three>
```

A weight of `10` against a total of `16` means that entry is picked 10 times out of 16. Exactly one entry runs per call.

`cache` parses the list once and reuses it under the given ID — worth using for long tables that fire often. Changing the list means clearing the cache with `removecache` first.

### Examples

A loot table where common drops are 20× more likely than the rare one:

```
/weightedrandom run <20::give Steve coal 1><5::give Steve iron_ingot 1><1::give Steve diamond 1>
```

Several commands in one entry:

```
/weightedrandom run <9::sendmessage Steve &7Nothing happens><1::give Steve diamond 1|broadcastmessage &6Steve found a diamond!>
```

The same table, cached under an ID:

```
/weightedrandom cache mine-drops | $ <20::give $player_name$ coal 1><5::give $player_name$ iron_ingot 1><1::give $player_name$ diamond 1>
/weightedrandom removecache mine-drops
```
