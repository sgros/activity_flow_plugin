// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;

public interface TintAwareDrawable
{
    void setTint(final int p0);
    
    void setTintList(final ColorStateList p0);
    
    void setTintMode(final PorterDuff$Mode p0);
}
