/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.spongepowered.api.scheduler.Task
 */
package us.myles.ViaVersion.sponge.platform;

import org.spongepowered.api.scheduler.Task;
import us.myles.ViaVersion.api.platform.TaskId;

public class SpongeTaskId
implements TaskId {
    private final Task object;

    public SpongeTaskId(Task object) {
        this.object = object;
    }

    public Task getObject() {
        return this.object;
    }
}

