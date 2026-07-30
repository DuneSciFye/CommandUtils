---
description: Runs a command after stripping surrounding whitespace
---

# Trim Command

Usage: /trimcommand \<Command>

* Command - The command to run, without a leading `/`

The command is trimmed and run from the console. Blank input is ignored instead of erroring.

Useful when a command is built from placeholders that may resolve to nothing or to padded text — `/trimcommand give %player_name% %reward%` will not fail on the leading space that `/give  Steve` would produce.

### Examples

```
/trimcommand   say hello
```

```
/trimcommand give %player_name% %stringutils_variable_reward_item% 1
```
