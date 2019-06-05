// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

class ApplicationLifecycle implements Lifecycle
{
    @Override
    public void addListener(final LifecycleListener lifecycleListener) {
        lifecycleListener.onStart();
    }
    
    @Override
    public void removeListener(final LifecycleListener lifecycleListener) {
    }
}
