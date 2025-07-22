package org.dslofficial.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.dslofficial.DSLPlugin;
import org.dslofficial.util.GetPlayer;
import org.dslofficial.util.PermissionSyntaxing;
import org.dslofficial.util.PrintHeader;
import org.dslofficial.util.TestArguments;

import org.jetbrains.annotations.NotNull;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Point implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!TestArguments.test(1, args)) {
                if (TestArguments.test(2, args) & !args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(PrintHeader.header("error", "커맨드를 잘못 사용하셨습니다."));
                    sender.sendMessage(PrintHeader.header("error", "사용법 : /point [set/remove/move] [포인트 이름]"));
                    return true;
                }
            } else {
                sender.sendMessage(PrintHeader.header("error", "커맨드를 잘못 사용하셨습니다."));
                sender.sendMessage(PrintHeader.header("error", "사용법 : /point [set/remove/move] [포인트 이름]"));
                return true;
            }

            switch (args[0]) {
                case "set" -> {
                    try {
                        JSONParser parser = new JSONParser();

                        File f = new File(DSLPlugin.GetFolder() + File.separator + "point.dat");
                        FileReader fr = new FileReader(f);
                        JSONObject obj = (JSONObject) parser.parse(fr);

                        HashMap<String, String> map = new HashMap<>();
                        for (Object key : obj.keySet()) map.put(key.toString(), obj.get(key).toString());
                        map.put(args[1], p.getLocation().getX() + ", " + p.getLocation().getY() + ", " + p.getLocation().getZ());

                        JSONObject target = new JSONObject(map);
                        FileWriter writer = new FileWriter(f);
                        writer.write(target.toJSONString());
                        writer.flush();
                        writer.close();

                        sender.sendMessage(PrintHeader.header("info", ChatColor.YELLOW + args[1] + ChatColor.WHITE + "에 '" + ChatColor.BLUE + Math.round(p.getLocation().getX()) + ", " + Math.round(p.getLocation().getY()) + ", " + Math.round(p.getLocation().getZ()) + ChatColor.WHITE + "' 을(를) 성공적으로 기록했습니다."));
                        return true;
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 없습니다."));
                        return true;
                    } catch (ParseException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 손상되었습니다."));
                        e.printStackTrace();
                        return true;
                    } catch (IOException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat를 읽을 수 없습니다."));
                        return true;
                    }
                }

                case "remove" -> {
                    try {
                        File f = new File(DSLPlugin.GetFolder() + File.separator + "point.dat");
                        FileReader reader = new FileReader(f);
                        JSONParser parser = new JSONParser();
                        JSONObject data = (JSONObject) parser.parse(reader);

                        HashMap<String, String> map = new HashMap<>();

                        boolean success = false;
                        for (Object o : data.keySet()) {
                            if (!o.toString().equalsIgnoreCase(args[1])) map.put(o.toString(), data.get(o).toString());
                            else success = true;
                        }

                        JSONObject target = new JSONObject(map);
                        FileWriter writer = new FileWriter(f);
                        writer.write(target.toJSONString());
                        writer.flush();
                        writer.close();

                        if (success) sender.sendMessage(PrintHeader.header("info", "포인트 " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + "을(를) 성공적으로 제거했습니다."));
                        else sender.sendMessage(PrintHeader.header("error", "포인트 " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + "을(를) 찾을 수 없습니다."));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 없습니다."));
                        return true;
                    } catch (ParseException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 손상되었습니다."));
                        return true;
                    } catch (IOException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat를 읽을 수 없습니다."));
                        return true;
                    }
                    return true;
                }
                case "move" -> {
                    try {
                        File f = new File(DSLPlugin.GetFolder() + File.separator + "point.dat");
                        FileReader reader = new FileReader(f);
                        JSONParser parser = new JSONParser();
                        JSONObject data = (JSONObject) parser.parse(reader);

                        if (data.get(args[1]) == null) {
                            p.sendMessage(PrintHeader.header("error", "포인트 " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + "을(를) 찾을 수 없습니다."));
                            return true;
                        }

                        String[] datalist = data.get(args[1]).toString().split(",");
                        Location targetloc = new Location(p.getWorld(), Double.parseDouble(datalist[0]), Double.parseDouble(datalist[1]), Double.parseDouble(datalist[2]));

                        p.teleport(targetloc);
                        p.getServer().broadcastMessage(PrintHeader.header("info", ChatColor.GRAY + "[ " + PermissionSyntaxing.get(GetPlayer.run(p.getName()).get("role").toString()) + ChatColor.GRAY + " ] " + ChatColor.RESET + ChatColor.AQUA + p.getName() + ChatColor.WHITE + "이(가) " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + "(으)로 이동하였습니다."));
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 없습니다."));
                        return true;
                    } catch (ParseException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 손상되었습니다."));
                        return true;
                    } catch (IOException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat를 읽을 수 없습니다."));
                        return true;
                    }
                    return true;
                }
                case "list" -> {
                    try {
                        File f = new File(DSLPlugin.GetFolder() + File.separator + "point.dat");
                        FileReader reader = new FileReader(f);
                        JSONParser parser = new JSONParser();
                        JSONObject data = (JSONObject) parser.parse(reader);

                        boolean success = false;
                        for (Object key : data.keySet()) {
                            String[] origData = data.get(key).toString().split(",");
                            ArrayList<Integer> newData = new ArrayList<>();
                            for (String i : origData) {
                                newData.add((int) Math.round(Double.parseDouble(i)));
                            }

                            String location = newData.get(0) + ", " + newData.get(1) + ", " + newData.get(2);
                            p.sendMessage(ChatColor.YELLOW + "" + key + ChatColor.WHITE + " : " + ChatColor.BLUE + location);
                            success = true;
                        }

                        if (!success) {
                            p.sendMessage(PrintHeader.header("info", "등록된 포인트가 한 개도 없습니다."));
                        }
                    } catch (FileNotFoundException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 없습니다."));
                        return true;
                    } catch (ParseException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat가 손상되었습니다."));
                        return true;
                    } catch (IOException e) {
                        sender.sendMessage(PrintHeader.header("error", "point.dat를 읽을 수 없습니다."));
                        return true;
                    }
                    return true;
                }

                default -> {
                    sender.sendMessage(PrintHeader.header("error", "커맨드를 잘못 사용하셨습니다."));
                    sender.sendMessage(PrintHeader.header("error", "사용법 : /point [set/remove/move] [포인트 이름]"));
                    return true;
                }
            }
        }
        return true;
    }
}
