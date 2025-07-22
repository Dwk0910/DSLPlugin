package org.dslofficial.commands.completes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MoneyComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> arg0 = new ArrayList<>();
        arg0.add("send");
        arg0.add("get");

        ArrayList<String> result = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                for (String i : arg0) if (i.toLowerCase().contains(args[0].toLowerCase())) result.add(i);
                return result;
            }

            case 2 -> {
                result.add("[<금액>]");
                return result;
            }

            case 3 -> {
                if (args[0].equals("send")) {
                    for (Player p : sender.getServer().getOnlinePlayers()) {
                        if (p.getName().toLowerCase().contains(args[2].toLowerCase())) result.add(p.getName());
                    }
                }
                return result;
            }

            default -> {
                return result;
            }
        }
    }
}
