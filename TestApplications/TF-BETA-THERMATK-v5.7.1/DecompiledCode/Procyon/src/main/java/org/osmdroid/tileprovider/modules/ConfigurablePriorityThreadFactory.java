// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.util.concurrent.ThreadFactory;

public class ConfigurablePriorityThreadFactory implements ThreadFactory
{
    private final String mName;
    private final int mPriority;
    
    public ConfigurablePriorityThreadFactory(final int mPriority, final String mName) {
        this.mPriority = mPriority;
        this.mName = mName;
    }
    
    @Override
    public Thread newThread(final Runnable target) {
        final Thread thread = new Thread(target);
        thread.setPriority(this.mPriority);
        final String mName = this.mName;
        if (mName != null) {
            thread.setName(mName);
        }
        return thread;
    }
}
