package org.bluk.auth.storages;

import org.bluk.auth.entities.PlayerConnection;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class PlayerConnectionStorage {
    private final Map<InetAddress, PlayerConnection> entities = new HashMap<>();

    public PlayerConnection get(InetAddress address) { return this.entities.get(address); };
    public void put(InetAddress address, PlayerConnection playerConnection) { this.entities.put(address, playerConnection); };

    // Creators
    public PlayerConnection createConnection(InetAddress address, String originAddress) {
        return new PlayerConnection(this, address, originAddress);
    }
}
