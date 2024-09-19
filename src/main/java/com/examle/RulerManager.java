package com.examle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RulerManager {
    private JavaPlugin plugin;
    private Connection connection; // Объект для соединения с базой данных
    private Map<Player, List<Player>> rulers; // Ключ - правитель, значение - команда

    private final Map<Player, String> tactics = new HashMap<>();
    private final DatabaseManager databaseManager;

    public RulerManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addRuler(String rulerName) throws SQLException {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO rulers (ruler_name) VALUES (?)")) {
            statement.setString(1, rulerName);
            statement.executeUpdate();
        }
    }


    public RulerManager(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeDatabase(); // Инициализация базы данных при создании менеджера
    }

    public void addPlayerToTeam(Player ruler, Player player) {
        // Проверяем, является ли игрок правителем
        if (!rulers.containsKey(ruler)) {
            // Если нет, создаем новую команду для правителя
            rulers.put(ruler, new ArrayList<>());
        }

        // Добавляем игрока в команду правителя
        List<Player> team = rulers.get(ruler);
        if (!team.contains(player)) {
            team.add(player);
        }
    }

    public Player getRulerForPlayer(Player player) {
        for (Map.Entry<Player, List<Player>> entry : rulers.entrySet()) {
            Player ruler = entry.getKey();
            List<Player> team = entry.getValue();
            if (team.contains(player)) {
                return ruler;
            }
        }
        return null; // Если правитель не найден
    }

    public List<Player> getTeam(Player ruler) {
        return rulers.getOrDefault(ruler, new ArrayList<>());
    }


    // Метод для инициализации соединения с базой данных
    private void initializeDatabase() {
        try {
            // Замените URL, USERNAME и PASSWORD на соответствующие значения
            String url = "jdbc:mysql://localhost:3306/war_plugin_db";
            String user = "root";
            String password = "yourpassword";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Не удалось подключиться к базе данных.");
        }
    }

    public Map<Player, List<Player>> getRulersMap() {
        Map<Player, List<Player>> rulersMap = new HashMap<>();
        List<Ruler> rulers = getAllRulers();

        for (Ruler ruler : rulers) {
            Player rulerPlayer = Bukkit.getPlayer(ruler.getName());
            if (rulerPlayer != null) {
                List<Player> team = getTeamFromRuler(ruler); // Метод для получения команды
                rulersMap.put(rulerPlayer, team);
            }
        }

        return rulersMap;
    }

    private List<Player> getTeamFromRuler(Ruler ruler) {
        // Реализуйте метод для получения команды из Ruler
        // Например, это может быть List<Player>, если у вас есть соответствующие данные
        List<Player> team = new ArrayList<>();
        for (String playerName : ruler.getTeamNames()) {
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) {
                team.add(player);
            }
        }
        return team;
    }

    // Метод для назначения игрока правителем
    public void setRuler(Player ruler) {
        // Создаем пустую команду для нового правителя
        rulers.put(ruler, new ArrayList<>());

        // Дополнительно можете сохранить правителя в базу данных
        try (Connection connection = getDatabaseConnection()) {
            String query = "INSERT INTO rulers (ruler_name, team_names) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, ruler.getName());
                statement.setString(2, ""); // Пустой список команды
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения соединения с базой данных
    private Connection getDatabaseConnection() throws SQLException {
        // Ваш код для получения соединения с базой данных
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/war_plugin_db", "root", "yourpassword");
    }

    // Метод для сохранения правителей в базу данных
    public void saveRulersToDatabase() {
        if (connection == null) {
            plugin.getLogger().severe("Соединение с базой данных не инициализировано.");
            return;
        }

        String sql = "INSERT INTO rulers (ruler_name, team_names) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Ruler> rulers = getAllRulers(); // Получение всех правителей
            for (Ruler ruler : rulers) {
                statement.setString(1, ruler.getName());
                statement.setString(2, String.join(",", ruler.getTeamNames()));
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Ошибка при сохранении правителей в базу данных.");
        }
    }

    // Метод для получения всех правителей из базы данных
    public List<Ruler> getAllRulers() {
        List<Ruler> rulers = new ArrayList<>();
        String sql = "SELECT ruler_name, team_names FROM rulers";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("ruler_name");
                String teamNamesString = resultSet.getString("team_names");
                List<String> teamNames = Arrays.asList(teamNamesString.split(","));
                Ruler ruler = new Ruler(name, teamNames);
                rulers.add(ruler);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Ошибка при получении правителей из базы данных.");
        }
        return rulers;
    }

    public boolean isRuler(Player player) {
        // Проверьте, является ли игрок правителем, используя вашу логику
        // Например, если у вас есть список правителей
        return rulers.containsKey(player);
    }

    public void removeRuler(Player ruler) {
        // Удаление правителя из вашей структуры данных
        // Удаление команды правителя, если нужно
        rulers.remove(ruler);

        // Обновите базу данных, если нужно
        saveRulersToDatabase();
    }

    // Метод для удаления игрока из команды правителя
    public boolean removePlayerFromTeam(Player playerToRemove) {
        // Получаем правителя игрока
        Player ruler = getRulerForPlayer(playerToRemove);

        // Проверяем, является ли игрок частью команды
        if (ruler != null && rulers.containsKey(ruler)) {
            List<Player> team = rulers.get(ruler);

            // Проверяем, есть ли игрок в команде
            if (team.remove(playerToRemove)) {
                // Обновляем список команды правителя в базе данных, если нужно
                 saveRulersToDatabase(); // Например, можно сохранить изменения в базе данных

                return true; // Игрок успешно удален
            }
        }

        return false; // Игрок не был найден или не был удален
    }

    // Метод для обновления команды правителя в базе данных
    private void updateTeamInDatabase(Player ruler, List<Player> team) {
        try (Connection connection = getDatabaseConnection()) {
            String query = "UPDATE rulers SET team_names = ? WHERE ruler_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Преобразуем список игроков в строку
                String teamNames = String.join(",", team.stream().map(Player::getName).toArray(String[]::new));
                statement.setString(1, teamNames);
                statement.setString(2, ruler.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Закрытие соединения при отключении плагина
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Ошибка при закрытии соединения с базой данных.");
            }
        }
    }
    // Метод для установки тактики для правителя
    public boolean setTactic(Player ruler, String tactic) {
        if (ruler == null || tactic == null || tactic.isEmpty()) {
            return false; // Не удалось установить тактику из-за некорректных входных данных
        }

        // Устанавливаем тактику для правителя
        applyTacticEffects(ruler, tactic);
        applyTacticToTeam(ruler, tactic);
        tactics.put(ruler, tactic);
        return true; // Тактика успешно установлена
    }

    public String getTactic(Player ruler) {
        return tactics.getOrDefault(ruler, "Нет тактики"); // Возвращает тактику или сообщение по умолчанию
    }

    public void applyTacticToTeam(Player ruler, String tactic) {
        List<Player> team = getTeam(ruler); // Получаем команду правителя

        for (Player member : team) {
            applyTacticEffects(member, tactic); // Применяем тактику к каждому игроку
        }
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
