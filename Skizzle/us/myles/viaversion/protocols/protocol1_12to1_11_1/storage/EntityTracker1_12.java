/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.protocols.protocol1_12to1_11_1.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_12
extends EntityTracker {
    public EntityTracker1_12(UserConnection user) {
        super(user, Entity1_12Types.EntityType.PLAYER);
    }
}

