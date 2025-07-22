package org.dslofficial.commands.replaces;

import org.bukkit.ChatColor;
import org.dslofficial.util.PrintHeader;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Kill implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                p.setHealth(0);
                return true;
            }

            Server s = sender.getServer();
            Player targetPlayer = s.getPlayer(args[0]);

            if (targetPlayer == null) {
                sender.sendMessage(PrintHeader.header("error", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.WHITE + "을(를) 찾을 수 없습니다"));
                return true;
            }

            targetPlayer.setHealth(0);
        } else {
            if (args.length == 0) {
                sender.sendMessage(PrintHeader.header("error", "플레이어를 지정해 주십시오."));
                return true;
            }

            Server s = sender.getServer();
            Player targetPlayer = s.getPlayer(args[0]);

            if (targetPlayer == null) {
                sender.sendMessage(PrintHeader.header("error", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.WHITE + "을(를) 찾을 수 없습니다"));
                return true;
            }

            targetPlayer.setHealth(0);
        }
        return true;
    }
}
