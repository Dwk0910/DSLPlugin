package org.dslofficial.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.dslofficial.util.*;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MoneyManagement implements CommandExecutor {
    @Override public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] arg) {
        if (sender instanceof Player) {
            sender.sendMessage(PrintHeader.header("error", "이 명령어는 Console에서만 사용이 가능합니다."));
            return true;
        }

        if (TestArguments.test(2, arg)) {
            sender.sendMessage(PrintHeader.header("error", "매개변수가 잘못되었습니다. (받음: " + arg.length + ", 필요: 2)"));
            return true;
        }

        if (!CompareType.isInt(arg[1])) {
            sender.sendMessage(PrintHeader.header("error", "올바른 값을 입력하여 주세요."));
            return true;
        }

        Server s = sender.getServer();
        // arg[0] = target player
        // arg[1] = target money

        if (GetPlayer.run(arg[0]).isEmpty()) {
            sender.sendMessage(PrintHeader.header("error", "Player not found"));
            return true;
        }

        // Set target player's money
        SetPlayer.edit(arg[0], "money", arg[1]);

        // Reload
        Reload.reload(s.getPlayer(arg[0]));

        // notice
        sender.sendMessage(PrintHeader.header("info", "플레이어 " + ChatColor.AQUA + arg[0] + ChatColor.WHITE + "의 소지금을 " + ChatColor.YELLOW + arg[1] + "DS " + ChatColor.WHITE + "로 설정했습니다."));
        Objects.requireNonNull(s.getPlayer(arg[0])).sendMessage(PrintHeader.header("info", "Console에서 당신의 소지금을 " + ChatColor.YELLOW + arg[1] + "DS" + ChatColor.WHITE + "로 설정했습니다."));
        return true;
    }
}