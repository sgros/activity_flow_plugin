// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import androidx.work.impl.model.WorkSpec;

public interface Scheduler
{
    void cancel(final String p0);
    
    void schedule(final WorkSpec... p0);
}
