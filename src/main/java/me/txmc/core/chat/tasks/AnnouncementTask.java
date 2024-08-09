package me.txmc.core.chat.tasks;

import me.txmc.core.Localization;
import me.txmc.core.patch.listeners.ChestLagFix;
import me.txmc.core.util.GlobalUtils;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AnnouncementTask implements Runnable {

    private ThreadLocalRandom random = ThreadLocalRandom.current();
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Localization loc = Localization.getLocalization(p.locale().getLanguage());
            List<TextComponent> announcements = loc.getStringList("announcements")
                    .stream()
                    .map(s -> s.replace("%prefix%", loc.getPrefix()))
                    .map(GlobalUtils::translateChars).toList();
            TextComponent announcement = announcements.get(random.nextInt(announcements.size()));
            p.sendMessage(announcement);
        }
    }
}
