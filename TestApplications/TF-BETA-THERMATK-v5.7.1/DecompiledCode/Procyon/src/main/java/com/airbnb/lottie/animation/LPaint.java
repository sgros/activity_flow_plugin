// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation;

import android.os.LocaleList;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;

public class LPaint extends Paint
{
    public LPaint() {
    }
    
    public LPaint(final int n) {
        super(n);
    }
    
    public LPaint(final int n, final PorterDuff$Mode porterDuff$Mode) {
        super(n);
        this.setXfermode((Xfermode)new PorterDuffXfermode(porterDuff$Mode));
    }
    
    public LPaint(final PorterDuff$Mode porterDuff$Mode) {
        this.setXfermode((Xfermode)new PorterDuffXfermode(porterDuff$Mode));
    }
    
    public void setTextLocales(final LocaleList list) {
    }
}
