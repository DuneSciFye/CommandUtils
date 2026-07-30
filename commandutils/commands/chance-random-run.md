---
description: Rolls a chance for each group of commands and runs the ones that hit
---

# Chance Random Run

Usage: /chancerandomrun \<Chance>,,\<Commands>\[,,\<Chance>,,\<Commands>...]

* Chance - A `1` in _N_ chance. `1` always runs, `100` runs one time in a hundred
* Commands - Commands to run if the roll succeeds, separated by `|`

Chances and command groups alternate, separated by `,,`. Every pair is rolled independently, so any number of them can succeed in the same run. Commands run from the console, without a leading `/`.

### Examples

A 1 in 10 chance to give a diamond:

```
/chancerandomrun 10,,give %player_name% diamond 1
```

Two independent rolls, one of which runs several commands:

```
/chancerandomrun 5,,give %player_name% diamond 1,,100,,give %player_name% netherite_ingot 1|broadcastmessage &6%player_name% struck it rich!
```
