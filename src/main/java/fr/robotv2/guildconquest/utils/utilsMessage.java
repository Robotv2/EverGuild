package fr.robotv2.guildconquest.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class utilsMessage {

        public void inviteAccept(Player player) {
            TextComponent text = new TextComponent("§7Cliquez ici pour §aaccepter §7l'invitation.");
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§7Cliquez ici pour §aaccepter §7l'invitation.")
                    .create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild accept"));
            player.spigot().sendMessage(text);
        }

        public void inviteDeny(Player player) {
            TextComponent text = new TextComponent("§7Cliquez ici pour §crefuser §7l'invitation.");
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§7Cliquez ici pour §crefuser §7l'invitation.")
                    .create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild deny"));
            player.spigot().sendMessage(text);
        }

        public void sendConfirmation(Player player) {
            TextComponent text = new TextComponent("§7Cliquez ici pour §b§lCONFIRMER §7l'action.");
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§bConfirmer l'action.")
                            .create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild confirm"));
            player.spigot().sendMessage(text);
        }
}
