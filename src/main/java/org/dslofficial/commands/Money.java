package org.dslofficial.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dslofficial.DSLPlugin;
import org.dslofficial.util.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Money implements CommandExecutor {
    @Override public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] arg) {
        String url = DSLPlugin.url;
        if (!(sender instanceof Player)) {
            sender.sendMessage(PrintHeader.header("error", "이 명령어는 플레이어만 사용 가능합니다.\n(콘솔에서 플레이어의 돈을 관리하려면 'mm' 명령어를 사용하시기 바랍니다.)"));
            return true;
        }

        if (arg.length == 0) {
            sender.sendMessage(PrintHeader.header("error", "매개변수의 갯수가 잘못되었습니다. 받음: 0, 필요 : 3"));
            return true;
        }

        switch (arg[0]) {
            case "send" -> {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.submit(() -> {
                    Server server = sender.getServer();

                    // Error handle
                    if (!CompareType.isInt(arg[1])) {
                        sender.sendMessage(PrintHeader.header("error", "보낼 금액을 정확히 입력해 주십시오."));
                        return true;
                    }

                    if (Integer.parseInt(arg[1]) <= 0) {
                        sender.sendMessage(PrintHeader.header("error", "보낼 금액을 정확히 입력해 주십시오."));
                        return true;
                    }

                    if (TestArguments.test(3, arg)) {
                        sender.sendMessage(PrintHeader.header("error", "매개변수의 갯수가 잘못되었습니다. 받음: " + arg.length + ", 필요 : 3"));
                        return true;
                    }

                    if (server.getPlayer(arg[2]) == null) {
                        sender.sendMessage(PrintHeader.header("error", "플레이어를 찾을 수 없습니다."));
                        return true;
                    }

                    if (sender.getName().equals(arg[2])) {
                        sender.sendMessage(PrintHeader.header("error", "자신에겐 송금할 수 없습니다."));
                        return true;
                    }

                    if (GetPlayer.run(sender.getName()).get("id") == null) ((Player) sender).kickPlayer(PrintHeader.header("exitmsg/kick", "유저 정보를 찾을 수 없습니다. 관리자에게 문의해 주세요."));
                    if (GetPlayer.run(arg[2]).get("id") == null) Objects.requireNonNull(server.getPlayer(arg[2])).kickPlayer(PrintHeader.header("exitmsg/kick", "유저 정보를 찾을 수 없습니다. 관리자에게 문의해 주세요."));

                    // Send Money
                    JSONObject senderobj = GetPlayer.run(sender.getName());
                    JSONObject targetobj = GetPlayer.run(arg[2]);

                    if (Integer.parseInt(senderobj.get("money").toString()) < Integer.parseInt(arg[1])) {
                        sender.sendMessage(PrintHeader.header("error", "송금할 돈이 부족합니다."));
                        return true;
                    }

                    String target_sender = Integer.toString(Integer.parseInt(senderobj.get("money").toString()) - Integer.parseInt(arg[1]));
                    String target_receiver = Integer.toString(Integer.parseInt(targetobj.get("money").toString()) + Integer.parseInt(arg[1]));

                    // sender target
                    SetPlayer.edit(sender.getName(), "money", target_sender);

                    // recevier target
                    SetPlayer.edit(arg[2], "money", target_receiver);

                    Reload.reload((Player) sender);
                    Reload.reload(server.getPlayer(arg[2]));

                    // Notice
                    sender.sendMessage(PrintHeader.header("info", ChatColor.AQUA + arg[2] + ChatColor.WHITE + " 님에게 " + ChatColor.YELLOW + arg[1] + "DS" + ChatColor.WHITE + " 만큼을 송금했습니다."));
                    Objects.requireNonNull(server.getPlayer(arg[2])).sendMessage(PrintHeader.header("info", ChatColor.AQUA + sender.getName() + ChatColor.WHITE + " 님이 " + ChatColor.YELLOW + arg[1] + "DS" + ChatColor.WHITE + " 만큼을 당신에게 송금하셨습니다."));
                    return true;
                });
                return true;
            }

            case "get" -> {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.submit(() -> {
                    if (!CompareType.isInt(arg[1])) {
                        sender.sendMessage(PrintHeader.header("error", "올바른 금액을 입력해 주세요."));
                        return true;
                    }

                    if (Integer.parseInt(arg[1]) <= 0) {
                        sender.sendMessage(PrintHeader.header("error", "올바른 금액을 입력해 주세요."));
                        return true;
                    }

                    JSONParser parser = new JSONParser();
                    JSONObject senderobj = GetPlayer.run(sender.getName());

                    if (Integer.parseInt(senderobj.get("money").toString()) < Integer.parseInt(arg[1])) {
                        sender.sendMessage(PrintHeader.header("error", "돈이 부족합니다."));
                        return true;
                    }

                    String itemname = ChatColor.GRAY + "" + ChatColor.BOLD + "[ " + ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "수 표" + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + " ] " + ChatColor.RESET + ChatColor.YELLOW + arg[1] + " DS";
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.WHITE + "현금과 같은 역할을 하는 " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "수표" + ChatColor.RESET + ChatColor.WHITE + "입니다.");
                    lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "이 수표는 " + ChatColor.YELLOW + arg[1] + " DS" + ChatColor.RESET + ChatColor.WHITE + " 만큼의 가치가 있으며, " + ChatColor.GREEN + ChatColor.BOLD + "마우스 우클릭" + ChatColor.RESET + ChatColor.WHITE + "을 하시면 소지금에 " + ChatColor.RESET + ChatColor.YELLOW + arg[1] + " DS" + ChatColor.RESET + ChatColor.WHITE+ " 만큼을 추가합니다.");
                    lore.add(ChatColor.RESET + arg[1]);

                    ItemStack paper = new ItemStack(Material.PAPER);
                    ItemMeta papermeta = paper.getItemMeta();

                    papermeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
                    papermeta.setDisplayName(itemname);
                    papermeta.setLore(lore);
                    paper.setItemMeta(papermeta);

                    if (((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR) {
                        sender.sendMessage(PrintHeader.header("error", "수표를 받으려면 손이 비어있어야 합니다."));
                        return true;
                    }

                    // Set sender's money to target money & reload
                    SetPlayer.edit(sender.getName(), "money", Integer.toString(Integer.parseInt(senderobj.get("money").toString()) - Integer.parseInt(arg[1])));
                    Reload.reload((Player) sender);

                    // 플레이어에게 수표주기
                    ((Player) sender).getInventory().setItemInMainHand(paper);
                    sender.sendMessage(PrintHeader.header("info", "수표 " + ChatColor.YELLOW + arg[1] + "DS " + ChatColor.RESET + ChatColor.WHITE + "만큼을 출금하였습니다."));
                    return true;
                });
                return true;
            }

            default -> {
                sender.sendMessage(PrintHeader.header("error", "잘못된 옵션입니다."));
                return true;
            }
        }
    }
}
