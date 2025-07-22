package org.dslofficial.tasks;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.dslofficial.util.PrintHeader;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AutoItemClear extends BukkitRunnable {
    public static Server s;

    public AutoItemClear(Plugin p) {
        s = p.getServer();
    }

    @Override
    public void run() {
        try {
            World w = s.getWorld("새로운 세계");
            if (w == null) {
                s.broadcastMessage("error");
                return;
            }

            List<Entity> elist = w.getEntities();
            int count = 0;
            for (Entity e : elist) {
                if (e instanceof Item) {
                    e.remove();
                    count++;
                }
            }

            s.broadcastMessage(PrintHeader.header("info", "아이템 " + ChatColor.GREEN + count + "" + ChatColor.WHITE + "개를 성공적으로 제거했습니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
