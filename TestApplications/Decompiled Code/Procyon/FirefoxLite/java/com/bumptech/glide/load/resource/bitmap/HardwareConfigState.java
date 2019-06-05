// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap$Config;
import android.os.Build$VERSION;
import com.bumptech.glide.load.DecodeFormat;
import android.graphics.BitmapFactory$Options;
import android.util.Log;
import java.io.File;

final class HardwareConfigState
{
    private static final File FD_SIZE_LIST;
    private static volatile HardwareConfigState instance;
    private volatile int decodesSinceLastFdCheck;
    private volatile boolean isHardwareConfigAllowed;
    
    static {
        FD_SIZE_LIST = new File("/proc/self/fd");
    }
    
    private HardwareConfigState() {
        this.isHardwareConfigAllowed = true;
    }
    
    static HardwareConfigState getInstance() {
        if (HardwareConfigState.instance == null) {
            synchronized (HardwareConfigState.class) {
                if (HardwareConfigState.instance == null) {
                    HardwareConfigState.instance = new HardwareConfigState();
                }
            }
        }
        return HardwareConfigState.instance;
    }
    
    private boolean isFdSizeBelowHardwareLimit() {
        synchronized (this) {
            final int decodesSinceLastFdCheck = this.decodesSinceLastFdCheck + 1;
            this.decodesSinceLastFdCheck = decodesSinceLastFdCheck;
            if (decodesSinceLastFdCheck >= 50) {
                boolean isHardwareConfigAllowed = false;
                this.decodesSinceLastFdCheck = 0;
                final int length = HardwareConfigState.FD_SIZE_LIST.list().length;
                if (length < 700) {
                    isHardwareConfigAllowed = true;
                }
                this.isHardwareConfigAllowed = isHardwareConfigAllowed;
                if (!this.isHardwareConfigAllowed && Log.isLoggable("Downsampler", 5)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
                    sb.append(length);
                    sb.append(", limit ");
                    sb.append(700);
                    Log.w("Downsampler", sb.toString());
                }
            }
            return this.isHardwareConfigAllowed;
        }
    }
    
    @TargetApi(26)
    boolean setHardwareConfigIfAllowed(final int n, final int n2, final BitmapFactory$Options bitmapFactory$Options, final DecodeFormat decodeFormat, final boolean b, final boolean b2) {
        if (b && Build$VERSION.SDK_INT >= 26 && decodeFormat != DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE && !b2) {
            final boolean b3 = n >= 128 && n2 >= 128 && this.isFdSizeBelowHardwareLimit();
            if (b3) {
                bitmapFactory$Options.inPreferredConfig = Bitmap$Config.HARDWARE;
                bitmapFactory$Options.inMutable = false;
            }
            return b3;
        }
        return false;
    }
}
