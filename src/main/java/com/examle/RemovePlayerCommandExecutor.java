package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovePlayerCommandExecutor implements CommandExecutor {
    private RulerManager rulerManager;

    public RemovePlayerCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player ruler = (Player) sender;

            if (args.length == 0) {
                ruler.sendMessage("Вы должны указать имя игрока.");
                return false;
            }

            Player targetPlayer = ruler.getServer().getPlayer(args[0]);

            if (targetPlayer == null) {
                ruler.sendMessage("Игрок с таким именем не найден.");
                return false;
            }

            if (!rulerManager.isRuler(ruler)) {
                ruler.sendMessage(ChatColor.DARK_RED + "Вы не являетесь правителем.");
                return false;
            }

            if (rulerManager.removePlayerFromTeam(targetPlayer)) {
                ruler.sendMessage(ChatColor.GOLD + "Игрок " + targetPlayer.getName() + " был удален из вашей команды.");
            } else {
                ruler.sendMessage(ChatColor.RED + "Игрок " + targetPlayer.getName() + " не находится в вашей команде.");
            }
        }

        return true;
    }
}
