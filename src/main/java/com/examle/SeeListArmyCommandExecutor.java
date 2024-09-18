package com.examle;

import com.examle.RulerManager;
import com.examle.SetRulerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SeeListArmyCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public SeeListArmyCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Пожалуйста, укажите ник правителя.");
            return false;
        }

        String rulerName = args[0];
        Player ruler = Bukkit.getPlayer(rulerName); // Ищем игрока по нику

        if (ruler == null || !rulerManager.isRuler(ruler)) {
            sender.sendMessage(ChatColor.RED + "Нет правителя с таким ником.");
            return false;
        }

        List<Player> team = rulerManager.getTeam(ruler);
        if (team.isEmpty()) {
            sender.sendMessage(ChatColor.GOLD + "Команда правителя " + rulerName + " пуста.");
        } else {
            sender.sendMessage(ChatColor.AQUA + "Команда правителя " + rulerName + ":");
            for (Player member : team) {
                sender.sendMessage(ChatColor.GREEN + "- " + member.getName());
            }
        }

        return true;
    }
}

