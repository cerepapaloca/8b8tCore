package me.txmc.core.chat.commands;

import lombok.RequiredArgsConstructor;
import me.txmc.core.chat.ChatCommand;
import me.txmc.core.chat.ChatInfo;
import me.txmc.core.chat.ChatSection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.txmc.core.util.GlobalUtils.sendMessage;
import static me.txmc.core.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class ReplyCommand extends ChatCommand {
    private final ChatSection manager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                ChatInfo senderInfo = manager.getInfo(player);
                if (senderInfo.getReplyTarget() != null) {
                    Player target = senderInfo.getReplyTarget();
                    if (target.isOnline()) {
                        ChatInfo targetInfo = manager.getInfo(target);
                        String msg = ChatColor.stripColor(String.join(" ", args));
                        sendWhisper(player, senderInfo, target, targetInfo, msg);
                    } else sendPrefixedLocalizedMessage(player, "reply_player_offline", target.getName());
                } else sendPrefixedLocalizedMessage(player, "reply_no_target");
            } else sendPrefixedLocalizedMessage(player, "reply_command_syntax");
        } else sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
