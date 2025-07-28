package org.dslofficial.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ServerUI {
    public static void openUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GREEN + "" + ChatColor.BOLD + "DSL SERVER" + ChatColor.GOLD + ChatColor.BOLD + " : " + ChatColor.BLUE + ChatColor.BOLD + "서버 기본 메뉴");
        player.openInventory(gui);
    }
}
