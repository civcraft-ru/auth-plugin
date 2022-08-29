package org.bluk.listeners;

import org.bluk.AuthPlugin;
import org.bluk.entities.PlayerConnection;
import org.bluk.storages.PlayerConnectionStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.logging.Level;

public class PlayerLogin implements Listener {
    @EventHandler
    public static void handleEvent(PlayerLoginEvent event) {
        // Trying to get player from PlayerConnection storage
        PlayerConnectionStorage storage = AuthPlugin.getPlayerConnectionStorage();
        Player player = event.getPlayer();
        PlayerConnection connection = storage.get(event.getAddress());

        if (connection == null) {
            // Kicking player
            player.kickPlayer("Connection error");
        } else {
            // Adding UUID to PlayerConnection
            connection.setPlayer(player);

            // Starting authorization process
            connection.authorize();
        }
    };
}
