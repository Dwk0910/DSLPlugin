package org.dslofficial.commands.completes;

import org.dslofficial.commands.Permission;
import org.dslofficial.util.GetPlayer;

import org.bukkit.entity.Player;
import org.dslofficial.util.SetPlayer;
import org.jetbrains.annotations.NotNull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.ArrayList;

public class PermissionComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String str, String[] args) {
        switch (args.length) {
            case 1 -> {
                ArrayList<String> result = new ArrayList<>();
                for (Player p : cs.getServer().getOnlinePlayers()) result.add(p.getName());

                return result;
            }

            case 2 -> {
                List<String> result = new ArrayList<>();

                result.add("add");
                result.add("remove");
                result.add("list");

                return result;
            }

            case 3 -> {
                ArrayList<String> result = new ArrayList<>();
                switch (args[1].toLowerCase()) {
                    case "add" -> {
                        result.addAll(Permission.permissionList);
                        return result;
                    }

                    case "remove" -> {
                        // 기존 유저용
                        if (GetPlayer.run(cs.getName()).get("permission") == null) SetPlayer.edit(cs.getName(), "permission", "[]");
                        try {
                            JSONParser parser = new JSONParser();
                            JSONArray permissionlist = (JSONArray) parser.parse(GetPlayer.run(cs.getName()).get("permission").toString());
                            for (Object o : permissionlist) {
                                String perm = (String) o;
                                result.add(perm);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    default -> {
                        result.add("");
                        return result;
                    }
                }
            }
        }
        List<String> result = new ArrayList<>();
        result.add("");
        return result;
    }
}
