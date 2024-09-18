package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPlayerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public AddPlayerCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player ruler = (Player) sender;

            // Проверяем, был ли передан аргумент с именем игрока
            if (args.length == 0) {
                ruler.sendMessage(ChatColor.RED + "Вы должны указать имя игрока.");
                return false;
            }

            // Проверяем, является ли отправитель правителем
            if (!rulerManager.isRuler(ruler)) {
                ruler.sendMessage(ChatColor.RED + "Вы не являетесь правителем.");
                return false;
            }

            // Получаем игрока по имени из аргументов команды
            Player targetPlayer = ruler.getServer().getPlayer(args[0]);

            // Проверяем, найден ли игрок
            if (targetPlayer == null) {
                ruler.sendMessage(ChatColor.RED + "Игрок с таким именем не найден.");
                return false;
            }

            // Добавляем игрока в команду и выводим сообщение о результате
            boolean added = rulerManager.addPlayerToTeam(ruler, targetPlayer);

            if (added) {
                ruler.sendMessage(ChatColor.GREEN + "Игрок " + targetPlayer.getName() + " был добавлен в вашу армию.");
            } else {
                ruler.sendMessage(ChatColor.GOLD + "Игрок " + targetPlayer.getName() + " уже в вашей армии.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнить только игрок.");
        }
        return true;
    }
}
