package de.scholle.timer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Timer extends JavaPlugin implements TabExecutor {

    private TimerTask timerTask;
    private long secondsElapsed;
    private ChatColor baseColor;

    private static final Map<String, ChatColor> COLOR_NAMES = Map.ofEntries(
            Map.entry("black", ChatColor.BLACK),
            Map.entry("dark_blue", ChatColor.DARK_BLUE),
            Map.entry("dark_green", ChatColor.DARK_GREEN),
            Map.entry("dark_aqua", ChatColor.DARK_AQUA),
            Map.entry("dark_red", ChatColor.DARK_RED),
            Map.entry("dark_purple", ChatColor.DARK_PURPLE),
            Map.entry("gold", ChatColor.GOLD),
            Map.entry("gray", ChatColor.GRAY),
            Map.entry("dark_gray", ChatColor.DARK_GRAY),
            Map.entry("blue", ChatColor.BLUE),
            Map.entry("green", ChatColor.GREEN),
            Map.entry("aqua", ChatColor.AQUA),
            Map.entry("red", ChatColor.RED),
            Map.entry("light_purple", ChatColor.LIGHT_PURPLE),
            Map.entry("yellow", ChatColor.YELLOW),
            Map.entry("white", ChatColor.WHITE)
    );

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        getCommand("timer").setExecutor(this);
        getCommand("timer").setTabCompleter(this);
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        String colorName = config.getString("base-color", "dark_green").toLowerCase();
        baseColor = COLOR_NAMES.getOrDefault(colorName, ChatColor.DARK_GREEN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Timer.op")) {
            sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung!");
            return true;
        }
        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                reloadConfig();
                loadConfigValues();
                sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                return true;
            }
            case "colour" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /timer colour <color>");
                    return true;
                }
                String colorName = args[1].toLowerCase();
                if (!COLOR_NAMES.containsKey(colorName)) {
                    sender.sendMessage(ChatColor.RED + "Unbekannte Farbe!");
                    return true;
                }
                getConfig().set("base-color", colorName);
                saveConfig();
                loadConfigValues();
                sender.sendMessage(ChatColor.GREEN + "Farbe geändert!");
                return true;
            }
            case "start" -> {
                startTimer();
                sender.sendMessage(ChatColor.GREEN + "Timer gestartet!");
                return true;
            }
            case "pause" -> {
                pauseTimer();
                sender.sendMessage(ChatColor.YELLOW + "Timer pausiert!");
                return true;
            }
            case "restart" -> {
                restartTimer();
                sender.sendMessage(ChatColor.GREEN + "Timer neu gestartet!");
                return true;
            }
            case "stop" -> {
                stopTimer();
                sender.sendMessage(baseColor + "Timer beendet nach " + formatTime(secondsElapsed));
                return true;
            }
            case "goal" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /timer goal <advancement>");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Advancement gesetzt: " + args[1]);
                return true;
            }
        }
        return false;
    }

    private void startTimer() {
        if (timerTask != null) timerTask.cancel();
        timerTask = new TimerTask();
        timerTask.runTaskTimer(this, 0, 20);
    }

    private void pauseTimer() {
        if (timerTask != null) timerTask.cancel();
    }

    private void restartTimer() {
        startTimer();
    }

    private void stopTimer() {
        if (timerTask != null) timerTask.cancel();
        timerTask = null;
    }

    private String formatTime(long totalSeconds) {
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "colour", "start", "pause", "restart", "stop", "goal")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("colour")) {
            return COLOR_NAMES.keySet().stream()
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private class TimerTask extends BukkitRunnable {

        @Override
        public void run() {
            secondsElapsed++;
            String timerText = formatTime(secondsElapsed);
            Component component = buildGradient(timerText, baseColor);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(component);
            }
        }

        private Component buildGradient(String text, ChatColor base) {
            Color startColor = chatColorToColor(base);
            Color endColor = startColor.darker().darker();
            int len = text.length();
            Component comp = Component.empty();

            for (int i = 0; i < len; i++) {
                float ratio = (float)i / (len - 1);
                Color c = blendColors(startColor, endColor, ratio);
                comp = comp.append(Component.text(String.valueOf(text.charAt(i)),
                        TextColor.color(c.getRed(), c.getGreen(), c.getBlue())).decorate(TextDecoration.BOLD));
            }
            return comp;
        }

        private Color chatColorToColor(ChatColor c) {
            return switch (c) {
                case GREEN, DARK_GREEN -> new Color(0, 170, 0);
                case DARK_BLUE -> new Color(0, 0, 170);
                case DARK_AQUA -> new Color(0, 170, 170);
                case DARK_RED -> new Color(170, 0, 0);
                case DARK_PURPLE -> new Color(170, 0, 170);
                case GOLD -> new Color(255, 170, 0);
                case GRAY -> new Color(170, 170, 170);
                case DARK_GRAY -> new Color(85, 85, 85);
                case BLUE -> new Color(85, 85, 255);
                case AQUA -> new Color(85, 255, 255);
                case RED -> new Color(255, 85, 85);
                case LIGHT_PURPLE -> new Color(255, 85, 255);
                case YELLOW -> new Color(255, 255, 85);
                case WHITE -> new Color(255, 255, 255);
                default -> new Color(0, 170, 0);
            };
        }

        private Color blendColors(Color start, Color end, float ratio) {
            int r = (int)(start.getRed() * (1 - ratio) + end.getRed() * ratio);
            int g = (int)(start.getGreen() * (1 - ratio) + end.getGreen() * ratio);
            int b = (int)(start.getBlue() * (1 - ratio) + end.getBlue() * ratio);
            return new Color(r, g, b);
        }
    }
}
