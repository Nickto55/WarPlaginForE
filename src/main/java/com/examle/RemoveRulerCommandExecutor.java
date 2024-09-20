package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveRulerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;
    private final PermissionManager PermissionManager;

    public RemoveRulerCommandExecutor(RulerManager rulerManager, PermissionManager permissionManager) {
        this.rulerManager = rulerManager;
        this.PermissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может выполнять только игрок.");
            return false;
        }



        Player player = (Player) sender;

        // Проверка прав с помощью PermissionManager
        if (!PermissionManager.hasPermission(player, "your.permission.node")) {
            player.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage("Укажите имя правителя для удаления.");
            return false;
        }

        String rulerName = args[0];
        Player ruler = Bukkit.getPlayer(rulerName);

        if (ruler == null || !rulerManager.getAllRulers().containsKey(ruler)) {
            player.sendMessage("Правитель с таким именем не найден.");
            return false;
        }

        boolean success = rulerManager.removeRulerAndTeam(ruler);
        if (success) {
            player.sendMessage("Правитель " + rulerName + " и его команда были удалены.");
        } else {
            player.sendMessage("Ошибка при удалении правителя.");
        }

        return true;
    }
}
