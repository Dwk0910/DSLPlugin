package org.dslofficial.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.dslofficial.DSLPlugin;
import org.dslofficial.util.PrintHeader;

import org.jetbrains.annotations.NotNull;

public class ReloadResource implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (DSLPlugin.resourcepackHash == null) sender.sendMessage(PrintHeader.header("error", "지금은 이 커맨드를 사용할 수 없습니다."));
        org.dslofficial.tasks.ReloadResource.run();
        return true;
    }
}
