---
description: Blocks inventory shuffles for a while, and reacts when one is blocked
---

# Prevent Mix Inventory

Usage: /preventmixinventory \<Duration> \[\<Command Separator> \<Commands>]

* Duration - How long the protection lasts, e.g. `5s`, `1m`, `100t`
* Command Separator _(optional)_ - String that separates the commands
* Commands _(optional)_ - Commands run each time a shuffle is blocked

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

While active, [Mix Inventory](mix-inventory.md) does nothing to this player. The commands are the hook for telling them why — a message, a sound, a particle.

Running it again replaces the previous timer rather than stacking.

### Examples

Ten seconds of protection:

```
/preventmixinventory 10s
```

Protection that announces itself when it blocks a shuffle:

```
/preventmixinventory 30s ,, sendmessage %player_name% &aYour ward blocked the scramble!,,playsound minecraft:block.beacon.deactivate master %player_name%
```
