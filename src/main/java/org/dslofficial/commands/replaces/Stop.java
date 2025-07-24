package org.dslofficial.commands.replaces;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.dslofficial.DSLPlugin;
import org.dslofficial.util.PrintHeader;
import org.dslofficial.util.Reload;

import org.jetbrains.annotations.NotNull;

// import org.bukkit.entity.Player;

public class Stop implements CommandExecutor {
    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Server s = sender.getServer();
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "You cannot use this command as a player!");
            return true;
        }

        if (!DSLPlugin.sharedData.getBoolean("isrunning_Stop")) {
            DSLPlugin.sharedData.setBoolean("isrunning_Stop", true);

            Bukkit.getScheduler().runTaskAsynchronously(DSLPlugin.getPlugin(DSLPlugin.class), () -> {
                try {
                    for (int i = 3; i >= 1; i--) {
                        s.broadcastMessage(PrintHeader.header("info", "서버가 " + ChatColor.RED + ChatColor.BOLD + "%d초".formatted(i) + ChatColor.WHITE + " 뒤에 꺼집니다."));
                        Thread.sleep(1000);
                        if (!DSLPlugin.sharedData.getBoolean("isrunning_Stop")) return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Player p : s.getOnlinePlayers()) {
                    p.kickPlayer(PrintHeader.header("exitmsg/stop", "* 수고하셨습니다 *\n모든 수정사항이 저장되었으며 서버가 " + ChatColor.RED + "종료" + ChatColor.YELLOW + "되었습니다.\n" + ChatColor.GREEN + "서버 가동 기간 : " + Reload.uptime_str));
                }

                System.out.println("* 수고하셨습니다 *\n모든 수정사항이 저장되었으며 서버가 " + ChatColor.RED + "종료" + ChatColor.YELLOW + "되었습니다.\n" + ChatColor.GREEN + "서버 가동 기간 : " + Reload.uptime_str);
                s.shutdown();
                DSLPlugin.sharedData.setBoolean("isrunning_Stop", false);
            });
        } else {
            DSLPlugin.sharedData.setBoolean("isrunning_Stop", false);
            sender.getServer().broadcastMessage(PrintHeader.header("info", "콘솔에서 서버 종료를 취소하였습니다."));
        }


//        // Event ( 서버시즌 종료때 사용 )
//        for (Player p : s.getOnlinePlayers()) {
//            p.kickPlayer(PrintHeader.header("exitmsg/stop", "DSL SERVER 시즌 1 : 2분기가 종료되었습니다.\n플레이해주셔서 감사합니다!"));
//        }
//
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return true;
    }
}
