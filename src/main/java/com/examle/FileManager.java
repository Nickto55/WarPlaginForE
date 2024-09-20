package com.examle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.bukkit.entity.Player;

public class FileManager {

    private static final String FILE_PATH = "rulers_teams.txt"; // путь к файлу

    // Сохранение команды в файл
    public void saveTeamToFile(Player ruler, List<Player> team) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("Рулевой: " + ruler.getName() + "\n");
            writer.write("Команда: \n");
            for (Player player : team) {
                writer.write(player.getName() + "\n");
            }
            writer.write("-----\n"); // разделитель между командами
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Удаление команды из файла (перезаписывание файла)
    public void removeTeamFromFile(Player ruler) {
        // Чтение, фильтрация и перезапись файла без команды рулевого
    }
}
