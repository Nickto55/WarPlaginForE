package com.examle;

import org.bukkit.entity.Player;

public class PermissionManager {

    // Проверка, есть ли у игрока права на выполнение команды
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    // Метод для установки прав для игрока (опционально)
    public void addPermission(Player player, String permission) {
        // Реализация добавления прав, возможно через какой-то API или систему прав
    }

    public void removePermission(Player player, String permission) {
        // Реализация удаления прав, возможно через какой-то API или систему прав
    }
}
