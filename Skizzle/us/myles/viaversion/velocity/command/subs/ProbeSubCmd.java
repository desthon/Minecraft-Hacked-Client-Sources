/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.velocity.command.subs;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;
import us.myles.ViaVersion.velocity.platform.VelocityViaConfig;
import us.myles.ViaVersion.velocity.service.ProtocolDetectorService;

public class ProbeSubCmd
extends ViaSubCommand {
    @Override
    public String name() {
        return "probe";
    }

    @Override
    public String description() {
        return "Forces ViaVersion to scan server protocol versions " + (((VelocityViaConfig)Via.getConfig()).getVelocityPingInterval() == -1 ? "" : "(Also happens at an interval)");
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        ProtocolDetectorService.getInstance().run();
        this.sendMessage(sender, "&6Started searching for protocol versions", new Object[0]);
        return true;
    }
}

