/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.StringTranslate;

public class StatCollector {
    private static StringTranslate reeName = StringTranslate.getInstance();
    private static StringTranslate fallbackTranslator = new StringTranslate();
    private static final String __OBFID = "CL_00001211";

    public static String translateToLocal(String p_74838_0_) {
        return reeName.translateKey(p_74838_0_);
    }

    public static String translateToLocalFormatted(String p_74837_0_, Object ... p_74837_1_) {
        return reeName.translateKeyFormat(p_74837_0_, p_74837_1_);
    }

    public static String translateToFallback(String p_150826_0_) {
        return fallbackTranslator.translateKey(p_150826_0_);
    }

    public static boolean canTranslate(String p_94522_0_) {
        return reeName.isKeyTranslated(p_94522_0_);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return reeName.getLastUpdateTimeInMilliseconds();
    }
}

