/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_13
extends EntityTracker {
    public EntityTracker1_13(UserConnection user) {
        super(user, Entity1_13Types.EntityType.PLAYER);
    }
}

