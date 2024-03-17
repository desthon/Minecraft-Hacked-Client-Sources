/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.protocols.protocol1_9to1_8;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.MovementTracker;

public class ViaIdleThread
implements Runnable {
    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnections()) {
            long nextIdleUpdate;
            MovementTracker movementTracker;
            ProtocolInfo protocolInfo = info.getProtocolInfo();
            if (protocolInfo == null || !protocolInfo.getPipeline().contains(Protocol1_9To1_8.class) || (movementTracker = info.get(MovementTracker.class)) == null || (nextIdleUpdate = movementTracker.getNextIdlePacket()) > System.currentTimeMillis() || !info.getChannel().isOpen()) continue;
            Via.getManager().getProviders().get(MovementTransmitterProvider.class).sendPlayer(info);
        }
    }
}

