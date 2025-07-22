package org.dslofficial.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.dslofficial.util.*;
import org.jetbrains.annotations.NotNull;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.ArrayList;

public class Permission implements CommandExecutor {
    public static List<String> permissionList = new ArrayList<>();
    @SuppressWarnings("unchecked")
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String str, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(PrintHeader.header("error", "Console에서는 사용이 불가능한 명령어입니다. 개발예정!"));
            return true;
        }

        permissionList.add("canBlockPlace-Break");
        // 추가예정

        String senderRole = GetPlayer.run(cs.getName()).get("role").toString();
        if (senderRole.equals("leader") | senderRole.equals("v.leader")) {
            /*
            args[0] = player's name
            args[1] = "add", "delete", "list"
            args[2] = permission name
             */


            // Error handling
            /*
                Add -> 3개 필요
                Remove -> 3개 필요
                List -> 2개 필요
             */
            if (TestArguments.test(2, args)) {
                cs.sendMessage(PrintHeader.header("error", "매개변수가 잘못되었습니다. (받음 : " + args.length + ", 필요 : 2 또는 3"));
                return false;
            }

            if (GetPlayer.run(args[0]).get("id") == null) {
                cs.sendMessage(PrintHeader.header("error", "플레이어를 찾을 수 없습니다."));
                return true;
            }

            // 기존 플레이어용
            if (GetPlayer.run(args[0]).get("permission") == null) SetPlayer.edit(args[0], "permission", "[]");

            switch (args[1]) {
                case "add" -> {
                    // Error handling
                    if (TestArguments.test(3, args)) {
                        cs.sendMessage(PrintHeader.header("error", "매개변수가 잘못되었습니다. (받음 : " + args.length + ", 필요 : 3"));
                        return false;
                    }

                    if (HasPermission.check(args[0], args[2])) {
                        cs.sendMessage(PrintHeader.header("error", "이 플레이어는 권한 " + ChatColor.YELLOW + args[2] + ChatColor.RESET + ChatColor.WHITE + "을(를) 이미 가지고 있습니다."));
                        return true;
                    }

                    for (String perm : permissionList) {
                        if (args[2].equals(perm)) {
                            JSONArray newlist = new JSONArray();
                            for (String permissionthathad : permissionList) {
                                if (HasPermission.check(args[0], permissionthathad)) newlist.add(permissionthathad);
                            }

                            newlist.add(perm);

                            SetPlayer.edit(args[0], "permission", newlist.toJSONString());
                            cs.sendMessage(PrintHeader.header("info", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.RESET + ChatColor.WHITE + "에게 권한 "+ ChatColor.YELLOW + args[2] + ChatColor.RESET + ChatColor.WHITE + "을(를) 추가했습니다."));
                            return true;
                        }
                    }

                    cs.sendMessage(PrintHeader.header("info", "권한 " + ChatColor.YELLOW + args[2] + ChatColor.RESET + ChatColor.WHITE + "은(는) 없는 권한입니다."));
                    return true;
                }

                case "remove" -> {
                    // Error handling
                    if (TestArguments.test(3, args)) {
                        cs.sendMessage(PrintHeader.header("error", "매개변수가 잘못되었습니다. (받음 : " + args.length + ", 필요 : 3"));
                        return false;
                    }

                    try {
                        JSONParser parser = new JSONParser();
                        JSONArray playerpermissions = (JSONArray) parser.parse(GetPlayer.run(args[0]).get("permission").toString());
                        boolean changed = false;
                        for (Object o : playerpermissions) {
                            String perm = (String) o;
                            if (perm.equals(args[2])) {
                                // args[2] 빼고 나머지 다 넣기
                                List<String> newPermissionList = new ArrayList<>();
                                for (String perm2 : permissionList) {
                                    if (!perm2.equals(perm)) newPermissionList.add(perm2);
                                }
                                SetPlayer.edit(args[0], "permission", newPermissionList.toString());
                                cs.sendMessage(PrintHeader.header("info", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.RESET + ChatColor.WHITE + "에게서 권한 " + ChatColor.YELLOW + args[2] + ChatColor.RESET + ChatColor.WHITE + "을(를) 제거하였습니다."));
                                changed = true;
                            }
                        }

                        if (!changed) cs.sendMessage(PrintHeader.header("error", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.RESET + ChatColor.WHITE + "은(는) 이미 권한 " + ChatColor.YELLOW + args[2] + ChatColor.RESET + ChatColor.WHITE + "을(를) 가지고 있지 않거나 없는 권한입니다."));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                case "list" -> {
                    try {
                        JSONParser parser = new JSONParser();
                        JSONArray array = (JSONArray) parser.parse(GetPlayer.run(args[0]).get("permission").toString());
                        StringBuilder sb = new StringBuilder();

                        for (Object o : array) {
                            String perm = (String) o;
                            if (!sb.isEmpty()) sb.append(" ").append(perm);
                            else sb.append(perm);
                        }

                        if (sb.isEmpty()) sb.append(ChatColor.RED).append(ChatColor.BOLD).append("없음");

                        cs.sendMessage(PrintHeader.header("info", "플레이어 " + ChatColor.AQUA + args[0] + ChatColor.RESET + ChatColor.WHITE + "의 권한 : " + ChatColor.YELLOW + sb));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                default -> {
                    cs.sendMessage(PrintHeader.header("error", "잘못된 옵션입니다 : " + args[1]));
                    return false;
                }
            }
        } else {
            cs.sendMessage(PrintHeader.header("error", "이 명령어를 사용할 권한이 없습니다."));
        }
        return true;
    }
}