package org.dslofficial.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dslofficial.util.GetPlayer;
import org.dslofficial.util.PrintHeader;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.*;

public class GiveItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }

        JSONObject data = GetPlayer.run(cs.getName());

        if (data.get("role").toString().toLowerCase().contains("leader") | data.get("role").toString().toLowerCase().contains("v.leader") | data.get("role").toString().toLowerCase().contains("manager")) {
            ItemStack itmstk = new ItemStack(Material.ARROW);
            ItemMeta im = itmstk.getItemMeta();
            im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "아이템 액자 리무버");
            itmstk.setItemMeta(im);
            if (args.length == 1) {
                for (Player p : cs.getServer().getOnlinePlayers()) {
                    if (p.getDisplayName().equals(args[0])) {
                        p.getInventory().addItem(itmstk);

                        cs.sendMessage(PrintHeader.header("info", ChatColor.AQUA + p.getName() + ChatColor.WHITE + " 에게 " + itmstk.getItemMeta().getDisplayName() + ChatColor.WHITE + " 을(를) 지급했습니다"));
                        p.sendMessage(PrintHeader.header("info", ChatColor.AQUA + cs.getName() + ChatColor.WHITE + " 이(가) 당신에게 " + itmstk.getItemMeta().getDisplayName() + ChatColor.WHITE + " 을(를) 지급했습니다"));
                        return true;
                    }
                }

                cs.sendMessage(PrintHeader.header("error", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.WHITE + "을(를) 찾을 수 없습니다."));
                return true;
            } else {
                Player p = cs.getServer().getPlayer(cs.getName());
                Objects.requireNonNull(p).getInventory().addItem(itmstk);

                cs.sendMessage(PrintHeader.header("info", itmstk.getItemMeta().getDisplayName() + ChatColor.WHITE + " 을(를) 주었습니다."));
                return true;
            }
        }

        return true;
    }
}
