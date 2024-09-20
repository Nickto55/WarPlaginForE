package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulerManager {
    private Map<Player, List<Player>> rulers = new HashMap<>();
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private Map<Player, List<Player>> rulersAndTeams;

    public RulerManager(File pluginFolder) {
        // Инициализация файла для сохранения данных
        dataFile = new File(pluginFolder, "rulers.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        rulersAndTeams = new HashMap<>();
        loadRulersFromFile();
    }

    // Загрузка правителей из файла
    private void loadRulersFromFile() {
        if (!dataFile.exists()) return;
        for (String key : dataConfig.getKeys(false)) {
            Player ruler = Bukkit.getPlayer(key);
            List<Player> team = (List<Player>) dataConfig.getList(key);
            rulers.put(ruler, team);
        }
    }

    public class PermissionManager {
        private static final String ALLOWED_PLAYER_UUID = "ваш-uuid-игрока"; // Замените на UUID игрока

        public boolean hasPermission(Player player) {
            return player.getUniqueId().toString().equals(ALLOWED_PLAYER_UUID);
        }
    }

    public void clearPlayerEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    // Сохранение правителей в файл
    public void saveRulersToFile() {
        for (Map.Entry<Player, List<Player>> entry : rulers.entrySet()) {
            dataConfig.set(entry.getKey().getName(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Добавление правителя и его команды
    public void setRuler(Player ruler, List<Player> team) {
        rulers.put(ruler, team);
        saveRulersToFile();
    }

    // Удаление правителя
    public boolean removeRulerAndTeam(Player ruler) {
        if (rulers.containsKey(ruler)) {
            rulers.remove(ruler);
            saveRulersToFile();
            return true;
        }
        return false;
    }

    // Метод для добавления игрока в команду правителя
    public boolean addPlayerToTeam(Player ruler, Player playerToAdd) {
        // Проверяем, является ли игрок правителем
        if (!isRuler(ruler)) {
            return false; // Если не правитель, возвращаем false
        }

        // Получаем команду правителя
        List<Player> team = rulersAndTeams.get(ruler);

        // Если команда правителя пуста, создаем новый список для его команды
        if (team == null) {
            team = new ArrayList<>();
            rulersAndTeams.put(ruler, team);
        }

        // Проверяем, находится ли уже игрок в команде
        if (team.contains(playerToAdd)) {
            return false; // Если игрок уже в команде, не добавляем его
        }

        // Добавляем игрока в команду правителя
        team.add(playerToAdd);
        return true; // Возвращаем true, если игрок успешно добавлен
    }



    // Метод для проверки, является ли игрок правителем
    public boolean isRuler(Player player) {
        return rulers.containsKey(player); // Проверка, существует ли запись для данного игрока в списке правителей
    }

    // Получение команды правителя
    public List<Player> getTeam(Player ruler) {
        return rulers.get(ruler);
    }

    // Возвращает карту всех правителей и их команд
    public Map<Player, List<Player>> getAllRulers() {
        return rulers;
    }

    public boolean removePlayerFromTeam(Player ruler, Player playerToRemove) {
        // Проверяем, является ли игрок правителем
        if (!isRuler(ruler)) {
            return false; // Если не правитель, возвращаем false
        }

        // Получаем команду правителя
        List<Player> team = rulersAndTeams.get(ruler);

        // Если команда пуста или игрока нет в команде, возвращаем false
        if (team == null || !team.contains(playerToRemove)) {
            return false;
        }

        // Удаляем игрока из команды
        team.remove(playerToRemove);

        // Если команда пустая после удаления, можно очистить ее запись
        if (team.isEmpty()) {
            rulersAndTeams.remove(ruler);
        }

        return true; // Возвращаем true, если игрок успешно удален
    }

    // Проверка, принадлежит ли игрок правителю
    public boolean isPlayerInRulerTeam(Player player) {
        for (List<Player> team : rulers.values()) {
            if (team.contains(player)) {
                return true;
            }
        }
        return false;
    }

    private void applyTacticEffects(Player player, String tactic) {
        player.getActivePotionEffects().clear();
        clearPlayerEffects(player);


        switch (tactic) {
            case "Блицкриг":
                player.getActivePotionEffects().clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 1)); // Скорость II
                break;
            case "Глубокая_оборона":
                player.getActivePotionEffects().clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 600000, 1)); // Сопротивление II
                break;
            case "Активное_наступление":
                player.getActivePotionEffects().clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600000, 0)); // Сила I
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 0)); // Скорость I
                break;
            case "Оперативное_взаимодействие":
                player.getActivePotionEffects().clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600000, 0)); // Скорость I
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 600000, 0)); // Сопротивление I
                break;
        }
    }
}
