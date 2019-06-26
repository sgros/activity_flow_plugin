// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.drawing;

import android.os.Looper;

public class MapSnapshot implements Runnable
{
    public static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
