package me.dunescifye.commandutils.commands;

@CommandInfo(enabled = false)
public class EffectCommand extends Command {

    @Override
    public void register() {
        createCommand()
            .register(this.getNamespace());
    }

}
