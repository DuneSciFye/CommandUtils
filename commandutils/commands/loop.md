---
description: Runs commands a set number of times on a timer
---

# Loop

Usage: /loop \<add> \<Command ID> \<Loop Amount> \<Delay> \<Period> \<Commands> \[\<End Commands>] \[\<Player>]

Usage: /loop \<remove | cancel> \<Command ID>

Usage: /loop \<list>

Usage: /loop \<run> \<Loop Amount> \<Delay> \<Period> \<Commands> \[\<End Commands>] \[\<Player>]

* Command ID - Name for this loop. Starting a new loop with an existing ID replaces it
* Loop Amount - How many times the commands run
* Delay - How long to wait before the first run, e.g. `0t`, `1s`
* Period - How long between runs
* Commands - Commands to run each time, separated by `,,`
* End Commands _(optional)_ - Commands to run once the loop finishes, separated by `,,`
* Player _(optional)_ - Placeholders are parsed against this player each run

`run` starts a loop that can't be cancelled by ID — use it for short, fire-and-forget effects.

Write placeholders with `$` instead of `%` so the outer plugin doesn't resolve them once at the start; they are converted back before each run.

{% hint style="warning" %}
Loops are not saved. Everything stops on server restart, and `list` only shows loops started since then.
{% endhint %}

{% hint style="info" %}
The `,,` separator can be changed under `Commands.Loop.CommandSeparator` in [config.yml](../config.md).
{% endhint %}

### Examples

Strike lightning at a player 5 times, once per second:

```
/loop run 5 0t 1s spawnnodamagelightning world $player_x$ $player_y$ $player_z$ Steve
```

A 10 second channelled effect that announces when it ends:

```
/loop add channel-Steve 10 0t 1s sendactionbar Steve &bChannelling... particle enchant $player_x$ $player_y$ $player_z$ 1 1 1 0 20,,sendmessage Steve &aChannel complete Steve
```

Stop it early, or see what's running:

```
/loop cancel channel-Steve
/loop list
```
