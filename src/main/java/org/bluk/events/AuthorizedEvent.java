package org.bluk.events;

import org.bukkit.entity.Player;

public class AuthorizedEvent extends BaseEvent {
    public AuthorizedEvent(Player player, String token) {
        this.player = player;
        this.token = token;
    };

    // Parameters
    private final Player player;
    private final String token;

    // Getters
    public Player getPlayer() { return player; };
    public String getToken() { return token; };
}
