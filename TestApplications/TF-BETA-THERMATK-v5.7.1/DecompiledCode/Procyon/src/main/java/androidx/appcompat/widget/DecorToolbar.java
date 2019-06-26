// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.view.Window$Callback;
import android.graphics.drawable.Drawable;

public interface DecorToolbar
{
    CharSequence getTitle();
    
    void setIcon(final int p0);
    
    void setIcon(final Drawable p0);
    
    void setLogo(final int p0);
    
    void setWindowCallback(final Window$Callback p0);
    
    void setWindowTitle(final CharSequence p0);
}
