package com.examle;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class WarPlaginForE extends JavaPlugin {

    private RulerManager rulerManager;

//    private DatabaseManager databaseManager;

    private DatabaseManager databaseManager = new DatabaseManager(); // Инициализация при объявлении


    @Override
    public void onEnable() {
        // Инициализация DatabaseManager
        databaseManager = new DatabaseManager();

        // Попытка подключения к базе данных
        try {
            databaseManager.connect();
            getLogger().info("Подключение к базе данных установлено успешно.");
        } catch (SQLException e) {
            getLogger().severe("Не удалось подключиться к базе данных: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return; // Прерываем выполнение плагина, если подключение не удалось
        }

        RulerManager rulerManager = new RulerManager(databaseManager);

        // Инициализация команд
        getCommand("setruler").setExecutor(new SetRulerCommandExecutor(rulerManager));
        getCommand("removeruler").setExecutor(new RemoveRulerCommandExecutor(rulerManager));
        getCommand("addplayer").setExecutor(new AddPlayerCommandExecutor(rulerManager));
        getCommand("removeplayer").setExecutor(new RemovePlayerCommandExecutor(rulerManager));
        getCommand("choosestrategy").setExecutor(new ChooseStrategyCommandExecutor(rulerManager));
        getCommand("choosestrategy").setTabCompleter(new ChooseStrategyTabCompleter());
        getCommand("listruler").setExecutor(new ListRulerCommandExecutor(rulerManager));
        getCommand("seelistarmy").setExecutor(new SeeListArmyCommandExecutor(rulerManager));
        getCommand("seemycommands").setExecutor(new SeeMyComandsCommandExecutor(rulerManager));
        getCommand("listmyarmy").setExecutor(new ListMyArmyCommandExecutor(rulerManager));

        // Регистрация событий
        getServer().getPluginManager().registerEvents(new RulerInventory(this), this);
    }

    @Override
    public void onDisable() {
        // Закрытие соединения с базой данных
        if (databaseManager != null) {
            try {
                databaseManager.close();
            } catch (SQLException e) {
                getLogger().severe("Ошибка при закрытии подключения к базе данных: " + e.getMessage());
            }
        }
    }
}
