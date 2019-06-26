// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.drawable.Drawable;

public abstract class StatusDrawable extends Drawable
{
    public abstract void setIsChat(final boolean p0);
    
    public abstract void start();
    
    public abstract void stop();
}
