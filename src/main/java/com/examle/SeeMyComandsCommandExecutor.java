package com.examle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SeeMyComandsCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    public SeeMyComandsCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Получаем правителя игрока
            Player ruler = rulerManager.getRulerForPlayer(player);

            if (ruler == null) {
                player.sendMessage("Вы не находитесь в команде.");
                return true;
            }

            // Получаем команду правителя
            List<Player> team = rulerManager.getTeam(ruler);

            // Выводим информацию о правителе и команде
            player.sendMessage("Ваш правитель: " + ruler.getName());
            player.sendMessage("Члены команды:");
            for (Player teamMember : team) {
                player.sendMessage("- " + teamMember.getName());
            }
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
        }

        return true;
    }
}
