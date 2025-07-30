package org.dslofficial.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.dslofficial.ui.MainMenu;
import org.dslofficial.util.PrintHeader;

import org.jetbrains.annotations.NotNull;

public class Menu implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            MainMenu.openUI(p);
        } else sender.sendMessage(PrintHeader.header("error", "이 명령어는 플레이어만 실행할 수 있습니다."));

        return true;
    }
}
