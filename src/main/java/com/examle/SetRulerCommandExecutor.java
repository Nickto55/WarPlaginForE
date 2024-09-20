package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetRulerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;
    private final PermissionManager PermissionManager;


    // Конструктор для инициализации RulerManager
    public SetRulerCommandExecutor(RulerManager rulerManager, PermissionManager permissionManager) {
        this.rulerManager = rulerManager;
        this.PermissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, что команду выполнил именно игрок
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнить только игрок.");
            return false;
        }

        Player player = (Player) sender;

        // Проверка прав с помощью PermissionManager
        if (!PermissionManager.hasPermission(player, "your.permission.node")) {
            player.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }

        // Проверяем количество аргументов команды
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Укажите имя правителя.");
            return false;
        }

        String rulerName = args[0];
        Player ruler = Bukkit.getPlayer(rulerName);

        // Проверяем, существует ли указанный игрок
        if (ruler == null) {
            player.sendMessage(ChatColor.RED + "Игрок с именем " + rulerName + " не найден.");
            return false;
        }

        // Добавляем правителя и его команду в RulerManager
        List<Player> team = new ArrayList<>();  // Создаем пустую команду для правителя
        team.add(player);  // Добавляем игрока, который ввел команду, в команду правителя

        rulerManager.setRuler(ruler, team);  // Добавляем правителя и его команду

        player.sendMessage(ChatColor.GREEN + "Правитель " + rulerName + " был установлен.");
        return true;
    }
}
