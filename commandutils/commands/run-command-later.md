---
description: Runs commands after a delay
---

# Run Command Later

Usage: /runcommandlater \<add> \<Command ID> \<Time> \<Commands> \[\<Player>] \[\<Command Separator>] \[\<Placeholder Surrounder>]

Usage: /runcommandlater \<run> \<Time> \<Commands> \[\<Player>]

Usage: /runcommandlater \<remove> \<Command ID>

* Command ID - Name for this task. Scheduling a new task with an existing ID replaces it
* Time - How long to wait, e.g. `5s`, `1m`, `100t`
* Commands - Commands to run, separated by `,,`
* Player _(optional)_ - Placeholders are parsed against this player when the delay is up
* Command Separator _(optional)_ - Replaces `,,`
* Placeholder Surrounder _(optional)_ - Character used in place of `%`. Defaults to `$`

Placeholders are resolved when the commands finally run, not when they are scheduled, so values are current.

`run` fires and forgets. `add` gives the task an ID so it can be cancelled with `remove` — useful for "cancel if the player moves" style effects.

{% hint style="warning" %}
Pending tasks are not saved and are lost on restart.
{% endhint %}

### Examples

Remove a temporary permission after 30 seconds:

```
/runcommandlater run 30s lp user Steve permission unset essentials.fly
```

A delayed teleport that can be interrupted:

```
/runcommandlater add warmup-Steve 5s tp Steve 100 64 -30 Steve
/runcommandlater remove warmup-Steve
```

Several commands after one delay:

```
/runcommandlater add revive-Steve 10s effect give Steve regeneration 5 2,,sendmessage Steve &aYou feel better Steve
```
