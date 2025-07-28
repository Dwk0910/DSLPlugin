package org.dslofficial.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.dslofficial.DSLPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.dslofficial.tasks.AutoItemClear;
import org.dslofficial.util.PrintHeader;
import org.jetbrains.annotations.NotNull;

public class ClearItem implements CommandExecutor {
    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Server s = sender.getServer();
        if (!DSLPlugin.sharedData.getBoolean("isrunning_ClearItem")) {
            DSLPlugin.sharedData.setBoolean("isrunning_ClearItem", true);
            Bukkit.getScheduler().runTaskAsynchronously(DSLPlugin.getPlugin(DSLPlugin.class), () -> {
               try {
                   for (int i = 3; i >= 1; i--) {
                       if (!DSLPlugin.sharedData.getBoolean("isrunning_ClearItem")) return;
                       s.broadcastMessage(PrintHeader.header("info", "%s에 의해 %s 후 아이템이 제거됩니다.".formatted(ChatColor.AQUA + sender.getName() + ChatColor.RESET + ChatColor.WHITE, ChatColor.RED + "" + ChatColor.BOLD + "%d초".formatted(i) + ChatColor.RESET + ChatColor.WHITE)));
                       Thread.sleep(1000);
                   }
                   if (DSLPlugin.sharedData.getBoolean("isrunning_ClearItem")) new AutoItemClear(DSLPlugin.getPlugin(DSLPlugin.class)).run();
                   else return;
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               DSLPlugin.sharedData.setBoolean("isrunning_ClearItem", false);
            });
        } else {
            DSLPlugin.sharedData.setBoolean("isrunning_ClearItem", false);
            s.broadcastMessage(PrintHeader.header("info", "작업이 %s에 의해 취소되었습니다.".formatted(ChatColor.AQUA + sender.getName() + ChatColor.RESET + ChatColor.WHITE)));
        }
        return true;
    }
}
