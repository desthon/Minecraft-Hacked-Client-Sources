/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

public class EntityGuardian
extends EntityMob {
    private float field_175482_b;
    private float field_175484_c;
    private float field_175483_bk;
    private float field_175485_bl;
    private float field_175486_bm;
    private EntityLivingBase field_175478_bn;
    private int field_175479_bo;
    private boolean field_175480_bp;
    private EntityAIWander field_175481_bq;
    private static final String __OBFID = "CL_00002213";

    public EntityGuardian(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.tasks.addTask(4, new AIGuardianAttack());
        EntityAIMoveTowardsRestriction var2 = new EntityAIMoveTowardsRestriction(this, 1.0);
        this.tasks.addTask(5, var2);
        this.field_175481_bq = new EntityAIWander(this, 1.0, 80);
        this.tasks.addTask(7, this.field_175481_bq);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.field_175481_bq.setMutexBits(3);
        var2.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector()));
        this.moveHelper = new GuardianMoveHelper();
        this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.func_175467_a(tagCompund.getBoolean("Elder"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Elder", this.func_175461_cl());
    }

    @Override
    protected PathNavigate func_175447_b(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }

    private boolean func_175468_a(int p_175468_1_) {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_175468_1_) != 0;
    }

    private void func_175473_a(int p_175473_1_, boolean p_175473_2_) {
        int var3 = this.dataWatcher.getWatchableObjectInt(16);
        if (p_175473_2_) {
            this.dataWatcher.updateObject(16, var3 | p_175473_1_);
        } else {
            this.dataWatcher.updateObject(16, var3 & ~p_175473_1_);
        }
    }

    public boolean func_175472_n() {
        return this.func_175468_a(2);
    }

    private void func_175476_l(boolean p_175476_1_) {
        this.func_175473_a(2, p_175476_1_);
    }

    public int func_175464_ck() {
        return this.func_175461_cl() ? 60 : 80;
    }

    public boolean func_175461_cl() {
        return this.func_175468_a(4);
    }

    public void func_175467_a(boolean p_175467_1_) {
        this.func_175473_a(4, p_175467_1_);
        if (p_175467_1_) {
            this.setSize(1.9975f, 1.9975f);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0);
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0);
            this.enablePersistence();
            this.field_175481_bq.func_179479_b(400);
        }
    }

    public void func_175465_cm() {
        this.func_175467_a(true);
        this.field_175485_bl = 1.0f;
        this.field_175486_bm = 1.0f;
    }

    private void func_175463_b(int p_175463_1_) {
        this.dataWatcher.updateObject(17, p_175463_1_);
    }

    public boolean func_175474_cn() {
        return this.dataWatcher.getWatchableObjectInt(17) != 0;
    }

    public EntityLivingBase func_175466_co() {
        if (!this.func_175474_cn()) {
            return null;
        }
        if (this.worldObj.isRemote) {
            if (this.field_175478_bn != null) {
                return this.field_175478_bn;
            }
            Entity var1 = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
            if (var1 instanceof EntityLivingBase) {
                this.field_175478_bn = (EntityLivingBase)var1;
                return this.field_175478_bn;
            }
            return null;
        }
        return this.getAttackTarget();
    }

    @Override
    public void func_145781_i(int p_145781_1_) {
        super.func_145781_i(p_145781_1_);
        if (p_145781_1_ == 16) {
            if (this.func_175461_cl() && this.width < 1.0f) {
                this.setSize(1.9975f, 1.9975f);
            }
        } else if (p_145781_1_ == 17) {
            this.field_175479_bo = 0;
            this.field_175478_bn = null;
        }
    }

    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    protected String getLivingSound() {
        return !this.isInWater() ? "mob.guardian.land.idle" : (this.func_175461_cl() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
    }

    @Override
    protected String getHurtSound() {
        return !this.isInWater() ? "mob.guardian.land.hit" : (this.func_175461_cl() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
    }

    @Override
    protected String getDeathSound() {
        return !this.isInWater() ? "mob.guardian.land.death" : (this.func_175461_cl() ? "mob.guardian.elder.death" : "mob.guardian.death");
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    @Override
    public float func_180484_a(BlockPos p_180484_1_) {
        return this.worldObj.getBlockState(p_180484_1_).getBlock().getMaterial() == Material.water ? 10.0f + this.worldObj.getLightBrightness(p_180484_1_) - 0.5f : super.func_180484_a(p_180484_1_);
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.field_175484_c = this.field_175482_b;
            if (!this.isInWater()) {
                this.field_175483_bk = 2.0f;
                if (this.motionY > 0.0 && this.field_175480_bp && !this.isSlient()) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0f, 1.0f, false);
                }
                this.field_175480_bp = this.motionY < 0.0 && this.worldObj.func_175677_d(new BlockPos(this).offsetDown(), false);
            } else {
                this.field_175483_bk = this.func_175472_n() ? (this.field_175483_bk < 0.5f ? 4.0f : (this.field_175483_bk += (0.5f - this.field_175483_bk) * 0.1f)) : (this.field_175483_bk += (0.125f - this.field_175483_bk) * 0.2f);
            }
            this.field_175482_b += this.field_175483_bk;
            this.field_175486_bm = this.field_175485_bl;
            this.field_175485_bl = !this.isInWater() ? this.rand.nextFloat() : (this.func_175472_n() ? (this.field_175485_bl += (0.0f - this.field_175485_bl) * 0.25f) : (this.field_175485_bl += (1.0f - this.field_175485_bl) * 0.06f));
            if (this.func_175472_n() && this.isInWater()) {
                Vec3 var1 = this.getLook(0.0f);
                for (int var2 = 0; var2 < 2; ++var2) {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width - var1.xCoord * 1.5, this.posY + this.rand.nextDouble() * (double)this.height - var1.yCoord * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width - var1.zCoord * 1.5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (this.func_175474_cn()) {
                EntityLivingBase var14;
                if (this.field_175479_bo < this.func_175464_ck()) {
                    ++this.field_175479_bo;
                }
                if ((var14 = this.func_175466_co()) != null) {
                    this.getLookHelper().setLookPositionWithEntity(var14, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    double var15 = this.func_175477_p(0.0f);
                    double var4 = var14.posX - this.posX;
                    double var6 = var14.posY + (double)(var14.height * 0.5f) - (this.posY + (double)this.getEyeHeight());
                    double var8 = var14.posZ - this.posZ;
                    double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
                    var4 /= var10;
                    var6 /= var10;
                    var8 /= var10;
                    double var12 = this.rand.nextDouble();
                    while (var12 < var10) {
                        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var4 * (var12 += 1.8 - var15 + this.rand.nextDouble() * (1.7 - var15)), this.posY + var6 * var12 + (double)this.getEyeHeight(), this.posZ + var8 * var12, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        } else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.motionZ += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.func_175474_cn()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }

    public float func_175471_a(float p_175471_1_) {
        return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
    }

    public float func_175469_o(float p_175469_1_) {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
    }

    public float func_175477_p(float p_175477_1_) {
        return ((float)this.field_175479_bo + p_175477_1_) / (float)this.func_175464_ck();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.func_175461_cl()) {
            if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
                Potion var5 = Potion.digSlowdown;
                List var6 = this.worldObj.func_175661_b(EntityPlayerMP.class, new Predicate(){
                    private static final String __OBFID = "CL_00002212";

                    public boolean func_179913_a(EntityPlayerMP p_179913_1_) {
                        return EntityGuardian.this.getDistanceSqToEntity(p_179913_1_) < 2500.0 && p_179913_1_.theItemInWorldManager.func_180239_c();
                    }

                    public boolean apply(Object p_apply_1_) {
                        return this.func_179913_a((EntityPlayerMP)p_apply_1_);
                    }
                });
                for (EntityPlayerMP var8 : var6) {
                    if (var8.isPotionActive(var5) && var8.getActivePotionEffect(var5).getAmplifier() >= 2 && var8.getActivePotionEffect(var5).getDuration() >= 1200) continue;
                    var8.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0f));
                    var8.addPotionEffect(new PotionEffect(var5.id, 6000, 2));
                }
            }
            if (!this.hasHome()) {
                this.func_175449_a(new BlockPos(this), 16);
            }
        }
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
        if (var3 > 0) {
            this.entityDropItem(new ItemStack(Items.prismarine_shard, var3, 0), 1.0f);
        }
        if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 1.0f);
        } else if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0f);
        }
        if (p_70628_1_ && this.func_175461_cl()) {
            this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0f);
        }
    }

    @Override
    protected void addRandomArmor() {
        ItemStack var1 = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
        this.entityDropItem(var1, 1.0f);
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean handleLavaMovement() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase var3 = (EntityLivingBase)source.getSourceOfDamage();
            if (!source.isExplosion()) {
                var3.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0f);
                var3.playSound("damage.thorns", 0.5f, 1.0f);
            }
        }
        this.field_175481_bq.func_179480_f();
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (this.isServerWorld()) {
            if (this.isInWater()) {
                this.moveFlying(p_70612_1_, p_70612_2_, 0.1f);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)0.9f;
                this.motionY *= (double)0.9f;
                this.motionZ *= (double)0.9f;
                if (!this.func_175472_n() && this.getAttackTarget() == null) {
                    this.motionY -= 0.005;
                }
            } else {
                super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            }
        } else {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    class AIGuardianAttack
    extends EntityAIBase {
        private EntityGuardian field_179456_a;
        private int field_179455_b;
        private static final String __OBFID = "CL_00002211";

        public AIGuardianAttack() {
            this.field_179456_a = EntityGuardian.this;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase var1 = this.field_179456_a.getAttackTarget();
            return var1 != null && var1.isEntityAlive();
        }

        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.field_179456_a.func_175461_cl() || this.field_179456_a.getDistanceSqToEntity(this.field_179456_a.getAttackTarget()) > 9.0);
        }

        @Override
        public void startExecuting() {
            this.field_179455_b = -10;
            this.field_179456_a.getNavigator().clearPathEntity();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(this.field_179456_a.getAttackTarget(), 90.0f, 90.0f);
            this.field_179456_a.isAirBorne = true;
        }

        @Override
        public void resetTask() {
            this.field_179456_a.func_175463_b(0);
            this.field_179456_a.setAttackTarget(null);
            this.field_179456_a.field_175481_bq.func_179480_f();
        }

        /*
         * Exception decompiling
         */
        @Override
        public void updateTask() {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl110 : ALOAD_0 - null : trying to set 1 previously set to 0
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:203)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1542)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:400)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
             * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
             * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             * org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:903)
             * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1015)
             * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
             * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
             * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
             * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
             * org.benf.cfr.reader.Main.main(Main.java:49)
             */
            throw new IllegalStateException(Decompilation failed);
        }
    }

    class GuardianMoveHelper
    extends EntityMoveHelper {
        private EntityGuardian field_179930_g;
        private static final String __OBFID = "CL_00002209";

        public GuardianMoveHelper() {
            super(EntityGuardian.this);
            this.field_179930_g = EntityGuardian.this;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.update && !this.field_179930_g.getNavigator().noPath()) {
                double var1 = this.posX - this.field_179930_g.posX;
                double var3 = this.posY - this.field_179930_g.posY;
                double var5 = this.posZ - this.field_179930_g.posZ;
                double var7 = var1 * var1 + var3 * var3 + var5 * var5;
                var7 = MathHelper.sqrt_double(var7);
                float var9 = (float)(Math.atan2(var5, var1) * 180.0 / Math.PI) - 90.0f;
                this.field_179930_g.renderYawOffset = this.field_179930_g.rotationYaw = this.limitAngle(this.field_179930_g.rotationYaw, var9, 30.0f);
                float var10 = (float)(this.speed * this.field_179930_g.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                this.field_179930_g.setAIMoveSpeed(this.field_179930_g.getAIMoveSpeed() + (var10 - this.field_179930_g.getAIMoveSpeed()) * 0.125f);
                double var11 = Math.sin((double)(this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.5) * 0.05;
                double var13 = Math.cos(this.field_179930_g.rotationYaw * (float)Math.PI / 180.0f);
                double var15 = Math.sin(this.field_179930_g.rotationYaw * (float)Math.PI / 180.0f);
                this.field_179930_g.motionX += var11 * var13;
                this.field_179930_g.motionZ += var11 * var15;
                var11 = Math.sin((double)(this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.75) * 0.05;
                this.field_179930_g.motionY += var11 * (var15 + var13) * 0.25;
                this.field_179930_g.motionY += (double)this.field_179930_g.getAIMoveSpeed() * (var3 /= var7) * 0.1;
                EntityLookHelper var17 = this.field_179930_g.getLookHelper();
                double var18 = this.field_179930_g.posX + var1 / var7 * 2.0;
                double var20 = (double)this.field_179930_g.getEyeHeight() + this.field_179930_g.posY + var3 / var7 * 1.0;
                double var22 = this.field_179930_g.posZ + var5 / var7 * 2.0;
                double var24 = var17.func_180423_e();
                double var26 = var17.func_180422_f();
                double var28 = var17.func_180421_g();
                if (!var17.func_180424_b()) {
                    var24 = var18;
                    var26 = var20;
                    var28 = var22;
                }
                this.field_179930_g.getLookHelper().setLookPosition(var24 + (var18 - var24) * 0.125, var26 + (var20 - var26) * 0.125, var28 + (var22 - var28) * 0.125, 10.0f, 40.0f);
                this.field_179930_g.func_175476_l(true);
            } else {
                this.field_179930_g.setAIMoveSpeed(0.0f);
                this.field_179930_g.func_175476_l(false);
            }
        }
    }

    class GuardianTargetSelector
    implements Predicate {
        private EntityGuardian field_179916_a;
        private static final String __OBFID = "CL_00002210";

        GuardianTargetSelector() {
            this.field_179916_a = EntityGuardian.this;
        }

        public boolean func_179915_a(EntityLivingBase p_179915_1_) {
            return (p_179915_1_ instanceof EntityPlayer || p_179915_1_ instanceof EntitySquid) && p_179915_1_.getDistanceSqToEntity(this.field_179916_a) > 9.0;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_179915_a((EntityLivingBase)p_apply_1_);
        }
    }
}

