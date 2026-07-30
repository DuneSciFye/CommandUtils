---
description: Prints what a placeholder resolves to
---

# Parse Placeholder

Usage: /parseplaceholder \<Player> \<Placeholder>

Usage: /parseplaceholder \<me> \<Placeholder>

* Player - The player the placeholder is parsed against
* me - Parses against yourself. Players only
* Placeholder - The placeholder text, which may contain several placeholders and plain text

The result is sent back to whoever ran the command. This is the quickest way to check a placeholder before wiring it into an item or command.

{% hint style="warning" %}
Requires **PlaceholderAPI**. Without it the command is not registered.
{% endhint %}

### Examples

```
/parseplaceholder me %player_health%
```

```
/parseplaceholder Steve %player_name% is in %player_world%
```

Test a CommandUtils placeholder:

```
/parseplaceholder me %stringutils_randomint_1,100%
```
