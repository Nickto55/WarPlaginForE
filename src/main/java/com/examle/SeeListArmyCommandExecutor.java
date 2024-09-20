package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public class SeeListArmyCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public SeeListArmyCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, является ли отправитель игроком
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок.");
            return false;
        }

        Player player = (Player) sender;

        // Проверяем, является ли игрок правителем
        if (!rulerManager.isRuler(player)) {
            player.sendMessage(ChatColor.RED + "Вы не являетесь правителем.");
            return false;
        }

        // Получаем список армии правителя
        List<Player> army = rulerManager.getTeam(player);

        // Если армия пуста
        if (army == null || army.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "Ваша армия пуста.");
            return true;
        }

        // Отправляем список армии игроку
        player.sendMessage(ChatColor.GREEN + "Ваши подданные (армия):");
        for (Player soldier : army) {
            player.sendMessage(ChatColor.GOLD + "- " + soldier.getName());
        }

        return true;
    }
}
