/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class ResourceLocation {
    protected final String resourceDomain;
    protected final String resourcePath;
    private static final String __OBFID = "CL_00001082";

    protected ResourceLocation(int p_i45928_1_, String ... p_i45928_2_) {
        this.resourceDomain = StringUtils.isEmpty((CharSequence)p_i45928_2_[0]) ? "minecraft" : p_i45928_2_[0].toLowerCase();
        this.resourcePath = p_i45928_2_[1];
        Validate.notNull((Object)this.resourcePath);
    }

    public ResourceLocation(String resourceName) {
        this(0, ResourceLocation.func_177516_a(resourceName));
    }

    public ResourceLocation(String p_i1292_1_, String p_i1292_2_) {
        this(0, p_i1292_1_, p_i1292_2_);
    }

    protected static String[] func_177516_a(String p_177516_0_) {
        String[] arrstring = new String[2];
        arrstring[1] = p_177516_0_;
        String[] var1 = arrstring;
        int var2 = p_177516_0_.indexOf(58);
        if (var2 >= 0) {
            var1[1] = p_177516_0_.substring(var2 + 1, p_177516_0_.length());
            if (var2 > 1) {
                var1[0] = p_177516_0_.substring(0, var2);
            }
        }
        return var1;
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public String getResourceDomain() {
        return this.resourceDomain;
    }

    public String toString() {
        return String.valueOf(this.resourceDomain) + ':' + this.resourcePath;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ResourceLocation)) {
            return false;
        }
        ResourceLocation var2 = (ResourceLocation)p_equals_1_;
        return this.resourceDomain.equals(var2.resourceDomain) && this.resourcePath.equals(var2.resourcePath);
    }

    public int hashCode() {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}

