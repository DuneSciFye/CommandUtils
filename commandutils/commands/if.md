---
description: Runs commands only when a condition is true
---

# If

Usage: /if "\<Condition>" \<Commands> \[elseif "\<Condition>" \<Commands>...] \[else \<Commands>]

* Condition - One or more comparisons, wrapped in the condition separator
* Commands - Commands to run when the condition is true, separated by `,if,`

Conditions are compared as plain text after placeholders have been substituted, so this command is normally used from a plugin that fills placeholders in first. To have CommandUtils parse the placeholders itself, use [Precise If](precise-if.md).

## Operators

| Operator | True when |
| --- | --- |
| `=` / `==` | Both sides are the same text |
| `!=` | The sides differ |
| `>` `<` `>=` `<=` | Numeric comparison |
| `contains` | The left side contains the right side |
| `!contains` | It does not |

Combine several comparisons in one condition with `&&` or ` and ` — all must pass.

## Keywords

* `elseif` — checked in order, first match wins
* `else` — runs when nothing matched
* `,if,` — separates multiple commands in one branch

{% hint style="info" %}
The condition separator (`"`), the `elseif` / `else` keywords and the `,if,` command separator can all be changed under `Commands.If` in [config.yml](../config.md).
{% endhint %}

### Examples

Reward a player only if they have enough levels:

```
/if "30 >= 10" give Steve diamond 1
```

Two commands in one branch:

```
/if "diamond_pickaxe contains pickaxe" say Nice pickaxe,if,effect give Steve haste 30 1
```

A full chain:

```
/if "5 > 10" say rich elseif "5 > 3" say comfortable else say broke
```

Two conditions at once:

```
/if "10 >= 5 && world = world_nether" say Deep in the nether
```
