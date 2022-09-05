package org.bluk.auth.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import org.bluk.auth.AuthPlugin;

import java.net.InetAddress;
import java.util.Collections;

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
