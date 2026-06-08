package me.dunescifye.commandutils.commands;

public class EffectCommand extends Command {

    @Override
    public void register() {
        createCommand()
            .register(this.getNamespace());
    }

}
