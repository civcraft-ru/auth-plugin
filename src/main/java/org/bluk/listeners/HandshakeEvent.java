package org.bluk.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import org.bluk.AuthPlugin;
import org.bluk.entities.PlayerConnection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class HandshakeEvent extends PacketAdapter {
    public HandshakeEvent() {
        super(
                AuthPlugin.getInstance(),
                ListenerPriority.HIGHEST,
                Collections.singletonList(PacketType.Handshake.Client.SET_PROTOCOL)
        );
    };

    @Override
    public void onPacketReceiving(PacketEvent event) {
        // Getting packet information
        final PacketContainer packet = event.getPacket();

        if (packet.getProtocols().read(0) == PacketType.Protocol.LOGIN) {
            final InetAddress playerAddress = event.getPlayer().getAddress().getAddress();
            final String originAddress = packet.getStrings().read(0);

            // Creating new PlayerConnection for this player
            AuthPlugin.getPlayerConnectionStorage().createConnection(playerAddress, originAddress);
        }
    }
}
