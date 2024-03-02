/*
 * Decompiled with CFR 0.150.
 */
package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.EntityTypeMapping;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.ImmediateRespawn;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.BlockItemPackets1_15;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.EntityPackets1_15;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class Protocol1_14_4To1_15
extends BackwardsProtocol<ClientboundPackets1_15, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14> {
    public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.15", "1.14", Protocol1_15To1_14_4.class, true);
    private BlockItemPackets1_15 blockItemPackets;

    public Protocol1_14_4To1_15() {
        super(ClientboundPackets1_15.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }

    @Override
    protected void registerPackets() {
        this.executeAsyncAfterLoaded(Protocol1_15To1_14_4.class, MAPPINGS::load);
        TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
        translatableRewriter.registerBossBar(ClientboundPackets1_15.BOSSBAR);
        translatableRewriter.registerChatMessage(ClientboundPackets1_15.CHAT_MESSAGE);
        translatableRewriter.registerCombatEvent(ClientboundPackets1_15.COMBAT_EVENT);
        translatableRewriter.registerDisconnect(ClientboundPackets1_15.DISCONNECT);
        translatableRewriter.registerOpenWindow(ClientboundPackets1_15.OPEN_WINDOW);
        translatableRewriter.registerTabList(ClientboundPackets1_15.TAB_LIST);
        translatableRewriter.registerTitle(ClientboundPackets1_15.TITLE);
        translatableRewriter.registerPing();
        this.blockItemPackets = new BlockItemPackets1_15(this, translatableRewriter);
        this.blockItemPackets.register();
        new EntityPackets1_15(this).register();
        SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_15.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_15.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_15.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_15.STOP_SOUND);
        this.registerOutgoing(ClientboundPackets1_15.EXPLOSION, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.handler(wrapper -> {
                    PacketWrapper soundPacket = wrapper.create(81);
                    soundPacket.write(Type.VAR_INT, 243);
                    soundPacket.write(Type.VAR_INT, 4);
                    soundPacket.write(Type.INT, this.toEffectCoordinate(wrapper.get(Type.FLOAT, 0).floatValue()));
                    soundPacket.write(Type.INT, this.toEffectCoordinate(wrapper.get(Type.FLOAT, 1).floatValue()));
                    soundPacket.write(Type.INT, this.toEffectCoordinate(wrapper.get(Type.FLOAT, 2).floatValue()));
                    soundPacket.write(Type.FLOAT, Float.valueOf(4.0f));
                    soundPacket.write(Type.FLOAT, Float.valueOf(1.0f));
                    soundPacket.send(Protocol1_14_4To1_15.class);
                });
            }

            private int toEffectCoordinate(float coordinate) {
                return (int)(coordinate * 8.0f);
            }
        });
        new TagRewriter(this, EntityTypeMapping::getOldEntityId).register(ClientboundPackets1_15.TAGS);
        new StatisticsRewriter(this, EntityTypeMapping::getOldEntityId).register(ClientboundPackets1_15.STATISTICS);
    }

    @Override
    public void init(UserConnection user) {
        if (!user.has(ImmediateRespawn.class)) {
            user.put(new ImmediateRespawn(user));
        }
        if (!user.has(EntityTracker.class)) {
            user.put(new EntityTracker(user));
        }
        user.get(EntityTracker.class).initProtocol(this);
    }

    public BlockItemPackets1_15 getBlockItemPackets() {
        return this.blockItemPackets;
    }

    @Override
    public BackwardsMappings getMappingData() {
        return MAPPINGS;
    }
}

