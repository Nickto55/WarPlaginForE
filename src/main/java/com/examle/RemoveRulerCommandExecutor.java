package com.examle;

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
        if (sender instanceof Player) {
            Player ruler = (Player) sender;

            if (args.length == 0) {
                ruler.sendMessage(ChatColor.RED + "Вы должны указать имя правителя.");
                return false;
            }

            // Получаем правителя, которого нужно удалить
            Player targetRuler = ruler.getServer().getPlayer(args[0]);

            if (targetRuler == null) {
                ruler.sendMessage(ChatColor.RED + "Правитель с таким именем не найден.");
                return false;
            }

            // Проверяем, является ли игрок правителем
            if (!rulerManager.isRuler(targetRuler)) {
                ruler.sendMessage(ChatColor.RED + "Этот игрок не является правителем.");
                return false;
            }

            // Удаляем правителя и его команду
            if (rulerManager.removeRulerAndTeam(targetRuler)) {
                ruler.sendMessage(ChatColor.GOLD + "Правитель " + targetRuler.getName() + " и его команда были удалены.");

                // Сохраняем изменения в файл
                rulerManager.saveRulersToFile();
            } else {
                ruler.sendMessage(ChatColor.RED + "Не удалось удалить правителя.");
            }
        }

        return true;
    }
}
