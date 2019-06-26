// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import android.graphics.PorterDuff$Mode;
import android.graphics.PorterDuffColorFilter;

public class SimpleColorFilter extends PorterDuffColorFilter
{
    public SimpleColorFilter(final int n) {
        super(n, PorterDuff$Mode.SRC_ATOP);
    }
}
