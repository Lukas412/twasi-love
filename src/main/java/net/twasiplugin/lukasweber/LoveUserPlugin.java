package net.twasiplugin.lukasweber;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasiplugin.lukasweber.commands.LoveCommand;

import java.util.Collections;
import java.util.List;

public class LoveUserPlugin extends TwasiUserPlugin {

    private List<TwasiPluginCommand> commands;

    public LoveUserPlugin() {
        this.commands = Collections.singletonList(new LoveCommand(this));
    }

    @Override
    public List<TwasiPluginCommand> getCommands() {
        return commands;
    }
}
