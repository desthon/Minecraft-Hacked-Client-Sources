/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import com.google.common.collect.Sets;
import java.util.Set;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.entities.Entity1_16_2Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.EntityPackets;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;

public class EntityPackets1_16_2
extends EntityRewriter<Protocol1_16_1To1_16_2> {
    private final Set<String> oldDimensions = Sets.newHashSet((Object[])new String[]{"minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"});

    public EntityPackets1_16_2(Protocol1_16_1To1_16_2 protocol) {
        super(protocol);
    }

    @Override
    protected void registerPackets() {
        this.registerSpawnTrackerWithData(ClientboundPackets1_16_2.SPAWN_ENTITY, Entity1_16_2Types.EntityType.FALLING_BLOCK);
        this.registerSpawnTracker(ClientboundPackets1_16_2.SPAWN_MOB);
        this.registerExtraTracker(ClientboundPackets1_16_2.SPAWN_EXPERIENCE_ORB, Entity1_16_2Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker(ClientboundPackets1_16_2.SPAWN_PAINTING, Entity1_16_2Types.EntityType.PAINTING);
        this.registerExtraTracker(ClientboundPackets1_16_2.SPAWN_PLAYER, Entity1_16_2Types.EntityType.PLAYER);
        this.registerEntityDestroy(ClientboundPackets1_16_2.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_16_2.ENTITY_METADATA, Types1_14.METADATA_LIST);
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing(ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(wrapper -> {
                    boolean hardcore = wrapper.read(Type.BOOLEAN);
                    short gamemode = wrapper.read(Type.UNSIGNED_BYTE);
                    if (hardcore) {
                        gamemode = (short)(gamemode | 8);
                    }
                    wrapper.write(Type.UNSIGNED_BYTE, gamemode);
                });
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.handler(wrapper -> {
                    wrapper.read(Type.NBT);
                    wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG);
                    CompoundTag dimensionData = wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.handler(wrapper -> {
                    int maxPlayers = wrapper.read(Type.VAR_INT);
                    wrapper.write(Type.UNSIGNED_BYTE, (short)Math.max(maxPlayers, 255));
                });
                this.handler(EntityPackets1_16_2.this.getTrackerHandler(Entity1_16_2Types.EntityType.PLAYER, Type.INT));
            }
        });
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing(ClientboundPackets1_16_2.RESPAWN, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    CompoundTag dimensionData = wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
            }
        });
    }

    private String getDimensionFromData(CompoundTag dimensionData) {
        StringTag effectsLocation = (StringTag)dimensionData.get("effects");
        return effectsLocation != null && this.oldDimensions.contains(effectsLocation.getValue()) ? effectsLocation.getValue() : "minecraft:overworld";
    }

    @Override
    protected void registerRewrites() {
        this.registerMetaHandler().handle(e -> {
            Metadata meta = e.getData();
            MetaType type = meta.getMetaType();
            if (type == MetaType1_14.Slot) {
                meta.setValue(((Protocol1_16_1To1_16_2)this.protocol).getBlockItemPackets().handleItemToClient((Item)meta.getValue()));
            } else if (type == MetaType1_14.BlockID) {
                meta.setValue(((Protocol1_16_1To1_16_2)this.protocol).getMappingData().getNewBlockStateId((Integer)meta.getValue()));
            } else if (type == MetaType1_14.OptChat) {
                JsonElement text = (JsonElement)meta.getCastedValue();
                if (text != null) {
                    ((Protocol1_16_1To1_16_2)this.protocol).getTranslatableRewriter().processText(text);
                }
            } else if (type == MetaType1_14.PARTICLE) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            return meta;
        });
        this.mapTypes(Entity1_16_2Types.EntityType.values(), Entity1_16Types.EntityType.class);
        this.mapEntity(Entity1_16_2Types.EntityType.PIGLIN_BRUTE, Entity1_16_2Types.EntityType.PIGLIN).jsonName("Piglin Brute");
        this.registerMetaHandler().filter((EntityType)Entity1_16_2Types.EntityType.ABSTRACT_PIGLIN, true).handle(meta -> {
            if (meta.getIndex() == 15) {
                meta.getData().setId(16);
            } else if (meta.getIndex() == 16) {
                meta.getData().setId(15);
            }
            return meta.getData();
        });
    }

    @Override
    protected EntityType getTypeFromId(int typeId) {
        return Entity1_16_2Types.getTypeFromId(typeId);
    }
}

