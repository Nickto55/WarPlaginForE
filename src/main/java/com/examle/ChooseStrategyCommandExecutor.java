package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ChooseStrategyCommandExecutor implements CommandExecutor {
    private final RulerManager rulerManager;

    // Конструктор для инициализации RulerManager
    public ChooseStrategyCommandExecutor(RulerManager rulerManager) {
        this.rulerManager = rulerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, что команду выполняет именно игрок
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнить только игрок.");
            return false;
        }

        Player player = (Player) sender;

        // Проверяем наличие аргумента (тактики)
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Укажите тактику: Блицкриг, Глубокая_оборона, Активное_наступление, Оперативное_взаимодействие.");
            return false;
        }

        String tactic = args[0];

        // Проверяем, является ли игрок правителем
        if (!rulerManager.isRuler(player)) {
            player.sendMessage(ChatColor.RED + "Вы не правитель.");
            return false;
        }

        // Получаем команду правителя
        List<Player> team = rulerManager.getTeam(player);
        if (team == null || team.isEmpty()) {
            player.sendMessage(ChatColor.RED + "У вас нет команды.");
            return false;
        }

        // Применяем выбранную тактику для всех игроков в команде
        for (Player teamMember : team) {
            applyTacticEffects(teamMember, tactic);
        }

        player.sendMessage(ChatColor.GREEN + "Тактика " + tactic + " успешно выбрана и применена к команде.");
        return true;
    }

    // Метод для применения эффекта в зависимости от выбранной тактики
    private void applyTacticEffects(Player player, String tactic) {
        // Очищаем все текущие эффекты зелья перед применением новых
        player.getActivePotionEffects().clear();
        rulerManager.clearPlayerEffects(player);

        switch (tactic) {
            case "Блицкриг":
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 1)); // Скорость II
                break;
            case "Глубокая_оборона":
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 600000, 1)); // Сопротивление II
                break;
            case "Активное_наступление":
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600000, 0)); // Сила I
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 0)); // Скорость I
                break;
            case "Оперативное_взаимодействие":
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 0)); // Скорость I
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 600000, 0)); // Сопротивление I
                break;
            default:
                player.sendMessage(ChatColor.RED + "Неизвестная тактика: " + tactic);
        }
    }
}
