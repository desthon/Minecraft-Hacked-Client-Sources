/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package us.myles.ViaVersion.bukkit.platform;

import io.netty.buffer.ByteBuf;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.ViaVersionPlugin;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.boss.ViaBossBar;
import us.myles.ViaVersion.bukkit.util.ProtocolSupportUtil;

public class BukkitViaAPI
implements ViaAPI<Player> {
    private final ViaVersionPlugin plugin;

    public BukkitViaAPI(ViaVersionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getPlayerVersion(Player player) {
        return this.getPlayerVersion(player.getUniqueId());
    }

    @Override
    public int getPlayerVersion(UUID uuid) {
        if (!this.isInjected(uuid)) {
            return this.getExternalVersion(Bukkit.getPlayer((UUID)uuid));
        }
        return Via.getManager().getConnection(uuid).getProtocolInfo().getProtocolVersion();
    }

    private int getExternalVersion(Player player) {
        if (!this.isProtocolSupport()) {
            return ProtocolRegistry.SERVER_PROTOCOL;
        }
        return ProtocolSupportUtil.getProtocolVersion(player);
    }

    @Override
    public boolean isInjected(UUID playerUUID) {
        return Via.getManager().isClientConnected(playerUUID);
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public void sendRawPacket(UUID uuid, ByteBuf packet) throws IllegalArgumentException {
        if (!this.isInjected(uuid)) {
            throw new IllegalArgumentException("This player is not controlled by ViaVersion!");
        }
        UserConnection ci = Via.getManager().getConnection(uuid);
        ci.sendRawPacket(packet);
    }

    @Override
    public void sendRawPacket(Player player, ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }

    @Override
    public BossBar<Player> createBossBar(String title, BossColor color, BossStyle style) {
        return new ViaBossBar(title, 1.0f, color, style);
    }

    @Override
    public BossBar<Player> createBossBar(String title, float health, BossColor color, BossStyle style) {
        return new ViaBossBar(title, health, color, style);
    }

    @Override
    public SortedSet<Integer> getSupportedVersions() {
        TreeSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }

    public boolean isCompatSpigotBuild() {
        return this.plugin.isCompatSpigotBuild();
    }

    public boolean isProtocolSupport() {
        return this.plugin.isProtocolSupport();
    }
}

