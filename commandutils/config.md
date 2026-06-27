---
icon: sliders-up
description: Configuring CommandUtils through config.yml
---

# Config

CommandUtils generates `plugins/CommandUtils/config.yml` on first run. The file is split into four sections: **Whitelists**, **Placeholders**, **Commands**, and a couple of global settings at the bottom.

{% hint style="info" %}
After editing `config.yml`, restart the server (or use a plugin manager) to apply the changes. Some settings are noted as requiring a restart.
{% endhint %}

## Whitelists

Whitelists (also used as blacklists) define reusable, named sets of blocks that block-break and replace commands can reference by name. Each entry in a list is one of:

* A **material** — `BARREL`, `OAK_LOG`
* A **tag** — prefixed with `#`, e.g. `#mineable/axe`, `#leaves`
* An **exclusion** — prefixed with `!`, e.g. `!BARREL`
* A **tag exclusion** — prefixed with `!#`, e.g. `!#all_signs`

```yaml
Whitelists:
  axe:
    - "#mineable/axe"
    - "#leaves"
    - "!BARREL"
    - "!CHEST"
    - "!#all_signs"
  pickaxe:
    - "#mineable/pickaxe"
    - "!SPAWNER"
    - "!#shulker_boxes"
  shovel:
    - "#mineable/shovel"
```

A defined whitelist name (e.g. `axe`) can then be passed wherever a command takes a `Whitelisted Blocks` argument.

## Placeholders

Enables/disables the PlaceholderAPI expansions and tunes their behaviour. The master `Enabled` toggle turns all expansions on or off.

```yaml
Placeholders:
  Enabled: true
  StringUtils:
    Enabled: true
    ArgumentSeparator: ","
    AllowCustomSeparator: true
    If:
      ElseIfKeyword: "elseif"
      ElseKeyword: "else"
      ConditionSeparator: "\""
    StaticString:
      Prefix: "&aPrefix"
  BlockUtils:
    Enabled: true
  PlayerUtils:
    Enabled: true
```

See [Placeholders](placeholders/README.md) for what each expansion provides.

## Commands

Every command has its own block under `Commands`. The common keys are:

* **Enabled** — whether the command is registered. Set to `false` to disable it.
* **Aliases** — a list of extra names the command responds to, e.g. `["ci", "icd"]`.
* **Permission** — overrides the default permission node for the command.

```yaml
Commands:
  BreakInRadius:
    Enabled: true
    Aliases: []
    Permission: "commandutils.command.breakinradiuscommand"
```

Many commands add their own extra keys. A few examples:

* **BreakInVein** — `DefaultCheckClaim`, `DefaultMaxBlocks`, `DefaultTriggerBlockBreakEvent`
* **HighlightBlocks** — `DefaultParticleOffset`, `DefaultParticleSpeed`, `DefaultParticleCount`, `DefaultNumberOfIntervals`, `DefaultParticleSpawnInterval`
* **If / Loop / While / WeightedRandom / ChanceRandomRun** — `CommandSeparator`, `ArgumentSeparator`, `PlaceholderSurrounder`, condition keywords
* **CooldownCommand** — `CooldownMessages` (Hours / Minutes / Seconds / Milliseconds)
* **SendMessage** — `Use&ForColorCodesByDefault`, `ParsePlaceholdersByDefault`, `ColorCodesByDefault`, `PlayersListArg`
* **SetTNTSource** — `MultipleTNTs`, `MultipleSources` (require a restart)
* **BlockCycle** — per-type block mappings (`oxidize`, `KeepStairData`, `KeepSlabData`)

{% hint style="info" %}
Commands that aren't listed in `config.yml` still register with their default settings. Add a block for a command only when you want to change its defaults.
{% endhint %}

## Global settings

```yaml
CommandNamespace: "commandutils"
```

* **CommandNamespace** — the namespace commands are registered under (used for the `namespace:command` form). Recommended to leave as-is unless you know what you're doing.
