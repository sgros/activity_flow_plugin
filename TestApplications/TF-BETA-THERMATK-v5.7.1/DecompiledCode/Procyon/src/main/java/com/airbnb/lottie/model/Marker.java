// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

public class Marker
{
    private static String CARRIAGE_RETURN = "\r";
    public final float durationFrames;
    private final String name;
    public final float startFrame;
    
    public Marker(final String name, final float startFrame, final float durationFrames) {
        this.name = name;
        this.durationFrames = durationFrames;
        this.startFrame = startFrame;
    }
    
    public boolean matchesName(final String s) {
        if (this.name.equalsIgnoreCase(s)) {
            return true;
        }
        if (this.name.endsWith(Marker.CARRIAGE_RETURN)) {
            final String name = this.name;
            if (name.substring(0, name.length() - 1).equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
