package com.examle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChooseStrategyCommandExecutor implements CommandExecutor {
    private RulerManager rulerManager;

    public ChooseStrategyCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Проверяем, является ли игрок правителем
            if (rulerManager.isRuler(player)) {
                if (args.length > 0) {
                    String tactic = args[0];

                    // Устанавливаем тактику для правителя
                    rulerManager.setTactic(player, tactic);
                    player.sendMessage(ChatColor.YELLOW + "Вы выбрали тактику: " + tactic);

                    // Получаем команду правителя и отображаем её
                    List<Player> team = rulerManager.getTeam(player);
                    if (team.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "У вас нет команды.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "Ваша команда:");
                        for (Player member : team) {
                            player.sendMessage("- " + member.getName());
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Пожалуйста, укажите тактику.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Только правители могут выбирать тактики.");
            }
        } else {
            sender.sendMessage("Эту команду может выполнить только игрок.");
        }

        return true;
    }
}
