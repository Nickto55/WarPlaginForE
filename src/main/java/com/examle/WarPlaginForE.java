package com.examle;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class WarPlaginForE extends JavaPlugin {
    private RulerManager rulerManager;
    PermissionManager permissionManager = new PermissionManager(); // Инициализация PermissionManager


    @Override
    public void onEnable() {
        // Создаем папку для плагина, если ее нет
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        // Инициализация менеджера правителей
        rulerManager = new RulerManager(pluginFolder);

        // Регистрируем команды
        getCommand("setruler").setExecutor(new SetRulerCommandExecutor(rulerManager, permissionManager));
        getCommand("removeruler").setExecutor(new RemoveRulerCommandExecutor(rulerManager, permissionManager));
        getCommand("addplayer").setExecutor(new AddPlayerCommandExecutor(rulerManager));
        getCommand("removeplayer").setExecutor(new RemovePlayerCommandExecutor(rulerManager));
        getCommand("choosestrategy").setExecutor(new ChooseStrategyCommandExecutor(rulerManager));
        getCommand("choosestrategy").setTabCompleter(new ChooseStrategyTabCompleter());
        getCommand("listruler").setExecutor(new ListRulerCommandExecutor(rulerManager));
        getCommand("seelistarmy").setExecutor(new SeeListArmyCommandExecutor(rulerManager));
        getCommand("seemycommands").setExecutor(new SeeMyComandsCommandExecutor(rulerManager));
        getCommand("listmyarmy").setExecutor(new ListMyArmyCommandExecutor(rulerManager));

        getLogger().info("Плагин WarPlaginForE включен.");
    }

    @Override
    public void onDisable() {
        // Сохранение всех правителей в файл перед отключением
        rulerManager.saveRulersToFile();
        getLogger().info("Плагин WarPlaginForE выключен.");
    }
}
