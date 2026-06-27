---
description: Explanation on NameSpacedKeys
---

# NameSpacedKeys

<figure><img src="../../.gitbook/assets/image.png" alt=""><figcaption><p>An item's complete NBT data obtained from Paper's <code>/paper dumpitem</code> command</p></figcaption></figure>

From the image above, the NBT data that this command alters is located in the `custom_data` section, highlighted in pink. It's all of this:&#x20;

```
custom_data={PublicBukkitValues: {"executableitems:ei-id": "test", "score:usage": 1}}
```

There are two [NameSpacedKeys](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/NamespacedKey.html#%3Cinit%3E\(java.lang.String,java.lang.String\)) in here, one being `"executableitems:ei-id": "test"` and the other being `"score:usage":` \
\
Focusing on the first one, `executableitems` is the Namespace, `ei-id` is the Key, and `test` is the Content of the NameSpacedKey.

Similarly, for the second one, `score` is the Namespace, `usage` is the Key, and `1` is the Content.\
<br>
