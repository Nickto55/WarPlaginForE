package com.examle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseStrategyTabCompleter implements TabCompleter {

    private static final List<String> STRATEGIES = Arrays.asList(
            "Блицкриг",
            "Глубокая_оборона",
            "Активное_наступление",
            "Оперативное_взаимодействие"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Проверяем, является ли отправитель игроком и количество аргументов
        if (sender instanceof Player && args.length == 1) {
            List<String> completions = new ArrayList<>();

            // Если игрок уже ввел часть команды, предлагаем возможные стратегии
            for (String strategy : STRATEGIES) {
                if (strategy.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(strategy);
                }
            }

            return completions;
        }

        return null; // Если условий нет, возвращаем null для стандартного поведения
    }
}
