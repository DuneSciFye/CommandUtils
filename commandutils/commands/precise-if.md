---
description: If, with placeholders parsed for a chosen player
---

# Precise If

Usage: /preciseif \<Player> \<Command Separator> \<Placeholder Surrounder> \<Arguments>

* Player - The player placeholders are parsed against
* Command Separator - String that separates commands inside a branch, e.g. `,,`
* Placeholder Surrounder - Character used in place of `%` in the arguments, so placeholders survive being passed through another plugin. Use `%` to write them normally
* Arguments - The same `"condition" commands elseif ... else ...` syntax as [If](if.md)

Unlike [If](if.md), placeholders are resolved by this command, right before the condition is checked. That makes it safe to call from an item or block that would otherwise substitute the placeholders too early.

See [If](if.md) for the operators and keywords.

### Examples

Check the player's health at run time:

```
/preciseif Steve ,, % "%player_health% <= 6" effect give Steve regeneration 10 1 else say Still healthy
```

Using `$` instead of `%` so the outer plugin leaves the placeholders alone:

```
/preciseif %player_name% ,, $ "$player_level$ >= 30" say Ready to enchant,,playsound minecraft:entity.player.levelup master $player_name$ else say Not yet
```
