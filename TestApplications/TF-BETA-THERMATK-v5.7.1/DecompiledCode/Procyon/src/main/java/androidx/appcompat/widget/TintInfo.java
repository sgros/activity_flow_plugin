// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;

class TintInfo
{
    public boolean mHasTintList;
    public boolean mHasTintMode;
    public ColorStateList mTintList;
    public PorterDuff$Mode mTintMode;
    
    void clear() {
        this.mTintList = null;
        this.mHasTintList = false;
        this.mTintMode = null;
        this.mHasTintMode = false;
    }
}
