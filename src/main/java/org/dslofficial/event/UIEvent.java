package org.dslofficial.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import org.dslofficial.ui.MainMenu;

public class UIEvent implements Listener {
    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent e) {
        // Shift + F 구현
        if (e.getPlayer().isSneaking()) {
            e.setCancelled(true);
            MainMenu.openUI(e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(MainMenu.uiName)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                // 어차피 이 이벤트가 일어나는 시점에는 itemMap이 무조건 null이 아니므로 걱정 ㄴㄴ
                String registeredName = MainMenu.itemMap.getRegisteredName(clickedItem);
                if (registeredName != null) {
                    switch (registeredName) {

                    }
                    p.closeInventory();
                } // null일경우 편의상 인벤토리는 닫지 않기로. (잘못 클릭한것일수도 있기 때문)
            }
        }
    }
}
