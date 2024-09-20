package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AddPlayerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;
    private final FileManager fileManager = new FileManager(); // менеджер для работы с файлами

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
            player.sendMessage("Укажите игрока для добавления.");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage("Игрок не найден.");
            return false;
        }

        if (!rulerManager.isRuler(player)) {
            player.sendMessage("Вы не являетесь правителем.");
            return false;
        }

        List<Player> team = rulerManager.getTeam(player);
        team.add(targetPlayer);
        fileManager.saveTeamToFile(player, team); // Сохраняем обновленную команду в файл

        player.sendMessage("Игрок " + targetPlayer.getName() + " добавлен в вашу команду.");
        return true;
    }
}

