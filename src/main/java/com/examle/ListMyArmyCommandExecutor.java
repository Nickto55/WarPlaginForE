package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListMyArmyCommandExecutor implements CommandExecutor {
    private RulerManager rulerManager;

    public ListMyArmyCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<Player> team = rulerManager.getTeam(player);
            if (team.isEmpty()) {
                player.sendMessage("У вас нет команды.");
            } else {
                player.sendMessage("Ваша команда:");
                for (Player member : team) {
                    player.sendMessage("- " + member.getName());
                }
            }
        }
        return true;
    }
}


