// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

public class Swatch
{
    public float brushWeight;
    public int color;
    public float colorLocation;
    
    public Swatch(final int color, final float colorLocation, final float brushWeight) {
        this.color = color;
        this.colorLocation = colorLocation;
        this.brushWeight = brushWeight;
    }
}
