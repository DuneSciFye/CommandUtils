---
description: Runs commands only if a cooldown has expired
---

# Cooldown Command

Usage: /cooldowncommand \<Player> \<ID> \<Time> \<Commands> \[\<Command Separator>]

Usage: /cooldowncommand \<run | silent | global> \<Player> \<ID> \<Time> \<Commands> \[\<Command Separator>]

Usage: /cooldowncommand \<run2 | silent2> \<Player> \<ID> \<Time> \<Commands>

Usage: /cooldowncommand \<reset | clear> \<Player> \[\<ID>]

Usage: /cooldowncommand \<getcooldown | getcd> \<Player> \<ID>

* Player - The player the cooldown belongs to
* ID - Name of the cooldown. Different IDs are tracked separately
* Time - How long the cooldown lasts, e.g. `5s`, `1m`, `100t`
* Commands - Commands to run when off cooldown, separated by `,,`
* Command Separator _(optional)_ - Replaces `,,`

| Form | Behaviour |
| --- | --- |
| _(no keyword)_ / `run` | Runs the commands, or shows the remaining time in the action bar |
| `silent` | Runs the commands, or does nothing. No message |
| `global` | One shared cooldown for the whole server instead of per player |
| `run2` / `silent2` | Same as `run` / `silent`, but the commands are a greedy argument, so quoting is not needed |
| `reset` / `clear` | Clears one cooldown, or every cooldown for that player if no ID is given |
| `getcooldown` / `getcd` | Reports the time left |

The cooldown starts the moment the commands run. Cooldowns are held in memory and cleared on restart.

{% hint style="info" %}
The action bar messages are configurable per unit under `Commands.CooldownCommand.CooldownMessages` in [config.yml](../config.md), using `%hours%`, `%minutes%`, `%seconds%` and `%milliseconds%`.
{% endhint %}

### Examples

An ability item that can be used once every 30 seconds:

```
/cooldowncommand run Steve dash 30s "effect give Steve speed 3 5,,playsound minecraft:entity.player.attack_sweep master Steve"
```

The same, but silently ignored while on cooldown:

```
/cooldowncommand silent Steve dash 30s "effect give Steve speed 3 5"
```

A world boss anyone can trigger, but only once an hour:

```
/cooldowncommand global Steve worldboss 1h "summon minecraft:wither 100 64 -30"
```

Check and clear:

```
/cooldowncommand getcd Steve dash
/cooldowncommand reset Steve dash
```
