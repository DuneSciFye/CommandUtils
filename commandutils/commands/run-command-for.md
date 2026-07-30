---
description: Runs commands once per player, with their own placeholders
---

# Run Command For

Usage: /runcommandfor \<Players> \<Command Separator> \<Placeholder Surrounder> \<Commands>

* Players - The players to run for. Accepts a selector such as `@a` or `@a[distance=..10]`
* Command Separator - String that separates the commands, e.g. `,,`
* Placeholder Surrounder - Character used in place of `%` in the commands. Use `%` to write them normally
* Commands - The commands to run for each player

Placeholders are parsed separately for every player, so one command can give each of them a personalised result.

### Examples

Heal everyone within 10 blocks:

```
/runcommandfor @a[distance=..10] ,, % effect give %player_name% instant_health 1 1
```

Announce and reward each player in a world:

```
/runcommandfor @a[x=0,y=64,z=0,distance=..50] ,, $ sendmessage $player_name$ &aEvent reward!,,give $player_name$ diamond 1
```
