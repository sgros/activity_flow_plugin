// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import android.content.Context;

public interface ConnectivityMonitorFactory
{
    ConnectivityMonitor build(final Context p0, final ConnectivityMonitor.ConnectivityListener p1);
}
