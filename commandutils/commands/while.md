---
description: Repeats commands on a timer for as long as a condition holds
---

# While

Usage: /while \<add> \<Command ID> \<Player> \<Compare 1> \<Compare Method> \<Compare 2> \<Initial Delay> \<Period> \<Commands>

Usage: /while \<remove> \<Command ID>

Usage: /while \<has> \<Command ID>

Usage: /while \<list>

* Command ID - Name for this loop. Starting a new loop with an existing ID replaces it
* Player - The player placeholders are parsed against. The loop stops if they log off
* Compare 1 - Left side of the comparison, re-evaluated every run
* Compare Method - One of `==`, `!=`, `contains`, `!contains`, `>`, `<`, `>=`, `<=`
* Compare 2 - Right side of the comparison
* Initial Delay - How long to wait before the first run, e.g. `20t`, `1s`
* Period - How long between runs
* Commands - Commands to run each time, separated by `,,`

Write placeholders with `$` instead of `%` in `Compare 1`, `Compare 2` and `Commands` — they are swapped back before being parsed, which keeps the outer plugin from resolving them once and freezing the value.

The loop cancels itself as soon as the comparison fails, the player logs off, or a numeric comparison gets a value that isn't a number.

{% hint style="warning" %}
Loops are not saved. Everything stops on server restart, and `list` only shows loops started since then.
{% endhint %}

### Examples

Drain a player's food while they stand in fire, twice a second:

```
/while add burning Steve $player_is_burning$ == true 0t 10t food remove Steve 1
```

Warn a player every 5 seconds while their health is low:

```
/while add lowhp Steve $player_health$ <= 6 0t 5s sendmessage Steve &cLow health!,,playsound minecraft:block.note_block.pling master Steve
```

Stop and inspect loops:

```
/while has lowhp
/while remove lowhp
/while list
```
