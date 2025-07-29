package org.dslofficial.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import org.dslofficial.object.UIItem;
import org.dslofficial.object.UIItemMap;
import org.dslofficial.util.GetPlayer;
import org.dslofficial.util.PermissionSyntaxing;
import org.dslofficial.util.Reload;

import java.text.SimpleDateFormat;

import java.util.Objects;
import java.util.List;

public class MainMenu {
    public static final String uiName = ChatColor.BLUE + "" + ChatColor.BOLD + "메인메뉴";
    public static final UIItemMap itemMap = new UIItemMap();
    public static void openUI(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.8f);

        // 장식용 유리판
//        for (int i = 0; i < 45; i++) {
//            ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
//            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
//            meta.setDisplayName(" ");
//            item.setItemMeta(meta);
//            itemMap.put(new UIItem(item, i), null);
//            if (i == 9) i = 16;
//            else if (i == 18) i = 25;
//            else if (i == 27) i = 34;
//        }

        // itemMap 등록

        // player skull
        ItemStack mySkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = Objects.requireNonNull((SkullMeta) mySkull.getItemMeta());
        meta.setOwnerProfile(player.getPlayerProfile());
        meta.setDisplayName(ChatColor.AQUA + player.getName());
        meta.setLore(List.of(ChatColor.WHITE + "" + ChatColor.BOLD + "역할 : " + PermissionSyntaxing.get(GetPlayer.run(player.getName()).get("role").toString()), ChatColor.WHITE + "" + ChatColor.BOLD + "마지막 로그인 : " + ChatColor.RESET + ChatColor.GOLD + new SimpleDateFormat("yyyy.MM.dd. hh:mm:ss").format(player.getLastPlayed())));

        mySkull.setItemMeta(meta);
        itemMap.put(new UIItem(mySkull, 22), null); // EventHandle을 할 필요가 없으므로 registeredName을 null로 등록

        // uptime
        Reload.reload(player);
        ItemStack arrow = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta itemMeta = Objects.requireNonNull(arrow.getItemMeta());
        itemMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "서버 업타임 : " + ChatColor.GREEN + Reload.uptime_str);
        arrow.setItemMeta(itemMeta);
        itemMap.put(new UIItem(arrow, 24), null);

        // money
        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        itemMeta = Objects.requireNonNull(gold.getItemMeta());
        itemMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "소지금 : " + ChatColor.YELLOW + GetPlayer.run(player.getName()).get("money").toString() + " DS");
        gold.setItemMeta(itemMeta);
        itemMap.put(new UIItem(gold, 25), null);

        // gohome
        ItemStack compass = new ItemStack(Material.COMPASS);
        itemMeta = Objects.requireNonNull(compass.getItemMeta());
        itemMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "집으로 이동");
        itemMeta.setLore(List.of(ChatColor.GRAY + "/sethome" + ChatColor.WHITE + " 으로 설정한 집으로 " + ChatColor.RED + ChatColor.BOLD + "텔레포트" + ChatColor.RESET + ChatColor.WHITE + "합니다."));
        compass.setItemMeta(itemMeta);
        itemMap.put(new UIItem(compass, 19), "gotohome");

        // wood axe
        ItemStack wood_axe = new ItemStack(Material.WOODEN_AXE);
        itemMeta = Objects.requireNonNull(wood_axe.getItemMeta());
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "건축관련 도우미");
        itemMeta.setLore(List.of(ChatColor.GOLD + "도시서버 건축" + ChatColor.RESET + ChatColor.WHITE + "에 관하여 유용한 기능을 제공합니다."));
        wood_axe.setItemMeta(itemMeta);
        itemMap.put(new UIItem(wood_axe, 20), "building_help");

        // red concrete
        ItemStack concrete = new ItemStack(Material.RED_CONCRETE);
        itemMeta = Objects.requireNonNull(concrete.getItemMeta());
        itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "서버 나가기");
        itemMeta.setLore(List.of(ChatColor.WHITE + "클릭하면 서버와의 연결이 즉시 " + ChatColor.RED + ChatColor.BOLD + "종료" + ChatColor.RESET + ChatColor.WHITE + "됩니다."));
        concrete.setItemMeta(itemMeta);
        itemMap.put(new UIItem(concrete, 40), "server_quit");

        // inventory에 등록
        Inventory gui = Bukkit.createInventory(null, 45, uiName);
        itemMap.forEach((k, v) -> gui.setItem(k.slot(), k.item()));
        player.openInventory(gui);
    }
}
