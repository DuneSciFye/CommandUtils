---
description: Waits for a condition to become true, then runs commands once
---

# Run Command When

Usage: /runcommandwhen \<add> \<Command ID> \<Condition> \<Initial Delay> \<Period> \<Placeholder Surrounder> \<Command Separator> \<Commands>

Usage: /runcommandwhen \<add> \<Command ID> \<Player> \<Compare 1> \<Compare Method> \<Compare 2> \<Initial Delay> \<Interval> \<Commands>

Usage: /runcommandwhen \<remove> \<Command ID>

* Command ID - Name for this watcher. Adding a new one with an existing ID replaces it
* Condition - A comparison, using the same operators as [If](if.md). Several can be joined with `&&`
* Initial Delay - How long to wait before the first check, e.g. `1s`
* Period / Interval - How long between checks. The second form takes plain ticks
* Placeholder Surrounder - Character used in place of `%` in the condition and commands
* Command Separator - String that separates the commands
* Player - The player placeholders are parsed against. The watcher stops if they log off
* Compare 1 / Compare 2 - The two values to compare, re-read on every check
* Compare Method - One of `==`, `!=`, `contains`, `!contains`
* Commands - Commands to run once the condition passes, split by `|` in the second form

The opposite of [While](while.md): the condition is checked on a timer and the commands run **once**, the first time it passes. The watcher then stops on its own.

The first form runs as the player who executed it, so call it through `/execute as <player>`. The second form takes the player as an argument instead.

{% hint style="warning" %}
A watcher whose condition never becomes true keeps checking until the player logs off or the server restarts. Give it an ID you can `remove`.
{% endhint %}

### Examples

Reward a player the moment they reach level 30:

```
/runcommandwhen add lvl30-Steve Steve $player_level$ >= 30 1s 20 sendmessage Steve &aLevel 30!|give Steve diamond 1
```

Wait until a player is back on the ground before dealing fall damage:

```
/runcommandwhen add land-Steve "$player_is_on_ground$ == true" 1s 5t $ ,, damage Steve 4
```

Stop watching:

```
/runcommandwhen remove lvl30-Steve
```
