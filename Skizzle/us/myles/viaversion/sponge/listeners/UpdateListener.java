/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.event.Listener
 *  org.spongepowered.api.event.network.ClientConnectionEvent$Join
 */
package us.myles.ViaVersion.sponge.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.update.UpdateUtil;

public class UpdateListener {
    @Listener
    public void onJoin(ClientConnectionEvent.Join join) {
        if (join.getTargetEntity().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(join.getTargetEntity().getUniqueId());
        }
    }
}

