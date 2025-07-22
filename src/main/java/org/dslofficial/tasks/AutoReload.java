package org.dslofficial.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.dslofficial.DSLPlugin;
import org.dslofficial.util.Reload;

public class AutoReload extends BukkitRunnable {
    @Override public void run() {
        for (Player p : DSLPlugin.server.getOnlinePlayers()) {
            Reload.reload(p);
        }
    }
}
