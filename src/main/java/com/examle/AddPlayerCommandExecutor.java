package com.examle;

import org.bukkit.Bukkit;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может выполнять только игрок.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Укажите имя игрока для добавления в команду.");
            return false;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            player.sendMessage("Игрок с таким именем не найден.");
            return false;
        }

        Player ruler = rulerManager.getRulerForPlayer(player);
        if (ruler == null) {
            player.sendMessage("Вы не являетесь частью команды правителя.");
            return false;
        }

        rulerManager.addPlayerToTeam(ruler, target);
        player.sendMessage("Игрок " + targetName + " был добавлен в команду вашего правителя.");

        return true;
    }
}
