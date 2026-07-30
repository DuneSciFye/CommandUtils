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

Every command has its own block under `Commands`, named after the command in PascalCase.

```yaml
Commands:
  BreakInRadius:
    Enabled: true
    Aliases: ["bir", "hammer"]
```

| Key | Effect |
| --- | --- |
| `Enabled` | Whether the command is registered at all. `false` removes it |
| `Aliases` | Extra names the command answers to |
| `Namespace` | Registers this one command under a different namespace |

{% hint style="info" %}
Commands with no block in `config.yml` still register with their defaults, and a block is written for them on the next start. Add one only when you want to change something.
{% endhint %}

{% hint style="warning" %}
The `Permission` key present in the generated file is not read — every command currently requires operator level. Use `Enabled` and your own command-blocking setup to restrict access.
{% endhint %}

### Per-command settings

Some commands add their own keys, which set the defaults used when the matching argument is left out:

| Command | Keys |
| --- | --- |
| [Break In Vein](commands/break-in-vein.md) | `DefaultCheckClaim`, `DefaultMaxBlocks`, `DefaultTriggerBlockBreakEvent` |
| [Highlight Blocks](commands/highlight-blocks.md) | `DefaultParticleOffset`, `DefaultParticleSpeed`, `DefaultParticleCount`, `DefaultNumberOfIntervals`, `DefaultParticleSpawnInterval` |
| [If](commands/if.md) | `ElseIfKeyword`, `ElseKeyword`, `CommandSeparator`, `ConditionSeparator` |
| [Loop](commands/loop.md) | `CommandSeparator` |
| [Chance Random Run](commands/chance-random-run.md) | `ArgumentSeparator`, `CommandSeparator` |
| [Cooldown Command](commands/cooldown-command.md) | `CooldownMessages` — `Hours`, `Minutes`, `Seconds`, `Milliseconds` |
| [Send Message](commands/send-message.md) | `Use&ForColorCodesByDefault`, `ParsePlaceholdersByDefault`, `ColorCodesByDefault`, `PlayersListArg` |
| [Mob Target](commands/mob-target.md) | `AllowMultipleTargets` |
| [Block Cycle](commands/block-cycle.md) | `oxidize` — `KeepStairData`, `KeepSlabData`, and the `blocks` mapping |

## Global settings

```yaml
CommandNamespace: "commandutils"
```

* **CommandNamespace** — the namespace every command is registered under, so `/commandutils:breakinradius` always works even if another plugin claims the plain name. Leave it alone unless you have a reason to change it.
