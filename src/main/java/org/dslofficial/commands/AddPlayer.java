package org.dslofficial.commands;

import org.dslofficial.util.PrintHeader;
import org.dslofficial.util.SetPlayer;
import org.dslofficial.util.TestArguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class AddPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        if (cs instanceof ConsoleCommandSender) {
            if (TestArguments.test(2, args)) {
                cs.sendMessage(PrintHeader.header("error", "매개변수의 갯수가 잘못되었습니다. 받음: " + args.length + ", 필요 : 2"));
                return true;
            }
            SetPlayer.add(args[0], args[1]);
            cs.sendMessage(PrintHeader.header("info", "플레이어 "+ args[0] + "을 성공적으로 생성시켰습니다."));
        } else {
            cs.sendMessage(PrintHeader.header("error", "이 명령어는 콘솔에서만 사용 가능합니다."));
        }
        return true;
    }
}
