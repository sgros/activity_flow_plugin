// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

public class Dependency
{
    public final String prerequisiteId;
    public final String workSpecId;
    
    public Dependency(final String workSpecId, final String prerequisiteId) {
        this.workSpecId = workSpecId;
        this.prerequisiteId = prerequisiteId;
    }
}
