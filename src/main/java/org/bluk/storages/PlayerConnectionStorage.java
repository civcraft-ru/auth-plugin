package org.bluk.storages;

import org.bluk.AuthPlugin;
import org.bluk.entities.PlayerConnection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerConnectionStorage {
    private final Map<InetAddress, PlayerConnection> entities = new HashMap<>();

    public PlayerConnection get(InetAddress address) { return this.entities.get(address); };
    public void put(InetAddress address, PlayerConnection playerConnection) { this.entities.put(address, playerConnection); };

    // Creators
    public PlayerConnection createConnection(InetAddress address, String originAddress) {
        return new PlayerConnection(this, address, originAddress);
    }
}
