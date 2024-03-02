/*
 * Decompiled with CFR 0.150.
 */
package nl.matsv.viabackwards.api.rewriters;

import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.VBMappingDataLoader;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;

public class TranslatableRewriter
extends ComponentRewriter {
    private static final Map<String, Map<String, String>> TRANSLATABLES = new HashMap<String, Map<String, String>>();
    protected final Map<String, String> newTranslatables;

    public static void loadTranslatables() {
        JsonObject jsonObject = VBMappingDataLoader.loadData("translation-mappings.json");
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            HashMap<String, String> versionMappings = new HashMap<String, String>();
            TRANSLATABLES.put(entry.getKey(), versionMappings);
            for (Map.Entry<String, JsonElement> translationEntry : entry.getValue().getAsJsonObject().entrySet()) {
                versionMappings.put(translationEntry.getKey(), translationEntry.getValue().getAsString());
            }
        }
    }

    public TranslatableRewriter(BackwardsProtocol protocol) {
        this(protocol, protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
    }

    public TranslatableRewriter(BackwardsProtocol protocol, String sectionIdentifier) {
        super(protocol);
        Map<String, String> newTranslatables = TRANSLATABLES.get(sectionIdentifier);
        if (newTranslatables == null) {
            ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
            this.newTranslatables = new HashMap<String, String>();
        } else {
            this.newTranslatables = newTranslatables;
        }
    }

    public void registerPing() {
        this.protocol.registerOutgoing(State.LOGIN, 0, 0, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }

    public void registerDisconnect(ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }

    @Override
    public void registerChatMessage(ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }

    public void registerLegacyOpenWindow(ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }

    public void registerOpenWindow(ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }

    public void registerTabList(ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                    TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT));
                });
            }
        });
    }

    @Override
    protected void handleTranslate(JsonObject root, String translate) {
        String newTranslate = this.newTranslatables.get(translate);
        if (newTranslate != null) {
            root.addProperty("translate", newTranslate);
        }
    }
}

