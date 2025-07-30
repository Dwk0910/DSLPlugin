package org.dslofficial.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import org.dslofficial.DSLPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

@SuppressWarnings("CallToPrintStackTrace")
public class Reload {
    public static String uptime_str = "";
    public static void reload(Player p) {
        Server s = DSLPlugin.server;

        // Scoreboard reload
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getNewScoreboard();

        // PREPARE FOR SCOREBOARD
        // Get Uptime
        File f = new File(System.getProperty("user.dir") + File.separator + "start_time.dat");
        if (f.exists()) {
            try {
                FileReader reader = new FileReader(f);
                Scanner reader_scanner = new Scanner(reader);
                long uptime = (System.currentTimeMillis() - reader_scanner.nextLong()) / 1000;
                long h = 0, m = uptime / 60;

                while (m / 60 >= 1) {
                    m -= 60;
                    h += 1;
                }

                uptime_str = h + "시간 " + m + "분";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("업타임 불러오기 실패");
            uptime_str = "업타임 불러오기 실패";
        }

        // Get Online
        String online_s = s.getOnlinePlayers().size() + " / " + s.getMaxPlayers();

        // Create Scoreboard
        Objective obj_sc = scoreboard.registerNewObjective("main", "main", "main");
        obj_sc.setDisplayName(ChatColor.GRAY + " [ " + ChatColor.GREEN + "DSL SERVER" + ChatColor.GRAY + " ] ");
        obj_sc.setDisplaySlot(DisplaySlot.SIDEBAR);

        String playerName = p.getName();

        Score name = obj_sc.getScore("" + ChatColor.WHITE + ChatColor.BOLD + "이ㅤ름 : " + ChatColor.RESET + ChatColor.AQUA + playerName);
        Score permission = obj_sc.getScore("" + ChatColor.WHITE + ChatColor.BOLD + "역ㅤ할 : " + ChatColor.RESET + PermissionSyntaxing.get(GetPlayer.run(playerName).get("role").toString()));
        Score money = obj_sc.getScore("" + ChatColor.WHITE + ChatColor.BOLD + "소지금 : " + ChatColor.RESET + ChatColor.YELLOW + GetPlayer.run(p.getName()).get("money") + " DS");
        Score online = obj_sc.getScore("" + ChatColor.WHITE + ChatColor.BOLD + "온라인 : " + ChatColor.RESET + ChatColor.GREEN + online_s);
        Score uptimescore = obj_sc.getScore("" + ChatColor.WHITE + ChatColor.BOLD + "업타임 : " + ChatColor.RESET + ChatColor.DARK_GREEN + uptime_str);

        Bukkit.getScheduler().runTask(DSLPlugin.getPlugin(DSLPlugin.class), () -> {
            name.setScore(10);
            permission.setScore(9);
            money.setScore(8);
            online.setScore(7);
            uptimescore.setScore(6);

            p.setScoreboard(scoreboard);
        });
    }
}