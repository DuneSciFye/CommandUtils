---
description: Stores per-player variables in memory
---

# Temp Player Var

Usage: /tempplayervar \<set | add | append | setifempty | get | clear | remove> \<Player> \<Variable Name> \[\<Content>]

* Player - Name of the player the variable belongs to
* Variable Name - Name of the variable. For `clear` and `remove`, several names can be given separated by commas
* Content _(optional)_ - The value to store

| Function | Effect |
| --- | --- |
| `set` | Stores the value, replacing anything already there |
| `add` | Adds numerically. Both the stored value and the new one must be numbers, otherwise nothing happens |
| `append` | Adds the text onto the end of the current value |
| `setifempty` | Stores the value only if the variable doesn't exist yet |
| `get` | Sends the value to whoever ran the command |
| `clear` / `remove` | Deletes the variable |

Read variables back with the `%stringutils_playervariable_<name>%` [placeholder](../placeholders/), which resolves against the player the placeholder is parsed for. Unset variables read as an empty string; `add` treats a missing variable as `0`.

For server-wide values, use [Temp Var](temp-var.md).

{% hint style="warning" %}
Variables are keyed by player name, live in memory only, and are gone after a restart.
{% endhint %}

### Examples

Count a player's combo hits:

```
/tempplayervar add Steve combo 1
/tempplayervar clear Steve combo
```

Remember where a player was before a teleport:

```
/tempplayervar set Steve return_loc 100 64 -30
/tempplayervar get Steve return_loc
```

Build up a list of collected pieces:

```
/tempplayervar append Steve keys ,gold_key
```
