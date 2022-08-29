package org.bluk;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketListener;
import org.bluk.entities.PlayerConnection;
import org.bluk.storages.PlayerConnectionStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bluk.api.APIServer;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;

public final class AuthPlugin extends JavaPlugin {

    private static AuthPlugin instance;
    private static FileConfiguration config;
    private static APIServer apiServer;
    private static PlayerConnectionStorage playerConnectionStorage = new PlayerConnectionStorage();

    public static AuthPlugin getInstance() { return instance; }
    public static FileConfiguration getConfiguration() { return config; }
    public static APIServer getApiServer() { return apiServer; }
    public static PlayerConnectionStorage getPlayerConnectionStorage() { return playerConnectionStorage; }

    public static void initReflections() {
        // Registering all spigot events
        Reflections reflection = new Reflections("org.bluk");

        Set<Class<? extends Listener>> eventsListenerTypes = reflection.getSubTypesOf(Listener.class);

        eventsListenerTypes.forEach(c -> {
            Listener l;
            try {
                l = c.getDeclaredConstructor().newInstance();
                getInstance().getServer().getPluginManager().registerEvents(l, getInstance());
                getInstance().getLogger().log(Level.INFO, String.format("Event [%s] was registered", c));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        // Registering all ProtocolLib events
        Set<Class<? extends PacketAdapter>> packetListenerTypes = reflection.getSubTypesOf(PacketAdapter.class);

        packetListenerTypes.forEach(c -> {
            PacketListener listener;
            try {
                listener = c.getDeclaredConstructor().newInstance();
                ProtocolLibrary.getProtocolManager().addPacketListener(listener);
                getInstance().getLogger().log(Level.INFO, String.format("PacketEvent [%s] was registered", c));
            } catch (Throwable e) {
                e.printStackTrace();
            };
        });
    };

    public static void initAPI() {
        // Initialize web server
        apiServer = new APIServer();
    };

    public static void initConfig() {
        // Initialize config file.
        getInstance().saveDefaultConfig();
    };

    @Override
    public void onEnable() {
        instance = this;

        // Initializing plugin
        initConfig();
        initAPI();
        initReflections();

        // Plugin is initialized
        getLogger().log(Level.INFO, "Plugin is loaded.");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin is disabled.");
    }
}
