package org.bluk.entities;

import org.bluk.AuthPlugin;
import org.bluk.events.AuthorizedEvent;
import org.bluk.processors.types.AbstractProcessor;
import org.bluk.processors.AuthProcessor;
import org.bluk.processors.types.OnStateCallback;
import org.bluk.processors.types.ProcessorState;
import org.bluk.storages.PlayerConnectionStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerConnection {
    public PlayerConnection(PlayerConnectionStorage storage, InetAddress address, String originAddress) {
        this.address = address;
        this.originAddress = originAddress;

        // Adding this entity to storage
        storage.put(address, this);
    }

    // Properties
    private final InetAddress address;
    private final String originAddress;
    private @Nullable UUID uuid;
    private AuthState state = AuthState.CONNECTING;
    private List<AbstractProcessor> processors = new ArrayList<>();

    // Getters
    public @Nullable Player getPlayer() {
        if (uuid == null) return null;
        return AuthPlugin.getInstance().getServer().getPlayer(uuid);
    };
    public AuthState getState() { return state; };
    public List<AbstractProcessor> getProcessors() { return processors; }

    // Methods
    public void setPlayer(Player player) {
        // Adding uuid
        this.uuid = player.getUniqueId();
    };

    public void authorize() {
        // Checking if everything needed is present
        if (this.uuid == null || this.address == null)
            throw new RuntimeException(String.format("Could not start Authorization process for player { uuid: %s, address: %s }", this.uuid, this.address));

        // Updating current state
        this.state = AuthState.AUTHORIZING;

        // Starting VoidProcessor for this connection
        // Starting AuthProcessor for this connection
        this.processors.add(
                new AuthProcessor(this)
                        .onState(ProcessorState.DONE, new OnStateCallback(this) {
                            @Override
                            public void call(Object ...args) {
                                // Getting connection from arguments
                                PlayerConnection connection = (PlayerConnection) args[0];
                                AuthProcessor.Response response = (AuthProcessor.Response) args[1];

                                // Stopping all processors
                                connection.processors.forEach(AbstractProcessor::stop);

                                // Updating connection state
                                connection.state = AuthState.LOGGED_IN;

                                AuthPlugin.getInstance().getLogger().log(Level.INFO, String.format("Logged in with token %s", response.token));

                                // Calling custom Login event
                                AuthPlugin.getInstance().getServer().getPluginManager().callEvent(new AuthorizedEvent(connection.getPlayer(), response.token));
                            };
                        })
        );
    };

    // AuthState enum
    public enum AuthState {
        CONNECTING,
        AUTHORIZING,
        LOGGED_IN,
        ERROR
    }
}
