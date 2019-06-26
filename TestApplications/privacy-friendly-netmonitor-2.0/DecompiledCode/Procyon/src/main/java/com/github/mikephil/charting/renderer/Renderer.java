// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class Renderer
{
    protected ViewPortHandler mViewPortHandler;
    
    public Renderer(final ViewPortHandler mViewPortHandler) {
        this.mViewPortHandler = mViewPortHandler;
    }
}
