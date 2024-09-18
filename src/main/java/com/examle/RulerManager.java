package com.examle;

import com.google.gson.Gson;

import com.google.common.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.lang.reflect.Type;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulerManager {
    private final JavaPlugin plugin;
    private final File dataFile;
    private final Map<Player, List<Player>> rulerTeams = new HashMap<>();
    private final String configFileName = "rulerTeams.yml"; // Название конфигурационного файла

//    // Инициализируем dataFile, указывая путь к файлу, где будут храниться данные
//    this.dataFile = new File(plugin.getDataFolder(), "rulersData.yml");
    public RulerManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Инициализируем dataFile, указывая путь к файлу, где будут храниться данные
        this.dataFile = new File(plugin.getDataFolder(), "rulersData.yml");

        // Проверяем, существует ли папка плагина, и создаем её, если нет
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }


    Gson gson = new Gson();
    private Map<Player, String> rulerTactics = new HashMap<>();

    private Map<Player, List<Player>> rulers = new HashMap<>();
//    private File dataFile;

    public void setTactic(Player ruler, String tactic) {
        rulerTactics.put(ruler, tactic);
        applyTacticToTeam(ruler, tactic); // Применяем эффекты для команды правителя
    }

    private Map<Player, List<Player>> teams;


    public boolean removeRulerAndTeam(Player ruler) {
        // Получаем команду правителя
        List<Player> team = teams.get(ruler);

        // Если правителя нет в списке, возвращаем false
        if (team == null) {
            return false;
        }

        // Удаляем всех игроков из команды
        teams.remove(ruler);

        // Сохраняем изменения в файл
        saveRulersToFile();

        return true;
    }

    public Map<Player, List<Player>> getAllRulers() {
        return rulers;
    }

//    private final Map<Player, List<Player>> rulerTeams = new HashMap<>();

    public void applyTacticToTeam(Player ruler, String tactic) {
        List<Player> team = getTeam(ruler);
        // Получаем команду правителя
        if (team == null || team.isEmpty()) {
            ruler.sendMessage(ChatColor.GRAY + "У вас нет команды для применения тактики.");
            ruler.sendMessage(ChatColor.GRAY + "Вы ничтожны!");
            return;
        }
        for (Player player : team) {
            clearPlayerEffects(ruler);

            // Убираем предыдущие эффекты со всей команды
            clearPlayerEffects(player);  // Метод для очистки эффектов

            // Применяем новую тактику
            applyTacticEffects(player, tactic);
            applyTacticEffects(ruler, tactic);
        }
    }

    private void clearPlayerEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    private void loadTeams() {
        FileConfiguration config = plugin.getConfig();
        // Логика загрузки данных из конфигурационного файла в rulerTeams
        // Пример загрузки:
         for (String rulerName : config.getKeys(false)) {
             Player ruler = getPlayerByName(rulerName); // Метод получения игрока по имени
             if (ruler != null) {
                 List<Player> team = new ArrayList<>();
                 for (String playerName : config.getStringList(rulerName)) {
                     Player player = getPlayerByName(playerName); // Метод получения игрока по имени
                     if (player != null) {
                         team.add(player);
                     }
                 }
                 rulerTeams.put(ruler, team);
             }
         }
    }


