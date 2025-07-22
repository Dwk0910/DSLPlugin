package org.dslofficial.util;

import org.bukkit.ChatColor;

public class PermissionSyntaxing {
    public static String get(String permission) {
        switch (permission) {
            case "leader" -> { return "" + ChatColor.DARK_RED + ChatColor.BOLD + "총관리자"; }
            case "v.leader" -> { return "" + ChatColor.RED + ChatColor.BOLD + "부관리자"; }
            case "manager" -> { return "" + ChatColor.YELLOW + "매니저"; }
            case "member" -> { return "" + ChatColor.GREEN + "멤버"; }
            default -> { return "잘못된 퍼미션: " + permission; }
        }
    }
}