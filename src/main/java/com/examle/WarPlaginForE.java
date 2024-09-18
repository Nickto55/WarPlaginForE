package com.examle;

import org.bukkit.plugin.java.JavaPlugin;

public class WarPlaginForE extends JavaPlugin {

    private RulerManager rulerManager;

    @Override
    public void onEnable() {
        rulerManager = new RulerManager(this);
        // Инициализация менеджеров
        RulerManager rulerManager = new RulerManager(this);
        PermissionManager permissionManager = new PermissionManager();

        rulerManager.loadRulersFromFile();

        //добавление/снятия роли правителя
        getCommand("setruler").setExecutor(new SetRulerCommandExecutor(rulerManager));
        getCommand("removeruler").setExecutor(new RemoveRulerCommandExecutor(rulerManager));

        //Регистрация добавления/удаления игрока в армию/из армии
        getCommand("addplayer").setExecutor(new AddPlayerCommandExecutor(rulerManager));
        getCommand("removeplayer").setExecutor(new RemovePlayerCommandExecutor(rulerManager));

        // Регистрация выбранной тактики и автодополнения
        getCommand("choosestrategy").setExecutor(new ChooseStrategyCommandExecutor(rulerManager));
        getCommand("choosestrategy").setTabCompleter(new ChooseStrategyTabCompleter()); // Регистрация TabCompleter

        // Регистрация команды для отображения всех правителей
        getCommand("listruler").setExecutor(new ListRulerCommandExecutor(rulerManager));

        // Регистрация команды просмотра армии к которой ты приднадлежиш
        getCommand("SeeListArmy").setExecutor(new SeeListArmyCommandExecutor(rulerManager));

        // Регистрация команды setruler
//        getCommand("removeruler").setExecutor(new RemoveRulerCommandExecutor(rulerManager));

        getCommand("seemycommands").setExecutor(new SeeMyComandsCommandExecutor(rulerManager));


        // Регистрация событий
        getServer().getPluginManager().registerEvents(new RulerInventory(this), this);


        getCommand("listmyarmy").setExecutor(new ListMyArmyCommandExecutor(rulerManager)); // Регистрация команды

        // Регистрация слушателя инвентаря
        getServer().getPluginManager().registerEvents(new RulerInventory(this), this);
    }


    @Override
    public void onDisable() {
        // Логика отключения плагина, если нужно
        rulerManager.saveRulersToFile();
    }
}