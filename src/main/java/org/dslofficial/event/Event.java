package org.dslofficial.event;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import org.dslofficial.DSLPlugin;
import org.dslofficial.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class Event implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPlayedBefore())
            e.setJoinMessage(PrintHeader.header("info", ChatColor.BLUE + "" + ChatColor.BOLD + e.getPlayer().getName() + ChatColor.YELLOW + " 님이 서버에 처음 들어오셨습니다!!" + ChatColor.AQUA + ChatColor.BOLD + " 환영합니다!"));
        e.setJoinMessage(PrintHeader.header("info", ChatColor.AQUA + e.getPlayer().getName() + ChatColor.YELLOW + " 님이 서버에 참여하셨습니다"));

        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(DSLPlugin.dataFolder + File.separator + "playerlist.dat");
            JSONObject players = (JSONObject) parser.parse(reader);

            Object playerData = players.get(p.getName());
            if (playerData == null)
                p.kickPlayer(PrintHeader.header("exitmsg/kick", "서버에 아직 가입되지 않으셨습니다. 가입을 먼저 한 후, 다시 시도해 주세요.\n(아이디가 바뀌였다면 관리자에게 문의 바랍니다)"));
        } catch (IOException | ParseException ex) {
            DSLPlugin.server.broadcastMessage(PrintHeader.header("error", "playerlist.dat파일이 손상되었습니다!!"));
        }
    }

    // 플레이어가 메시지를 읽을 수 있는 시점.
    @EventHandler
    public void onResourceLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();
        Server s = p.getServer();

        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            Date date = new Date(p.getLastPlayed());
            Format format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            p.sendMessage("\n\n\n\n");
            p.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.WHITE + ChatColor.BOLD + "님, 반갑습니다!");
            p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "마지막 로그인 : " + ChatColor.GOLD + format.format(date));
            p.sendMessage("\n\n\n\n");
        } else {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) return;
            p.kickPlayer(PrintHeader.header("exitmsg/kick", "서버에 들어오시려면 리소스팩을 다운로드 하여야 합니다. (STATUS: " + e.getStatus() + ")\n(다운로드 오류가 발생한 경우 관리자에게 문의해 주세요)"));
            return;
        }
        Reload.reload(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(PrintHeader.header("info", ChatColor.AQUA + e.getPlayer().getName() + ChatColor.YELLOW + " 님이 서버에서 나가셨습니다"));
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        String target, senderName, senderPermission;
        target = e.getMessage();
        senderName = e.getPlayer().getName();
        senderPermission = GetPlayer.run(e.getPlayer().getName()).get("role").toString();

        /*
        permission color

        총관리자 -> darkred, bold
        부관리자 -> red, bold
        매니저 -> yellow, nobold
        멤버 -> green, nobold
         */

        String perm = "";
        switch (senderPermission) {
            case "leader" -> perm = ChatColor.DARK_RED + "" + ChatColor.BOLD + "총관리자";
            case "v.leader" -> perm = ChatColor.RED + "" + ChatColor.BOLD + "부관리자";
            case "manager" -> perm = ChatColor.YELLOW + "매니저";
            case "member" -> perm = ChatColor.GREEN + "멤버";
        }

        e.setCancelled(true);
        DSLPlugin.server.broadcastMessage(ChatColor.AQUA + senderName + ChatColor.GRAY + " [" + perm + ChatColor.GRAY + "]" + ChatColor.GOLD + " : " + ChatColor.WHITE + target);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        e.setDeathMessage(PrintHeader.header("info", "플레이어 " + ChatColor.AQUA + p.getName() + ChatColor.WHITE + " 이(가) 사망하셨습니다."));
    }

    // 월드 수정 (배치, 제거) 컨트롤.
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!HasPermission.check(p.getName(), "canBlockPlace-Break")) {
            e.setCancelled(true);
            p.sendMessage(PrintHeader.header("error", "당신은 블럭을 부술 권한이 없습니다."));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!HasPermission.check(p.getName(), "canBlockPlace-Break")) {
            e.setCancelled(true);
            p.sendMessage(PrintHeader.header("error", "당신은 블럭을 설치할 권한이 없습니다."));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getServer().getPlayer(e.getDamager().getName()) == null) {
            e.setCancelled(true);
            return;
        }

        String itmname = ChatColor.YELLOW + "" + ChatColor.BOLD + "아이템 액자 리무버";
        Player p = Objects.requireNonNull(e.getDamager().getServer().getPlayer(e.getDamager().getName()));

        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (e.getEntityType() == EntityType.ITEM_FRAME & !p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(itmname)) {
                e.setCancelled(true);
            }
        } else e.setCancelled(true);
    }

    // 자연파괴 방지 (엔티티 한정)
    @EventHandler
    public void onEntityBreak(HangingBreakEvent e) {
        if (!e.getCause().toString().equalsIgnoreCase("ENTITY")) {
            e.setCancelled(true);
        }
    }

    // 폭발억제
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    // 인터렉트 이벤트 (아이템액자)
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        // 회전억제
        if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("ImageMap")) e.setCancelled(true);
        }
    }

    // 인터렉트 이벤트 (수표)
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        // 수표
        // 손에 아이템이 없을 경우 return
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("수 표") & p.getInventory().getItemInMainHand().getType() == Material.PAPER) {
            List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
            if (lore == null) return;
            if (lore.size() != 3) return;
            int m = Integer.parseInt(lore.get(2));

            // Add money
            SetPlayer.edit(e.getPlayer().getName(), "money", Integer.toString(Integer.parseInt(GetPlayer.run(p.getName()).get("money").toString()) + m));
            Reload.reload(p);

            ItemStack air = new ItemStack(Material.AIR);
            p.getInventory().setItemInMainHand(air);
            p.sendMessage(PrintHeader.header("info", "수표를 사용하여 " + ChatColor.YELLOW + m + " DS" + ChatColor.RESET + ChatColor.WHITE + " 만큼을 입급했습니다."));
        }
    }
}