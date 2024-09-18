package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ListRulerCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public ListRulerCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Map<Player, List<Player>> rulers = rulerManager.getAllRulers();

        if (rulers.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "На данный момент нет правителей.");
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + "Список всех правителей:");
        for (Player ruler : rulers.keySet()) {
            sender.sendMessage(ChatColor.AQUA + "- " + ruler.getName());
        }

        return true;
    }
}
