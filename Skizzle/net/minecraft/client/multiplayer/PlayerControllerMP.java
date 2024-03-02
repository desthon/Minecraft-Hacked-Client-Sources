/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.multiplayer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import skizzle.Client;
import skizzle.events.EventType;
import skizzle.events.listeners.EventAttack;

public class PlayerControllerMP {
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;
    private BlockPos field_178895_c = new BlockPos(-1, -1, -1);
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    public WorldSettings.GameType currentGameType = WorldSettings.GameType.SURVIVAL;
    private int currentPlayerItem;
    private static final String __OBFID = "CL_00000881";

    public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_) {
        this.mc = mcIn;
        this.netClientHandler = p_i45062_2_;
    }

    public static void func_178891_a(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_) {
        if (!Minecraft.theWorld.func_175719_a(mcIn.thePlayer, p_178891_2_, p_178891_3_)) {
            p_178891_1_.func_178888_a(p_178891_2_, p_178891_3_);
        }
    }

    public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }

    public boolean enableEverythingIsScrewedUpMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public void setGameType(WorldSettings.GameType p_78746_1_) {
        this.currentGameType = p_78746_1_;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    public void flipPlayer(EntityPlayer playerIn) {
        playerIn.rotationYaw = -180.0f;
    }

    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean func_178888_a(BlockPos p_178888_1_, EnumFacing p_178888_2_) {
        ItemStack var7;
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.mc.thePlayer.func_175142_cm()) {
                Block var3 = Minecraft.theWorld.getBlockState(p_178888_1_).getBlock();
                ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();
                if (var4 == null) {
                    return false;
                }
                if (!var4.canDestroy(var3)) {
                    return false;
                }
            }
        }
        if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        WorldClient var8 = Minecraft.theWorld;
        IBlockState var9 = var8.getBlockState(p_178888_1_);
        Block var5 = var9.getBlock();
        if (var5.getMaterial() == Material.air) {
            return false;
        }
        var8.playAuxSFX(2001, p_178888_1_, Block.getStateId(var9));
        boolean var6 = var8.setBlockToAir(p_178888_1_);
        if (var6) {
            var5.onBlockDestroyedByPlayer(var8, p_178888_1_, var9);
        }
        this.field_178895_c = new BlockPos(this.field_178895_c.getX(), -1, this.field_178895_c.getZ());
        if (!this.currentGameType.isCreative() && (var7 = this.mc.thePlayer.getCurrentEquippedItem()) != null) {
            var7.onBlockDestroyed(var8, var5, p_178888_1_, this.mc.thePlayer);
            if (var7.stackSize == 0) {
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        return var6;
    }

    public boolean func_180511_b(BlockPos p_180511_1_, EnumFacing p_180511_2_) {
        Block var3;
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.mc.thePlayer.func_175142_cm()) {
                var3 = Minecraft.theWorld.getBlockState(p_180511_1_).getBlock();
                ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();
                if (var4 == null) {
                    return false;
                }
                if (!var4.canDestroy(var3)) {
                    return false;
                }
            }
        }
        if (!Minecraft.theWorld.getWorldBorder().contains(p_180511_1_)) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
            PlayerControllerMP.func_178891_a(this.mc, this, p_180511_1_, p_180511_2_);
            this.blockHitDelay = 5;
        } else if (!this.isHittingBlock || !this.func_178893_a(p_180511_1_)) {
            boolean var5;
            if (this.isHittingBlock) {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, p_180511_2_));
            }
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
            var3 = Minecraft.theWorld.getBlockState(p_180511_1_).getBlock();
            boolean bl = var5 = var3.getMaterial() != Material.air;
            if (var5 && this.curBlockDamageMP == 0.0f) {
                var3.onBlockClicked(Minecraft.theWorld, p_180511_1_, this.mc.thePlayer);
            }
            if (var5 && var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180511_1_) >= 1.0f) {
                this.func_178888_a(p_180511_1_, p_180511_2_);
            } else {
                this.isHittingBlock = true;
                this.field_178895_c = p_180511_1_;
                this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                this.curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                Minecraft.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int)(this.curBlockDamageMP * 10.0f) - 1);
            }
        }
        return true;
    }

    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0f;
            Minecraft.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, -1);
        }
    }

    public boolean destroyBlock(BlockPos p_180512_1_, EnumFacing p_180512_2_) {
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        }
        if (this.currentGameType.isCreative() && Minecraft.theWorld.getWorldBorder().contains(p_180512_1_)) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
            PlayerControllerMP.func_178891_a(this.mc, this, p_180512_1_, p_180512_2_);
            return true;
        }
        if (this.func_178893_a(p_180512_1_)) {
            Block var3 = Minecraft.theWorld.getBlockState(p_180512_1_).getBlock();
            if (var3.getMaterial() == Material.air) {
                this.isHittingBlock = false;
                return false;
            }
            this.curBlockDamageMP += var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180512_1_);
            if (this.stepSoundTickCounter % 4.0f == 0.0f) {
                this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var3.stepSound.getStepSound()), (var3.stepSound.getVolume() + 1.0f) / 8.0f, var3.stepSound.getFrequency() * 0.5f, (float)p_180512_1_.getX() + 0.5f, (float)p_180512_1_.getY() + 0.5f, (float)p_180512_1_.getZ() + 0.5f));
            }
            this.stepSoundTickCounter += 1.0f;
            if (this.curBlockDamageMP >= 1.0f) {
                this.isHittingBlock = false;
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
                this.func_178888_a(p_180512_1_, p_180512_2_);
                this.curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                this.blockHitDelay = 5;
            }
            Minecraft.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int)(this.curBlockDamageMP * 10.0f) - 1);
            return true;
        }
        return this.func_180511_b(p_180512_1_, p_180512_2_);
    }

    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        } else {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }

    private boolean func_178893_a(BlockPos p_178893_1_) {
        boolean var3;
        ItemStack var2 = this.mc.thePlayer.getHeldItem();
        boolean bl = var3 = this.currentItemHittingBlock == null && var2 == null;
        if (this.currentItemHittingBlock != null && var2 != null) {
            boolean bl2 = var3 = var2.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(var2, this.currentItemHittingBlock) && (var2.isItemStackDamageable() || var2.getMetadata() == this.currentItemHittingBlock.getMetadata());
        }
        return p_178893_1_.equals(this.field_178895_c) && var3;
    }

    private void syncCurrentPlayItem() {
        int var1 = this.mc.thePlayer.inventory.currentItem;
        if (var1 != this.currentPlayerItem) {
            this.currentPlayerItem = var1;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean onPlayerRightClick(EntityPlayerSP p_178890_1_, WorldClient p_178890_2_, ItemStack p_178890_3_, BlockPos p_178890_4_, EnumFacing p_178890_5_, Vec3 p_178890_6_) {
        this.syncCurrentPlayItem();
        float var7 = (float)(p_178890_6_.xCoord - (double)p_178890_4_.getX());
        float var8 = (float)(p_178890_6_.yCoord - (double)p_178890_4_.getY());
        float var9 = (float)(p_178890_6_.zCoord - (double)p_178890_4_.getZ());
        boolean var10 = false;
        if (!Minecraft.theWorld.getWorldBorder().contains(p_178890_4_)) {
            return false;
        }
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            ItemBlock var12;
            IBlockState var11 = p_178890_2_.getBlockState(p_178890_4_);
            if ((!p_178890_1_.isSneaking() || p_178890_1_.getHeldItem() == null) && var11.getBlock().onBlockActivated(p_178890_2_, p_178890_4_, var11, p_178890_1_, p_178890_5_, var7, var8, var9)) {
                var10 = true;
            }
            if (!var10 && p_178890_3_ != null && p_178890_3_.getItem() instanceof ItemBlock && !(var12 = (ItemBlock)p_178890_3_.getItem()).canPlaceBlockOnSide(p_178890_2_, p_178890_4_, p_178890_5_, p_178890_1_, p_178890_3_)) {
                return false;
            }
        }
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(p_178890_4_, p_178890_5_.getIndex(), p_178890_1_.inventory.getCurrentItem(), var7, var8, var9));
        if (!var10 && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            if (p_178890_3_ == null) {
                return false;
            }
            if (this.currentGameType.isCreative()) {
                int var14 = p_178890_3_.getMetadata();
                int var15 = p_178890_3_.stackSize;
                boolean var13 = p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, var7, var8, var9);
                p_178890_3_.setItemDamage(var14);
                p_178890_3_.stackSize = var15;
                return var13;
            }
            return p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, var7, var8, var9);
        }
        return true;
    }

    public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
        int var4 = itemStackIn.stackSize;
        ItemStack var5 = itemStackIn.useItemRightClick(worldIn, playerIn);
        if (var5 == itemStackIn && (var5 == null || var5.stackSize == var4)) {
            return false;
        }
        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = var5;
        if (var5.stackSize == 0) {
            playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
        }
        return true;
    }

    public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_) {
        return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
    }

    public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
        EventAttack event = new EventAttack(targetEntity);
        event.setType(EventType.PRE);
        Client.onEvent(event);
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }

    public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
    }

    public boolean isPlayerRightClickingOnEntity(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_) {
        this.syncCurrentPlayItem();
        Vec3 var4 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, var4));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.func_174825_a(p_178894_1_, var4);
    }

    public ItemStack windowClick(int windowId, int slotId, int p_78753_3_, int clickType, EntityPlayer playerIn) {
        short var6 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack var7 = playerIn.openContainer.slotClick(slotId, p_78753_3_, clickType, playerIn);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, p_78753_3_, clickType, var7, var6));
        return var7;
    }

    public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }

    public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }

    public void sendPacketDropItem(ItemStack itemStackIn) {
        if (this.currentGameType.isCreative() && itemStackIn != null) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }

    public void onStoppedUsingItem(EntityPlayer playerIn) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }

    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }

    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }

    public boolean isRidingHorse() {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }

    public boolean isSpectatorMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public WorldSettings.GameType func_178889_l() {
        return this.currentGameType;
    }
}

