package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;

public class SendConditionMessageCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {
        new CommandTree("sendconditionmessage")

            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
