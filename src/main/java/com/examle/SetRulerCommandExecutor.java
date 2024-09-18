package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRulerCommandExecutor implements CommandExecutor {
    private RulerManager rulerManager;

    public SetRulerCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, является ли отправитель команды игроком
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Проверяем наличие аргументов (имя игрока для назначения правителем)
            if (args.length == 0) {
                player.sendMessage(ChatColor.GOLD + "Вы должны указать имя игрока для назначения правителем.");
                return false;
            }

            // Получаем имя игрока из аргументов
            String targetPlayerName = args[0];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

            // Проверяем, существует ли игрок с таким именем
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Игрок с именем " + targetPlayerName + " не найден.");
                return false;
            }

            // Назначаем указанного игрока правителем
            rulerManager.setRuler(targetPlayer);
            player.sendMessage(ChatColor.GREEN + "Игрок " + targetPlayer.getName() + " был назначен правителем.");
            targetPlayer.sendMessage(ChatColor.AQUA + "Вы были назначены правителем!");

            return true;
        } else {
            sender.sendMessage("Эту команду может выполнять только игрок.");
            return false;
        }
    }
}
