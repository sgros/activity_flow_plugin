// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

public interface ConnectivityMonitor extends LifecycleListener
{
    public interface ConnectivityListener
    {
        void onConnectivityChanged(final boolean p0);
    }
}
