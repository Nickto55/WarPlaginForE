package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.List;

public class SeeMyComandsCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public SeeMyComandsCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверка на то, что команду выполняет игрок
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок.");
            return false;
        }

        Player player = (Player) sender;

        // Проверка, является ли игрок правителем
        if (!rulerManager.isRuler(player)) {
            player.sendMessage(ChatColor.RED + "Вы не являетесь правителем.");
            return false;
        }

        // Получаем все команды правителя
        Map<Player, List<Player>> allRulers = rulerManager.getAllRulers();
        if (allRulers == null || allRulers.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "Вы не управляете никакими командами.");
            return true;
        }

        // Проверяем, есть ли команда для данного правителя
        if (!allRulers.containsKey(player)) {
            player.sendMessage(ChatColor.YELLOW + "У вас нет команд.");
            return true;
        }

        // Выводим список всех команд
        player.sendMessage(ChatColor.GREEN + "Вы управляете следующими командами:");
        List<Player> playerTeam = allRulers.get(player);
        for (Player teamMember : playerTeam) {
            player.sendMessage(ChatColor.GOLD + "- " + teamMember.getName());
        }

        return true;
    }
}
