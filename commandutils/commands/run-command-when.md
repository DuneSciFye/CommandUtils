---
description: Runs commands repeatedly until/when a condition is met
---

# Run Command When

Usage: /runcommandwhen \<add> \<Command ID> \<Condition> \<Initial Delay> \<Period> \<Placeholder Surrounder> \<Command Separator> \<Commands>

Usage: /runcommandwhen \<add> \<Command ID> \<Player> \<Compare 1> \<Compare Method> \<Compare 2> \<Initial Delay> \<Period> \<Commands>

Usage: /runcommandwhen \<remove> \<Command ID>

* Command ID - A unique identifier for the scheduled/looping task
* Condition - The condition to evaluate
* Initial Delay - Delay before the first run
* Period - Ticks between each run
* Placeholder Surrounder - The character used to surround placeholders (e.g. %)
* Command Separator - The string used to separate individual commands
* Commands - Commands to run, separated by the command separator
* Player - The player to affect
* Compare 1 - First value to compare
* Compare Method - Comparison operator (==, !=, >, <, >=, <=, contains, !contains)
* Compare 2 - Second value to compare
