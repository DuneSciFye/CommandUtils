---
description: Reserved — not yet implemented
---

# Send Condition Message

The `/sendconditionmessage` command is registered but has no usable form yet.

{% hint style="warning" %}
Nothing happens when you run it. For conditional messages today, combine [If](if.md) or [Precise If](precise-if.md) with [Send Message](send-message.md).
{% endhint %}

### Example

```
/preciseif Steve ,, % "%player_health% <= 6" sendmessage Steve &cYou are badly hurt else sendmessage Steve &aYou are fine
```
