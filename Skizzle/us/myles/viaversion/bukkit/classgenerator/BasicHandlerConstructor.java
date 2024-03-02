/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.ByteToMessageDecoder
 *  io.netty.handler.codec.MessageToByteEncoder
 */
package us.myles.ViaVersion.bukkit.classgenerator;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.bukkit.classgenerator.HandlerConstructor;
import us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler;
import us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler;

public class BasicHandlerConstructor
implements HandlerConstructor {
    @Override
    public BukkitEncodeHandler newEncodeHandler(UserConnection info, MessageToByteEncoder minecraftEncoder) {
        return new BukkitEncodeHandler(info, minecraftEncoder);
    }

    @Override
    public BukkitDecodeHandler newDecodeHandler(UserConnection info, ByteToMessageDecoder minecraftDecoder) {
        return new BukkitDecodeHandler(info, minecraftDecoder);
    }
}

