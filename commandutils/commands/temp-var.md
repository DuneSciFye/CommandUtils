---
description: Stores server-wide variables in memory
---

# Temp Var

Usage: /tempvar \<set | add | setifempty | get | clear | remove> \<Variable Name> \[\<Content>]

* Variable Name - Name of the variable. For `clear` and `remove`, several names can be given separated by commas
* Content _(optional)_ - The value to store

| Function | Effect |
| --- | --- |
| `set` | Stores the value, replacing anything already there |
| `add` | Adds numerically. Both the stored value and the new one must be numbers, otherwise nothing happens |
| `setifempty` | Stores the value only if the variable doesn't exist yet |
| `get` | Sends the value to whoever ran the command |
| `clear` / `remove` | Deletes the variable |

Read variables back with the `%stringutils_variable_<name>%` [placeholder](../placeholders/). Unset variables read as an empty string; `add` treats a missing variable as `0`.

For values that belong to one player, use [Temp Player Var](temp-player-var.md).

{% hint style="warning" %}
Variables live in memory only and are gone after a restart.
{% endhint %}

### Examples

Track how many mobs an event has spawned:

```
/tempvar setifempty event_kills 0
/tempvar add event_kills 1
```

Store the current event name and read it back:

```
/tempvar set current_event dragon_raid
/tempvar get current_event
```

Clear several variables at once:

```
/tempvar clear event_kills,current_event
```
