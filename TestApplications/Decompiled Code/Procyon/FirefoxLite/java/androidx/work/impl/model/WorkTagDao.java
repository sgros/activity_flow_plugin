// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import java.util.List;

public interface WorkTagDao
{
    List<String> getTagsForWorkSpecId(final String p0);
    
    void insert(final WorkTag p0);
}
