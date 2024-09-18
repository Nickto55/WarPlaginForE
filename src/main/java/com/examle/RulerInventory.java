package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RulerInventory implements Listener {

    private final JavaPlugin plugin;

    public RulerInventory(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void openRulerInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Ruler's Inventory");

        ItemStack addPlayerButton = new ItemStack(Material.GREEN_WOOL);
        ItemStack removePlayerButton = new ItemStack(Material.RED_WOOL);
        ItemStack chooseStrategyButton = new ItemStack(Material.BLUE_WOOL);

        inv.setItem(0, addPlayerButton);
        inv.setItem(1, removePlayerButton);
        inv.setItem(2, chooseStrategyButton);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Ruler's Inventory")) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.GREEN_WOOL) {
                    event.getWhoClicked().sendMessage("Clicked Add Player");
                    // Логика для добавления игрока
                } else if (event.getCurrentItem().getType() == Material.RED_WOOL) {
                    event.getWhoClicked().sendMessage("Clicked Remove Player");
                    // Логика для удаления игрока
                } else if (event.getCurrentItem().getType() == Material.BLUE_WOOL) {
                    event.getWhoClicked().sendMessage("Clicked Choose Strategy");
                    // Логика для выбора стратегии
                }
            }
        }
    }
}