//    public RulerManager(JavaPlugin plugin) {
//        this.plugin = plugin;
//        loadTeams(); // Загружаем команды из конфигурации при инициализации
//    }

    // Метод для сохранения правителей и их команд
    public void saveRulers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rulers.txt"))) {
            for (Map.Entry<Player, List<Player>> entry : rulers.entrySet()) {
                Player ruler = entry.getKey();
                List<Player> team = entry.getValue();

                // Проверяем, что команда не null
                if (team != null) {
                    writer.write(ruler.getName() + ": ");
                    for (Player member : team) {
                        writer.write(member.getName() + " ");
                    }
                    writer.newLine();
                } else {
                    // Если команда null, просто сохраняем имя правителя без команды
                    writer.write(ruler.getName() + ": No team");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRulersToFile() {
        try (Writer writer = new FileWriter(dataFile)) {
            Map<String, List<String>> rulerData = new HashMap<>();
            for (Map.Entry<Player, List<Player>> entry : rulers.entrySet()) {
                String rulerName = entry.getKey().getName();
                List<String> teamNames = new ArrayList<>();
                for (Player player : entry.getValue()) {
                    teamNames.add(player.getName());
                }
                rulerData.put(rulerName, teamNames);
            }
            gson.toJson(rulerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Player, List<Player>> rulerData = new HashMap<>(); // Инициализация

    public void loadRulersFromFile() {

        if (!dataFile.exists()) return;
        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            Map<String, List<String>> rulerData = gson.fromJson(reader, type);
            for (Map.Entry<String, List<String>> entry : rulerData.entrySet()) {
                Player ruler = getPlayerByName(entry.getKey());
                List<Player> team = new ArrayList<>();
                for (String playerName : entry.getValue()) {
                    Player teamPlayer = getPlayerByName(playerName);
                    if (teamPlayer != null) {
                        team.add(teamPlayer);
                    }
                }
                rulers.put(ruler, team);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player getPlayerByName(String name) {
        return org.bukkit.Bukkit.getPlayer(name);
    }

    public void setRuler(Player ruler, List<Player> team) {
        rulers.put(ruler, team);
    }

    // Получение команды игрока
    public List<Player> getTeam(Player player) {
        return rulerTeams.getOrDefault(player, new ArrayList<>());
    }



//    public void removeRuler(Player ruler) {
//        rulers.remove(ruler);
//    }

    // Метод для загрузки правителей из файла
    public void loadRulers() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        for (String rulerName : config.getKeys(false)) {
            Player ruler = findPlayerByName(rulerName); // Ищем правителя по имени
            List<String> teamNames = config.getStringList(rulerName);

            List<Player> team = new ArrayList<>();
            for (String playerName : teamNames) {
                Player teamMember = findPlayerByName(playerName); // Ищем игрока по имени
                if (teamMember != null) {
                    team.add(teamMember);
                }
            }

            if (ruler != null) {
                rulers.put(ruler, team); // Восстанавливаем команду правителя
            }
        }
    }

    // Простой метод для поиска игрока по имени
    private Player findPlayerByName(String playerName) {
        return org.bukkit.Bukkit.getPlayer(playerName);
    }

    // Методы для управления правителями и командами (уже реализованы)
//    public void setRuler(Player ruler, List<Player> team) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter("rulers.txt"));
//        if (team != null) {
//            writer.write(ruler.getName() + ": ");
//            for (Player member : team) {
//                writer.write(member.getName() + " ");
//            }
//            writer.newLine();
//        } else {
//            // Если команда null, просто сохраняем имя правителя без команды
//            writer.write(ruler.getName() + ": No team");
//            writer.newLine();
//        }
//        rulers.put(ruler, team);
//        saveRulers(); // Сохраняем данные после изменения
//    }
    public void setRuler(Player ruler) {
        rulers.put(ruler, null);
        saveRulers(); // Сохраняем данные после изменения
    }

    public void removeRuler(Player ruler) {
        rulers.remove(ruler);
        saveRulers(); // Сохраняем данные после изменения
    }

//    public List<Player> getTeam(Player ruler) {
//        return rulers.getOrDefault(ruler, new ArrayList<>());
//    }

    // Метод проверки, является ли игрок правителем
    public boolean isRuler(Player player) {
        // Реализуйте логику проверки правителя
        return true; // Пример, замените по необходимости
    }

    // Добавление игрока в команду
    public boolean addPlayerToTeam(Player ruler, Player newPlayer) {
        // Получаем или создаем список команды правителя
        List<Player> team = rulerTeams.computeIfAbsent(ruler, k -> new ArrayList<>());

        // Если игрок уже в команде, возвращаем false
        if (team.contains(newPlayer)) {
            return false;
        }

        // Добавляем игрока в команду и сохраняем изменения
        team.add(newPlayer);
        saveTeams();
        return true;
    }

    public boolean removePlayerFromTeam(Player ruler, Player player) {
        if (rulers.containsKey(ruler)) {
            rulers.get(ruler).remove(player);
            saveRulers(); // Сохраняем изменения
            return true;
        } else {
            return false;
        }
    }

    // Получаем правителя для игрока
    public Player getRulerForPlayer(Player player) {
        for (Map.Entry<Player, List<Player>> entry : rulers.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Сохранение данных команды в конфигурационный файл
    private void saveTeams() {
        FileConfiguration config = plugin.getConfig();
        // Логика сохранения данных из rulerTeams в конфигурационный файл
        // Пример сохранения:
         for (Map.Entry<Player, List<Player>> entry : rulerTeams.entrySet()) {
             String rulerName = entry.getKey().getName();
             List<String> playerNames = new ArrayList<>();
             for (Player player : entry.getValue()) {
                 playerNames.add(player.getName());
             }
             config.set(rulerName, playerNames);
         }
         plugin.saveConfig(); // Сохраняем изменения в конфигурационном файле
    }

    private void applyTacticEffects(Player player, String tactic) {
        player.getActivePotionEffects().clear();


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
