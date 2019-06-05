// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints;

import java.util.List;

public interface WorkConstraintsCallback
{
    void onAllConstraintsMet(final List<String> p0);
    
    void onAllConstraintsNotMet(final List<String> p0);
}
