package org.dslofficial.util;

import org.bukkit.ChatColor;

public class PrintHeader {
    public static String header(String type, String detail) {
        String result = "", exitmsg_detail = "";

        if (type.contains("/")) {
            exitmsg_detail = type.split("/")[1];
            type = type.split("/")[0];
        }

        switch (type) {
            case "info" -> result = ChatColor.GRAY + "[" + ChatColor.GREEN + "DSL SERVER" + ChatColor.GOLD + "/" + ChatColor.AQUA + "INFO" + ChatColor.GRAY + "] " + ChatColor.WHITE + detail;
            case "error" -> result = ChatColor.GRAY + "[" + ChatColor.GREEN + "DSL SERVER" + ChatColor.GOLD + "/" + ChatColor.RED + "ERROR" + ChatColor.GRAY + "] " + ChatColor.WHITE + detail;
            case "warning" -> result = ChatColor.GRAY + "[" + ChatColor.GREEN + "DSL SERVER" + ChatColor.GOLD + "/" + ChatColor.YELLOW + "WARNING" + ChatColor.GRAY + "] " + ChatColor.WHITE + detail;

            case "exitmsg" -> {
                 String defaulttitle = ChatColor.GRAY + "" + ChatColor.BOLD + "\n[ " +  ChatColor.GREEN + ChatColor.BOLD + "DSL OFFICIAL SERVER" + ChatColor.GRAY + ChatColor.BOLD + " ]\n" + ChatColor.RESET;
                 result += defaulttitle;
                 switch (exitmsg_detail) {
                     case "kick" -> result += ChatColor.WHITE + "" + ChatColor.BOLD + "서버에서 " + ChatColor.RED + ChatColor.BOLD + "추방 " + ChatColor.WHITE + ChatColor.BOLD + "당하셨습니다:\n\n" + ChatColor.RESET + ChatColor.YELLOW + detail + "\n";
                     case "ban" -> result += ChatColor.WHITE + "" + ChatColor.BOLD + "서버에서 " + ChatColor.RED + ChatColor.BOLD + "차단 " + ChatColor.WHITE + ChatColor.BOLD + "당하셨습니다:\n\n" + ChatColor.RESET + ChatColor.YELLOW + detail + "\n";
                     case "stop" -> result += ChatColor.WHITE + "" + ChatColor.BOLD + "서버가 " + ChatColor.BLUE + ChatColor.BOLD + "닫혔습니다.\n\n" + ChatColor.RESET + ChatColor.YELLOW + detail + "\n";
                     default -> result += "알 수 없는 detail : " + detail;
                 }
            }

            default -> result = "알 수 없는 type : " + type;
        }
        return result;
    }
}
