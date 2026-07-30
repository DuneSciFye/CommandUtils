---
description: Gives villagers a new profession
---

# Change Villager Profession

Usage: /changevillagerprofession \<Villagers> \<Profession>

* Villagers - The villagers to change
* Profession - e.g. `farmer`, `librarian`, `weaponsmith`, `none`, `nitwit`

Villagers with no experience are bumped to 1, which locks the profession in so they don't lose it when they wander away from a job site. Their trades are rerolled for the new profession.

Entities that aren't villagers are ignored.

### Examples

Turn a villager into a librarian:

```
/changevillagerprofession @e[type=villager,limit=1,sort=nearest] librarian
```

Reset a whole group so they can pick new jobs:

```
/changevillagerprofession @e[type=villager,distance=..10] none
```
