// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

public enum MemoryCategory
{
    HIGH(1.5f), 
    LOW(0.5f), 
    NORMAL(1.0f);
    
    private float multiplier;
    
    private MemoryCategory(final float multiplier) {
        this.multiplier = multiplier;
    }
}
