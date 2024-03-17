/*
 * Decompiled with CFR 0.150.
 */
package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16;

import java.util.UUID;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat.TranslatableRewriter1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data.BackwardsMappings;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.BlockItemPackets1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.EntityPackets1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.storage.PlayerSneakStorage;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;

public class Protocol1_15_2To1_16
extends BackwardsProtocol<ClientboundPackets1_16, ClientboundPackets1_15, ServerboundPackets1_16, ServerboundPackets1_14> {
    public static final BackwardsMappings MAPPINGS = new BackwardsMappings();
    private BlockItemPackets1_16 blockItemPackets;
    private TranslatableRewriter translatableRewriter;

    public Protocol1_15_2To1_16() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_15.class, ServerboundPackets1_16.class, ServerboundPackets1_14.class);
    }

    @Override
    protected void registerPackets() {
        this.executeAsyncAfterLoaded(Protocol1_16To1_15_2.class, MAPPINGS::load);
        this.translatableRewriter = new TranslatableRewriter1_16(this);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_16.BOSSBAR);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_16.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_16.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_16.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_16.TITLE);
        this.translatableRewriter.registerPing();
        this.blockItemPackets = new BlockItemPackets1_16(this, this.translatableRewriter);
        this.blockItemPackets.register();
        EntityPackets1_16 entityPackets = new EntityPackets1_16(this);
        entityPackets.register();
        this.registerOutgoing(State.STATUS, 0, 0, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    String original = wrapper.passthrough(Type.STRING);
                    JsonObject object = GsonUtil.getGson().fromJson(original, JsonObject.class);
                    JsonElement description = object.get("description");
                    if (description == null) {
                        return;
                    }
                    Protocol1_15_2To1_16.this.translatableRewriter.processText(description);
                    wrapper.set(Type.STRING, 0, object.toString());
                });
            }
        });
        this.registerOutgoing(ClientboundPackets1_16.CHAT_MESSAGE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                this.map(Type.BYTE);
                this.map(Type.UUID, Type.NOTHING);
            }
        });
        this.registerOutgoing(ClientboundPackets1_16.OPEN_WINDOW, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                this.handler(wrapper -> {
                    int windowType = wrapper.get(Type.VAR_INT, 1);
                    if (windowType == 20) {
                        wrapper.set(Type.VAR_INT, 1, 7);
                    } else if (windowType > 20) {
                        wrapper.set(Type.VAR_INT, 1, --windowType);
                    }
                });
            }
        });
        SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_16.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_16.STOP_SOUND);
        this.registerOutgoing(State.LOGIN, 2, 2, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    UUID uuid = wrapper.read(Type.UUID_INT_ARRAY);
                    wrapper.write(Type.STRING, uuid.toString());
                });
            }
        });
        new TagRewriter(this, entityPackets::getOldEntityId).register(ClientboundPackets1_16.TAGS);
        new StatisticsRewriter(this, entityPackets::getOldEntityId).register(ClientboundPackets1_16.STATISTICS);
        this.registerIncoming(ServerboundPackets1_14.ENTITY_ACTION, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.VAR_INT);
                    int action = wrapper.passthrough(Type.VAR_INT);
                    if (action == 0) {
                        wrapper.user().get(PlayerSneakStorage.class).setSneaking(true);
                    } else if (action == 1) {
                        wrapper.user().get(PlayerSneakStorage.class).setSneaking(false);
                    }
                });
            }
        });
        this.registerIncoming(ServerboundPackets1_14.INTERACT_ENTITY, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.VAR_INT);
                    int action = wrapper.passthrough(Type.VAR_INT);
                    if (action == 0 || action == 2) {
                        if (action == 2) {
                            wrapper.passthrough(Type.FLOAT);
                            wrapper.passthrough(Type.FLOAT);
                            wrapper.passthrough(Type.FLOAT);
                        }
                        wrapper.passthrough(Type.VAR_INT);
                    }
                    wrapper.write(Type.BOOLEAN, wrapper.user().get(PlayerSneakStorage.class).isSneaking());
                });
            }
        });
        this.registerIncoming(ServerboundPackets1_14.PLAYER_ABILITIES, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    byte flags = wrapper.read(Type.BYTE);
                    flags = (byte)(flags & 2);
                    wrapper.write(Type.BYTE, flags);
                    wrapper.read(Type.FLOAT);
                    wrapper.read(Type.FLOAT);
                });
            }
        });
        this.cancelIncoming(ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }

    @Override
    public void init(UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        if (!user.has(EntityTracker.class)) {
            user.put(new EntityTracker(user));
        }
        user.put(new PlayerSneakStorage(user));
        user.get(EntityTracker.class).initProtocol(this);
    }

    public BlockItemPackets1_16 getBlockItemPackets() {
        return this.blockItemPackets;
    }

    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }

    @Override
    public BackwardsMappings getMappingData() {
        return MAPPINGS;
    }
}

