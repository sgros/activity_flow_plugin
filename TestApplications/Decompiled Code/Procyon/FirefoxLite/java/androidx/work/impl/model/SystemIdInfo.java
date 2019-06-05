// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

public class SystemIdInfo
{
    public final int systemId;
    public final String workSpecId;
    
    public SystemIdInfo(final String workSpecId, final int systemId) {
        this.workSpecId = workSpecId;
        this.systemId = systemId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final SystemIdInfo systemIdInfo = (SystemIdInfo)o;
            return this.systemId == systemIdInfo.systemId && this.workSpecId.equals(systemIdInfo.workSpecId);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.workSpecId.hashCode() * 31 + this.systemId;
    }
}
