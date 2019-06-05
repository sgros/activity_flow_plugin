// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.nightmode.themed;

public final class ThemedWidgetUtils
{
    public static final ThemedWidgetUtils INSTANCE;
    private static final int[] STATE_NIGHT_MODE;
    
    static {
        INSTANCE = new ThemedWidgetUtils();
        STATE_NIGHT_MODE = new int[] { 2130969045 };
    }
    
    private ThemedWidgetUtils() {
    }
    
    public final int[] getSTATE_NIGHT_MODE() {
        return ThemedWidgetUtils.STATE_NIGHT_MODE;
    }
}
