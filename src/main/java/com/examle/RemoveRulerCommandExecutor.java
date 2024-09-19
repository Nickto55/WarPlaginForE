package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveRulerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public RemoveRulerCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может выполнять только игрок.");
            return false; // Возвращаем false, чтобы показать, что команда не выполнена
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Укажите имя правителя для удаления.");
            return false;
        }

        String rulerName = args[0];
        Player ruler = Bukkit.getPlayer(rulerName);

        if (ruler == null || !rulerManager.isRuler(ruler)) {
            player.sendMessage("Правитель с таким именем не найден.");
            return false;
        }

        // Удаление правителя и его команды
        rulerManager.removeRuler(ruler); // Убедитесь, что этот метод существует в RulerManager
        player.sendMessage("Правитель " + rulerName + " и его команда были удалены.");

        return true; // Возвращаем true, чтобы показать, что команда выполнена успешно
    }
}
