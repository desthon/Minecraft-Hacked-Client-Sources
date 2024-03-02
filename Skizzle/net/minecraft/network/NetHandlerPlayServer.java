/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.Futures
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer
implements INetHandlerPlayServer,
IUpdatePlayerListBox {
    private static final Logger logger = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private int field_175090_f;
    private int floatingTickCount;
    private boolean field_147366_g;
    private int field_147378_h;
    private long lastPingTime;
    private long lastSentPingPacket;
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private IntHashMap field_147372_n = new IntHashMap();
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean hasMoved = true;
    private static final String __OBFID = "CL_00001452";

    public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn) {
        this.serverController = server;
        this.netManager = networkManagerIn;
        networkManagerIn.setNetHandler(this);
        this.playerEntity = playerIn;
        playerIn.playerNetServerHandler = this;
    }

    @Override
    public void update() {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");
        if ((long)this.networkTickCount - this.lastSentPingPacket > 40L) {
            this.lastSentPingPacket = this.networkTickCount;
            this.lastPingTime = this.currentTimeMillis();
            this.field_147378_h = (int)this.lastPingTime;
            this.sendPacket(new S00PacketKeepAlive(this.field_147378_h));
        }
        this.serverController.theProfiler.endSection();
        if (this.chatSpamThresholdCount > 0) {
            --this.chatSpamThresholdCount;
        }
        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > (long)(this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60)) {
            this.kickPlayerFromServer("You have been idle for too long!");
        }
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    public void kickPlayerFromServer(String reason) {
        final ChatComponentText var2 = new ChatComponentText(reason);
        this.netManager.sendPacket(new S40PacketDisconnect(var2), new GenericFutureListener(){
            private static final String __OBFID = "CL_00001453";

            public void operationComplete(io.netty.util.concurrent.Future p_operationComplete_1_) {
                NetHandlerPlayServer.this.netManager.closeChannel(var2);
            }
        }, new GenericFutureListener[0]);
        this.netManager.disableAutoRead();
        Futures.getUnchecked((Future)this.serverController.addScheduledTask(new Runnable(){
            private static final String __OBFID = "CL_00001454";

            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        }));
    }

    @Override
    public void processInput(C0CPacketInput packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
    }

    @Override
    public void processPlayer(C03PacketPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        this.field_147366_g = true;
        if (!this.playerEntity.playerConqueredTheEnd) {
            double var3 = this.playerEntity.posX;
            double var5 = this.playerEntity.posY;
            double var7 = this.playerEntity.posZ;
            double var9 = 0.0;
            double var11 = packetIn.getPositionX() - this.lastPosX;
            double var13 = packetIn.getPositionY() - this.lastPosY;
            double var15 = packetIn.getPositionZ() - this.lastPosZ;
            if (packetIn.func_149466_j()) {
                var9 = var11 * var11 + var13 * var13 + var15 * var15;
                if (!this.hasMoved && var9 < 0.25) {
                    this.hasMoved = true;
                }
            }
            if (this.hasMoved) {
                double var37;
                double var35;
                this.field_175090_f = this.networkTickCount;
                if (this.playerEntity.ridingEntity != null) {
                    float var47 = this.playerEntity.rotationYaw;
                    float var18 = this.playerEntity.rotationPitch;
                    this.playerEntity.ridingEntity.updateRiderPosition();
                    double var19 = this.playerEntity.posX;
                    double var21 = this.playerEntity.posY;
                    double var23 = this.playerEntity.posZ;
                    if (packetIn.getRotating()) {
                        var47 = packetIn.getYaw();
                        var18 = packetIn.getPitch();
                    }
                    this.playerEntity.onGround = packetIn.func_149465_i();
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(var19, var21, var23, var47, var18);
                    if (this.playerEntity.ridingEntity != null) {
                        this.playerEntity.ridingEntity.updateRiderPosition();
                    }
                    this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                    if (this.playerEntity.ridingEntity != null) {
                        if (var9 > 4.0) {
                            Entity var48 = this.playerEntity.ridingEntity;
                            this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(var48));
                            this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        }
                        this.playerEntity.ridingEntity.isAirBorne = true;
                    }
                    if (this.hasMoved) {
                        this.lastPosX = this.playerEntity.posX;
                        this.lastPosY = this.playerEntity.posY;
                        this.lastPosZ = this.playerEntity.posZ;
                    }
                    var2.updateEntity(this.playerEntity);
                    return;
                }
                if (this.playerEntity.isPlayerSleeping()) {
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    var2.updateEntity(this.playerEntity);
                    return;
                }
                double var17 = this.playerEntity.posY;
                this.lastPosX = this.playerEntity.posX;
                this.lastPosY = this.playerEntity.posY;
                this.lastPosZ = this.playerEntity.posZ;
                double var19 = this.playerEntity.posX;
                double var21 = this.playerEntity.posY;
                double var23 = this.playerEntity.posZ;
                float var25 = this.playerEntity.rotationYaw;
                float var26 = this.playerEntity.rotationPitch;
                if (packetIn.func_149466_j() && packetIn.getPositionY() == -999.0) {
                    packetIn.func_149469_a(false);
                }
                if (packetIn.func_149466_j()) {
                    var19 = packetIn.getPositionX();
                    var21 = packetIn.getPositionY();
                    var23 = packetIn.getPositionZ();
                    if (Math.abs(packetIn.getPositionX()) > 3.0E7 || Math.abs(packetIn.getPositionZ()) > 3.0E7) {
                        this.kickPlayerFromServer("Illegal position");
                        return;
                    }
                }
                if (packetIn.getRotating()) {
                    var25 = packetIn.getYaw();
                    var26 = packetIn.getPitch();
                }
                this.playerEntity.onUpdateEntity();
                this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var25, var26);
                if (!this.hasMoved) {
                    return;
                }
                double var27 = var19 - this.playerEntity.posX;
                double var29 = var21 - this.playerEntity.posY;
                double var31 = var23 - this.playerEntity.posZ;
                double var33 = Math.min(Math.abs(var27), Math.abs(this.playerEntity.motionX));
                double var39 = var33 * var33 + (var35 = Math.min(Math.abs(var29), Math.abs(this.playerEntity.motionY))) * var35 + (var37 = Math.min(Math.abs(var31), Math.abs(this.playerEntity.motionZ))) * var37;
                if (!(!(var39 > 100.0) || this.serverController.isSinglePlayer() && this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                    logger.warn(String.valueOf(this.playerEntity.getName()) + " moved too quickly! " + var27 + "," + var29 + "," + var31 + " (" + var33 + ", " + var35 + ", " + var37 + ")");
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    return;
                }
                float var41 = 0.0625f;
                boolean var42 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(var41, var41, var41)).isEmpty();
                if (this.playerEntity.onGround && !packetIn.func_149465_i() && var29 > 0.0) {
                    this.playerEntity.jump();
                }
                this.playerEntity.moveEntity(var27, var29, var31);
                this.playerEntity.onGround = packetIn.func_149465_i();
                double var43 = var29;
                var27 = var19 - this.playerEntity.posX;
                var29 = var21 - this.playerEntity.posY;
                if (var29 > -0.5 || var29 < 0.5) {
                    var29 = 0.0;
                }
                var31 = var23 - this.playerEntity.posZ;
                var39 = var27 * var27 + var29 * var29 + var31 * var31;
                boolean var45 = false;
                if (var39 > 0.0625 && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
                    var45 = true;
                    logger.warn(String.valueOf(this.playerEntity.getName()) + " moved wrongly!");
                }
                this.playerEntity.setPositionAndRotation(var19, var21, var23, var25, var26);
                this.playerEntity.addMovementStat(this.playerEntity.posX - var3, this.playerEntity.posY - var5, this.playerEntity.posZ - var7);
                if (!this.playerEntity.noClip) {
                    boolean var46 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(var41, var41, var41)).isEmpty();
                    if (var42 && (var45 || !var46) && !this.playerEntity.isPlayerSleeping()) {
                        this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, var25, var26);
                        return;
                    }
                }
                AxisAlignedBB var49 = this.playerEntity.getEntityBoundingBox().expand(var41, var41, var41).addCoord(0.0, -0.55, 0.0);
                if (!(this.serverController.isFlightAllowed() || this.playerEntity.capabilities.allowFlying || var2.checkBlockCollision(var49))) {
                    if (var43 >= -0.03125) {
                        ++this.floatingTickCount;
                        if (this.floatingTickCount > 80) {
                            return;
                        }
                    }
                } else {
                    this.floatingTickCount = 0;
                }
                this.playerEntity.onGround = packetIn.func_149465_i();
                this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                this.playerEntity.handleFalling(this.playerEntity.posY - var17, packetIn.func_149465_i());
            } else if (this.networkTickCount - this.field_175090_f > 20) {
                this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            }
        }
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {
        this.func_175089_a(x, y, z, yaw, pitch, Collections.emptySet());
    }

    public void func_175089_a(double p_175089_1_, double p_175089_3_, double p_175089_5_, float p_175089_7_, float p_175089_8_, Set p_175089_9_) {
        this.hasMoved = false;
        this.lastPosX = p_175089_1_;
        this.lastPosY = p_175089_3_;
        this.lastPosZ = p_175089_5_;
        if (p_175089_9_.contains((Object)S08PacketPlayerPosLook.EnumFlags.X)) {
            this.lastPosX += this.playerEntity.posX;
        }
        if (p_175089_9_.contains((Object)S08PacketPlayerPosLook.EnumFlags.Y)) {
            this.lastPosY += this.playerEntity.posY;
        }
        if (p_175089_9_.contains((Object)S08PacketPlayerPosLook.EnumFlags.Z)) {
            this.lastPosZ += this.playerEntity.posZ;
        }
        float var10 = p_175089_7_;
        float var11 = p_175089_8_;
        if (p_175089_9_.contains((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            var10 = p_175089_7_ + this.playerEntity.rotationYaw;
        }
        if (p_175089_9_.contains((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            var11 = p_175089_8_ + this.playerEntity.rotationPitch;
        }
        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var10, var11);
        this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(p_175089_1_, p_175089_3_, p_175089_5_, p_175089_7_, p_175089_8_, p_175089_9_));
    }

    @Override
    public void processPlayerDigging(C07PacketPlayerDigging packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        BlockPos var3 = packetIn.func_179715_a();
        this.playerEntity.markPlayerActive();
        switch (packetIn.func_180762_c()) {
            case DROP_ITEM: {
                if (!this.playerEntity.func_175149_v()) {
                    this.playerEntity.dropOneItem(false);
                }
                return;
            }
            case DROP_ALL_ITEMS: {
                if (!this.playerEntity.func_175149_v()) {
                    this.playerEntity.dropOneItem(true);
                }
                return;
            }
            case RELEASE_USE_ITEM: {
                this.playerEntity.stopUsingItem();
                return;
            }
            case START_DESTROY_BLOCK: 
            case ABORT_DESTROY_BLOCK: 
            case STOP_DESTROY_BLOCK: {
                double var4 = this.playerEntity.posX - ((double)var3.getX() + 0.5);
                double var6 = this.playerEntity.posY - ((double)var3.getY() + 0.5) + 1.5;
                double var8 = this.playerEntity.posZ - ((double)var3.getZ() + 0.5);
                double var10 = var4 * var4 + var6 * var6 + var8 * var8;
                if (var10 > 36.0) {
                    return;
                }
                if (var3.getY() >= this.serverController.getBuildLimit()) {
                    return;
                }
                if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(var2, var3, this.playerEntity) && var2.getWorldBorder().contains(var3)) {
                        this.playerEntity.theItemInWorldManager.func_180784_a(var3, packetIn.func_179714_b());
                    } else {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
                    }
                } else {
                    if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.func_180785_a(var3);
                    } else if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.func_180238_e();
                    }
                    if (var2.getBlockState(var3).getBlock().getMaterial() != Material.air) {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
                    }
                }
                return;
            }
        }
        throw new IllegalArgumentException("Invalid player action");
    }

    @Override
    public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        ItemStack var3 = this.playerEntity.inventory.getCurrentItem();
        boolean var4 = false;
        BlockPos var5 = packetIn.func_179724_a();
        EnumFacing var6 = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
        this.playerEntity.markPlayerActive();
        if (packetIn.getPlacedBlockDirection() == 255) {
            if (var3 == null) {
                return;
            }
            this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, var2, var3);
        } else if (var5.getY() >= this.serverController.getBuildLimit() - 1 && (var6 == EnumFacing.UP || var5.getY() >= this.serverController.getBuildLimit())) {
            ChatComponentTranslation var7 = new ChatComponentTranslation("build.tooHigh", this.serverController.getBuildLimit());
            var7.getChatStyle().setColor(EnumChatFormatting.RED);
            this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(var7));
            var4 = true;
        } else {
            if (this.hasMoved && this.playerEntity.getDistanceSq((double)var5.getX() + 0.5, (double)var5.getY() + 0.5, (double)var5.getZ() + 0.5) < 64.0 && !this.serverController.isBlockProtected(var2, var5, this.playerEntity) && var2.getWorldBorder().contains(var5)) {
                this.playerEntity.theItemInWorldManager.func_180236_a(this.playerEntity, var2, var3, var5, var6, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
            }
            var4 = true;
        }
        if (var4) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5));
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5.offset(var6)));
        }
        if ((var3 = this.playerEntity.inventory.getCurrentItem()) != null && var3.stackSize == 0) {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            var3 = null;
        }
        if (var3 == null || var3.getMaxItemUseDuration() == 0) {
            this.playerEntity.isChangingQuantityOnly = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            Slot var8 = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.isChangingQuantityOnly = false;
            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack())) {
                this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, var8.slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }

    @Override
    public void func_175088_a(C18PacketSpectate p_175088_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_175088_1_, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.func_175149_v()) {
            Entity var2 = null;
            for (WorldServer var6 : this.serverController.worldServers) {
                if (var6 != null && (var2 = p_175088_1_.func_179727_a(var6)) != null) break;
            }
            if (var2 != null) {
                this.playerEntity.func_175399_e(this.playerEntity);
                this.playerEntity.mountEntity(null);
                if (var2.worldObj != this.playerEntity.worldObj) {
                    WorldServer var7 = this.playerEntity.getServerForPlayer();
                    WorldServer var8 = (WorldServer)var2.worldObj;
                    this.playerEntity.dimension = var2.dimension;
                    this.sendPacket(new S07PacketRespawn(this.playerEntity.dimension, var7.getDifficulty(), var7.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
                    var7.removePlayerEntityDangerously(this.playerEntity);
                    this.playerEntity.isDead = false;
                    this.playerEntity.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
                    if (this.playerEntity.isEntityAlive()) {
                        var7.updateEntityWithOptionalForce(this.playerEntity, false);
                        var8.spawnEntityInWorld(this.playerEntity);
                        var8.updateEntityWithOptionalForce(this.playerEntity, false);
                    }
                    this.playerEntity.setWorld(var8);
                    this.serverController.getConfigurationManager().func_72375_a(this.playerEntity, var7);
                    this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
                    this.playerEntity.theItemInWorldManager.setWorld(var8);
                    this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, var8);
                    this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
                } else {
                    this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
                }
            }
        }
    }

    @Override
    public void func_175086_a(C19PacketResourcePackStatus p_175086_1_) {
    }

    @Override
    public void onDisconnect(IChatComponent reason) {
        logger.info(String.valueOf(this.playerEntity.getName()) + " lost connection: " + reason);
        this.serverController.refreshStatusNextTick();
        ChatComponentTranslation var2 = new ChatComponentTranslation("multiplayer.player.left", this.playerEntity.getDisplayName());
        var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.serverController.getConfigurationManager().sendChatMsg(var2);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
        if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }

    public void sendPacket(final Packet packetIn) {
        if (packetIn instanceof S02PacketChat) {
            S02PacketChat var2 = (S02PacketChat)packetIn;
            EntityPlayer.EnumChatVisibility var3 = this.playerEntity.getChatVisibility();
            if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN) {
                return;
            }
            if (var3 == EntityPlayer.EnumChatVisibility.SYSTEM && !var2.isChat()) {
                return;
            }
        }
        try {
            this.netManager.sendPacket(packetIn);
        }
        catch (Throwable var5) {
            CrashReport var6 = CrashReport.makeCrashReport(var5, "Sending packet");
            CrashReportCategory var4 = var6.makeCategory("Packet being sent");
            var4.addCrashSectionCallable("Packet class", new Callable(){
                private static final String __OBFID = "CL_00002270";

                public String func_180225_a() {
                    return packetIn.getClass().getCanonicalName();
                }

                public Object call() {
                    return this.func_180225_a();
                }
            });
            throw new ReportedException(var6);
        }
    }

    @Override
    public void processHeldItemChange(C09PacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize()) {
            this.playerEntity.inventory.currentItem = packetIn.getSlotId();
            this.playerEntity.markPlayerActive();
        } else {
            logger.warn(String.valueOf(this.playerEntity.getName()) + " tried to set an invalid carried item");
        }
    }

    @Override
    public void processChatMessage(C01PacketChatMessage packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            ChatComponentTranslation var4 = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            var4.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(var4));
        } else {
            this.playerEntity.markPlayerActive();
            String var2 = packetIn.getMessage();
            var2 = StringUtils.normalizeSpace((String)var2);
            for (int var3 = 0; var3 < var2.length(); ++var3) {
                if (ChatAllowedCharacters.isAllowedCharacter(var2.charAt(var3))) continue;
                this.kickPlayerFromServer("Illegal characters in chat");
                return;
            }
            if (var2.startsWith("/")) {
                this.handleSlashCommand(var2);
            } else {
                ChatComponentTranslation var5 = new ChatComponentTranslation("chat.type.text", this.playerEntity.getDisplayName(), var2);
                this.serverController.getConfigurationManager().sendChatMsgImpl(var5, false);
            }
            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())) {
                this.kickPlayerFromServer("disconnect.spam");
            }
        }
    }

    private void handleSlashCommand(String command) {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
    }

    @Override
    public void func_175087_a(C0APacketAnimation p_175087_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_175087_1_, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        this.playerEntity.swingItem();
    }

    @Override
    public void processEntityAction(C0BPacketEntityAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        switch (packetIn.func_180764_b()) {
            case START_SNEAKING: {
                this.playerEntity.setSneaking(true);
                break;
            }
            case STOP_SNEAKING: {
                this.playerEntity.setSneaking(false);
                break;
            }
            case START_SPRINTING: {
                this.playerEntity.setSprinting(true);
                break;
            }
            case STOP_SPRINTING: {
                this.playerEntity.setSprinting(false);
                break;
            }
            case STOP_SLEEPING: {
                this.playerEntity.wakeUpPlayer(false, true, true);
                this.hasMoved = false;
                break;
            }
            case RIDING_JUMP: {
                if (!(this.playerEntity.ridingEntity instanceof EntityHorse)) break;
                ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.func_149512_e());
                break;
            }
            case OPEN_INVENTORY: {
                if (!(this.playerEntity.ridingEntity instanceof EntityHorse)) break;
                ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }

    @Override
    public void processUseEntity(C02PacketUseEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        Entity var3 = packetIn.getEntityFromWorld(var2);
        this.playerEntity.markPlayerActive();
        if (var3 != null) {
            boolean var4 = this.playerEntity.canEntityBeSeen(var3);
            double var5 = 36.0;
            if (!var4) {
                var5 = 9.0;
            }
            if (this.playerEntity.getDistanceSqToEntity(var3) < var5) {
                if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    this.playerEntity.interactWith(var3);
                } else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
                    var3.func_174825_a(this.playerEntity, packetIn.func_179712_b());
                } else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (var3 instanceof EntityItem || var3 instanceof EntityXPOrb || var3 instanceof EntityArrow || var3 == this.playerEntity) {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }
                    this.playerEntity.attackTargetEntityWithCurrentItem(var3);
                }
            }
        }
    }

    @Override
    public void processClientStatus(C16PacketClientStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        C16PacketClientStatus.EnumState var2 = packetIn.getStatus();
        switch (var2) {
            case PERFORM_RESPAWN: {
                if (this.playerEntity.playerConqueredTheEnd) {
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
                    break;
                }
                if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
                    if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
                        this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                        this.serverController.deleteWorldAndStopServer();
                        break;
                    }
                    UserListBansEntry var3 = new UserListBansEntry(this.playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
                    this.serverController.getConfigurationManager().getBannedPlayers().addEntry(var3);
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                    break;
                }
                if (this.playerEntity.getHealth() > 0.0f) {
                    return;
                }
                this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
                break;
            }
            case REQUEST_STATS: {
                this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
                break;
            }
            case OPEN_INVENTORY_ACHIEVEMENT: {
                this.playerEntity.triggerAchievement(AchievementList.openInventory);
            }
        }
    }

    @Override
    public void processCloseWindow(C0DPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.closeContainer();
    }

    @Override
    public void processClickWindow(C0EPacketClickWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            if (this.playerEntity.func_175149_v()) {
                ArrayList var2 = Lists.newArrayList();
                for (int var3 = 0; var3 < this.playerEntity.openContainer.inventorySlots.size(); ++var3) {
                    var2.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var3)).getStack());
                }
                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var2);
            } else {
                ItemStack var5 = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), this.playerEntity);
                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), var5)) {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.playerEntity.isChangingQuantityOnly = true;
                    this.playerEntity.openContainer.detectAndSendChanges();
                    this.playerEntity.updateHeldItem();
                    this.playerEntity.isChangingQuantityOnly = false;
                } else {
                    this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, packetIn.getActionNumber());
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                    ArrayList var6 = Lists.newArrayList();
                    for (int var4 = 0; var4 < this.playerEntity.openContainer.inventorySlots.size(); ++var4) {
                        var6.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var4)).getStack());
                    }
                    this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var6);
                }
            }
        }
    }

    @Override
    public void processEnchantItem(C11PacketEnchantItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.func_175149_v()) {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }

    @Override
    public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.theItemInWorldManager.isCreative()) {
            boolean var10;
            BlockPos var5;
            TileEntity var6;
            NBTTagCompound var4;
            boolean var2 = packetIn.getSlotId() < 0;
            ItemStack var3 = packetIn.getStack();
            if (var3 != null && var3.hasTagCompound() && var3.getTagCompound().hasKey("BlockEntityTag", 10) && (var4 = var3.getTagCompound().getCompoundTag("BlockEntityTag")).hasKey("x") && var4.hasKey("y") && var4.hasKey("z") && (var6 = this.playerEntity.worldObj.getTileEntity(var5 = new BlockPos(var4.getInteger("x"), var4.getInteger("y"), var4.getInteger("z")))) != null) {
                NBTTagCompound var7 = new NBTTagCompound();
                var6.writeToNBT(var7);
                var7.removeTag("x");
                var7.removeTag("y");
                var7.removeTag("z");
                var3.setTagInfo("BlockEntityTag", var7);
            }
            boolean var8 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
            boolean var9 = var3 == null || var3.getItem() != null;
            boolean bl = var10 = var3 == null || var3.getMetadata() >= 0 && var3.stackSize <= 64 && var3.stackSize > 0;
            if (var8 && var9 && var10) {
                if (var3 == null) {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), null);
                } else {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), var3);
                }
                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            } else if (var2 && var9 && var10 && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                EntityItem var11 = this.playerEntity.dropPlayerItemWithRandomChoice(var3, true);
                if (var11 != null) {
                    var11.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    @Override
    public void processConfirmTransaction(C0FPacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        Short var2 = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
        if (var2 != null && packetIn.getUid() == var2.shortValue() && this.playerEntity.openContainer.windowId == packetIn.getId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.func_175149_v()) {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }

    @Override
    public void processUpdateSign(C12PacketUpdateSign packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        BlockPos var3 = packetIn.func_179722_a();
        if (var2.isBlockLoaded(var3)) {
            TileEntity var4 = var2.getTileEntity(var3);
            if (!(var4 instanceof TileEntitySign)) {
                return;
            }
            TileEntitySign var5 = (TileEntitySign)var4;
            if (!var5.getIsEditable() || var5.func_145911_b() != this.playerEntity) {
                this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }
            System.arraycopy(packetIn.func_180768_b(), 0, var5.signText, 0, 4);
            var5.markDirty();
            var2.markBlockForUpdate(var3);
        }
    }

    @Override
    public void processKeepAlive(C00PacketKeepAlive packetIn) {
        if (packetIn.getKey() == this.field_147378_h) {
            int var2 = (int)(this.currentTimeMillis() - this.lastPingTime);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + var2) / 4;
        }
    }

    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }

    @Override
    public void processPlayerAbilities(C13PacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.capabilities.isFlying = packetIn.isFlying() && this.playerEntity.capabilities.allowFlying;
    }

    @Override
    public void processTabComplete(C14PacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        ArrayList var2 = Lists.newArrayList();
        for (String var4 : this.serverController.func_180506_a(this.playerEntity, packetIn.getMessage(), packetIn.func_179709_b())) {
            var2.add(var4);
        }
        this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete(var2.toArray(new String[var2.size()])));
    }

    @Override
    public void processClientSettings(C15PacketClientSettings packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.handleClientSettings(packetIn);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processVanilla250Packet(C17PacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
        if ("MC|BEdit".equals(packetIn.getChannelName())) {
            PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)packetIn.getBufferData()));
            try {
                ItemStack var3 = var2.readItemStackFromBuffer();
                if (var3 == null) {
                    return;
                }
                if (!ItemWritableBook.validBookPageTagContents(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
                if (var4 == null) return;
                if (var3.getItem() != Items.writable_book || var3.getItem() != var4.getItem()) return;
                var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                return;
            }
            catch (Exception var38) {
                logger.error("Couldn't handle book info", (Throwable)var38);
                return;
            }
            finally {
                var2.release();
            }
        }
        if ("MC|BSign".equals(packetIn.getChannelName())) {
            PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)packetIn.getBufferData()));
            try {
                ItemStack var3 = var2.readItemStackFromBuffer();
                if (var3 == null) return;
                if (!ItemEditableBook.validBookTagContents(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
                if (var4 == null) {
                    return;
                }
                if (var3.getItem() != Items.written_book || var4.getItem() != Items.writable_book) return;
                var4.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                var4.setTagInfo("title", new NBTTagString(var3.getTagCompound().getString("title")));
                var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                var4.setItem(Items.written_book);
                return;
            }
            catch (Exception var36) {
                logger.error("Couldn't sign book", (Throwable)var36);
                return;
            }
            finally {
                var2.release();
            }
        }
        if ("MC|TrSel".equals(packetIn.getChannelName())) {
            try {
                int var40 = packetIn.getBufferData().readInt();
                Container var42 = this.playerEntity.openContainer;
                if (!(var42 instanceof ContainerMerchant)) return;
                ((ContainerMerchant)var42).setCurrentRecipeIndex(var40);
                return;
            }
            catch (Exception var35) {
                logger.error("Couldn't select trade", (Throwable)var35);
            }
            return;
        } else if ("MC|AdvCdm".equals(packetIn.getChannelName())) {
            if (!this.serverController.isCommandBlockEnabled()) {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            } else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
                PacketBuffer var2 = packetIn.getBufferData();
                try {
                    try {
                        Entity var48;
                        byte var43 = var2.readByte();
                        CommandBlockLogic var46 = null;
                        if (var43 == 0) {
                            TileEntity var5 = this.playerEntity.worldObj.getTileEntity(new BlockPos(var2.readInt(), var2.readInt(), var2.readInt()));
                            if (var5 instanceof TileEntityCommandBlock) {
                                var46 = ((TileEntityCommandBlock)var5).getCommandBlockLogic();
                            }
                        } else if (var43 == 1 && (var48 = this.playerEntity.worldObj.getEntityByID(var2.readInt())) instanceof EntityMinecartCommandBlock) {
                            var46 = ((EntityMinecartCommandBlock)var48).func_145822_e();
                        }
                        String var49 = var2.readStringFromBuffer(var2.readableBytes());
                        boolean var6 = var2.readBoolean();
                        if (var46 == null) return;
                        var46.setCommand(var49);
                        var46.func_175573_a(var6);
                        if (!var6) {
                            var46.func_145750_b(null);
                        }
                        var46.func_145756_e();
                        this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", var49));
                        return;
                    }
                    catch (Exception var33) {
                        logger.error("Couldn't set command block", (Throwable)var33);
                        var2.release();
                    }
                    return;
                }
                finally {
                    var2.release();
                }
            } else {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
            }
            return;
        } else if ("MC|Beacon".equals(packetIn.getChannelName())) {
            if (!(this.playerEntity.openContainer instanceof ContainerBeacon)) return;
            try {
                PacketBuffer var2 = packetIn.getBufferData();
                int var44 = var2.readInt();
                int var47 = var2.readInt();
                ContainerBeacon var50 = (ContainerBeacon)this.playerEntity.openContainer;
                Slot var51 = var50.getSlot(0);
                if (!var51.getHasStack()) return;
                var51.decrStackSize(1);
                IInventory var7 = var50.func_180611_e();
                var7.setField(1, var44);
                var7.setField(2, var47);
                var7.markDirty();
                return;
            }
            catch (Exception var32) {
                logger.error("Couldn't set beacon", (Throwable)var32);
            }
            return;
        } else {
            if (!"MC|ItemName".equals(packetIn.getChannelName()) || !(this.playerEntity.openContainer instanceof ContainerRepair)) return;
            ContainerRepair var41 = (ContainerRepair)this.playerEntity.openContainer;
            if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1) {
                String var45 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
                if (var45.length() > 30) return;
                var41.updateItemName(var45);
                return;
            } else {
                var41.updateItemName("");
            }
        }
    }
}

