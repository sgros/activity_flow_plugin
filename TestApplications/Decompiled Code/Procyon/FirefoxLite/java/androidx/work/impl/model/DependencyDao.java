// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import java.util.List;

public interface DependencyDao
{
    List<String> getDependentWorkIds(final String p0);
    
    boolean hasCompletedAllPrerequisites(final String p0);
    
    boolean hasDependents(final String p0);
    
    void insertDependency(final Dependency p0);
}
