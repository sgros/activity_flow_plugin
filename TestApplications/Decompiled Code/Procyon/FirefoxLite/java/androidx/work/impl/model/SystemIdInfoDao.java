// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

public interface SystemIdInfoDao
{
    SystemIdInfo getSystemIdInfo(final String p0);
    
    void insertSystemIdInfo(final SystemIdInfo p0);
    
    void removeSystemIdInfo(final String p0);
}
