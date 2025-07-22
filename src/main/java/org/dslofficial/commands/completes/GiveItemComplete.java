package org.dslofficial.commands.completes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;

public class GiveItemComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();

            for (Player p : cs.getServer().getOnlinePlayers()) result.add(p.getName());
            return result;
        } else {
            ArrayList<String> result = new ArrayList<>();
            result.add("");
            return result;
        }
    }
}
