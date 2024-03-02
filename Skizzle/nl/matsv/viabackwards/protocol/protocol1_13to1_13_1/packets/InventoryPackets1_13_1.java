/*
 * Decompiled with CFR 0.150.
 */
package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets;

import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;

public class InventoryPackets1_13_1 {
    public static void register(Protocol protocol) {
        ItemRewriter itemRewriter = new ItemRewriter(protocol, InventoryPackets1_13_1::toClient, InventoryPackets1_13_1::toServer);
        itemRewriter.registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        itemRewriter.registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        itemRewriter.registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        protocol.registerOutgoing(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        String channel = wrapper.passthrough(Type.STRING);
                        if (channel.equals("minecraft:trader_list")) {
                            wrapper.passthrough(Type.INT);
                            int size = wrapper.passthrough(Type.UNSIGNED_BYTE).shortValue();
                            for (int i = 0; i < size; ++i) {
                                Item input = wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.toClient(input);
                                Item output = wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.toClient(output);
                                boolean secondItem = wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    Item second = wrapper.passthrough(Type.FLAT_ITEM);
                                    InventoryPackets1_13_1.toClient(second);
                                }
                                wrapper.passthrough(Type.BOOLEAN);
                                wrapper.passthrough(Type.INT);
                                wrapper.passthrough(Type.INT);
                            }
                        }
                    }
                });
            }
        });
        itemRewriter.registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        itemRewriter.registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        itemRewriter.registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, Type.FLOAT);
    }

    public static void toClient(Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13To1_13_1.MAPPINGS.getNewItemId(item.getIdentifier()));
    }

    public static void toServer(Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13To1_13_1.MAPPINGS.getOldItemId(item.getIdentifier()));
    }
}

