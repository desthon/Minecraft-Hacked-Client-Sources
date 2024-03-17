/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  io.netty.bootstrap.ServerBootstrap
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelException
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelOption
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.local.LocalAddress
 *  io.netty.channel.local.LocalEventLoopGroup
 *  io.netty.channel.local.LocalServerChannel
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.socket.nio.NioServerSocketChannel
 *  io.netty.handler.timeout.ReadTimeoutHandler
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PingResponseHandler;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
    private static final Logger logger = LogManager.getLogger();
    public static final LazyLoadBase eventLoops = new LazyLoadBase(){
        private static final String __OBFID = "CL_00001448";

        protected NioEventLoopGroup genericLoad() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
        }

        @Override
        protected Object load() {
            return this.genericLoad();
        }
    };
    public static final LazyLoadBase SERVER_LOCAL_EVENTLOOP = new LazyLoadBase(){
        private static final String __OBFID = "CL_00001449";

        protected LocalEventLoopGroup genericLoad() {
            return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
        }

        @Override
        protected Object load() {
            return this.genericLoad();
        }
    };
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;
    private final List endpoints = Collections.synchronizedList(Lists.newArrayList());
    private final List networkManagers = Collections.synchronizedList(Lists.newArrayList());
    private static final String __OBFID = "CL_00001447";

    public NetworkSystem(MinecraftServer server) {
        this.mcServer = server;
        this.isAlive = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addLanEndpoint(InetAddress address, int port) throws IOException {
        List var3 = this.endpoints;
        List list = this.endpoints;
        synchronized (list) {
            this.endpoints.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(NioServerSocketChannel.class)).childHandler((ChannelHandler)new ChannelInitializer(){
                private static final String __OBFID = "CL_00001450";

                protected void initChannel(Channel p_initChannel_1_) {
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, (Object)24);
                    }
                    catch (ChannelException channelException) {
                        // empty catch block
                    }
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)false);
                    }
                    catch (ChannelException channelException) {
                        // empty catch block
                    }
                    p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
                    NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)var2);
                    var2.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, var2));
                }
            }).group((EventLoopGroup)eventLoops.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SocketAddress addLocalEndpoint() {
        ChannelFuture var1;
        List var2 = this.endpoints;
        List list = this.endpoints;
        synchronized (list) {
            var1 = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer(){
                private static final String __OBFID = "CL_00001451";

                protected void initChannel(Channel p_initChannel_1_) {
                    NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    var2.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, var2));
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)var2);
                }
            }).group((EventLoopGroup)eventLoops.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add(var1);
        }
        return var1.channel().localAddress();
    }

    public void terminateEndpoints() {
        this.isAlive = false;
        for (ChannelFuture var2 : this.endpoints) {
            try {
                var2.channel().close().sync();
            }
            catch (InterruptedException var4) {
                logger.error("Interrupted whilst closing channel");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void networkTick() {
        List var1 = this.networkManagers;
        List list = this.networkManagers;
        synchronized (list) {
            Iterator var2 = this.networkManagers.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = (NetworkManager)((Object)var2.next());
                if (var3.hasNoChannel()) continue;
                if (!var3.isChannelOpen()) {
                    var2.remove();
                    var3.checkDisconnected();
                    continue;
                }
                try {
                    var3.processReceivedPackets();
                }
                catch (Exception var8) {
                    if (var3.isLocalChannel()) {
                        CrashReport var10 = CrashReport.makeCrashReport(var8, "Ticking memory connection");
                        CrashReportCategory var6 = var10.makeCategory("Ticking connection");
                        var6.addCrashSectionCallable("Connection", new Callable(){
                            private static final String __OBFID = "CL_00002272";

                            public String func_180229_a() {
                                return ((Object)((Object)var3)).toString();
                            }

                            public Object call() {
                                return this.func_180229_a();
                            }
                        });
                        throw new ReportedException(var10);
                    }
                    logger.warn("Failed to handle packet for " + var3.getRemoteAddress(), (Throwable)var8);
                    final ChatComponentText var5 = new ChatComponentText("Internal server error");
                    var3.sendPacket(new S40PacketDisconnect(var5), new GenericFutureListener(){
                        private static final String __OBFID = "CL_00002271";

                        public void operationComplete(Future p_operationComplete_1_) {
                            var3.closeChannel(var5);
                        }
                    }, new GenericFutureListener[0]);
                    var3.disableAutoRead();
                }
            }
        }
    }

    public MinecraftServer getServer() {
        return this.mcServer;
    }
}

