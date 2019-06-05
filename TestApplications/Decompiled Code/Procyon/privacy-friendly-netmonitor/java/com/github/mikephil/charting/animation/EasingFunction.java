// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.animation;

import android.annotation.SuppressLint;
import android.animation.TimeInterpolator;

@SuppressLint({ "NewApi" })
public interface EasingFunction extends TimeInterpolator
{
    float getInterpolation(final float p0);
}
