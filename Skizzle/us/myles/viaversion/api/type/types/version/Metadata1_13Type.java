/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13;
import us.myles.ViaVersion.api.type.types.minecraft.ModernMetaType;

public class Metadata1_13Type
extends ModernMetaType {
    @Override
    protected MetaType getType(int index) {
        return MetaType1_13.byId(index);
    }
}

