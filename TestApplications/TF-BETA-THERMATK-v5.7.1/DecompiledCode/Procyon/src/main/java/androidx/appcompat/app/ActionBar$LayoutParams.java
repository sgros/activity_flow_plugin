// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.app;

import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewGroup$MarginLayoutParams;

public class ActionBar$LayoutParams extends ViewGroup$MarginLayoutParams
{
    public int gravity;
    
    public ActionBar$LayoutParams(final int n, final int n2) {
        super(n, n2);
        this.gravity = 0;
        this.gravity = 8388627;
    }
    
    public ActionBar$LayoutParams(final Context context, final AttributeSet set) {
        super(context, set);
        this.gravity = 0;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.ActionBarLayout);
        this.gravity = obtainStyledAttributes.getInt(R$styleable.ActionBarLayout_android_layout_gravity, 0);
        obtainStyledAttributes.recycle();
    }
    
    public ActionBar$LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super(viewGroup$LayoutParams);
        this.gravity = 0;
    }
    
    public ActionBar$LayoutParams(final ActionBar$LayoutParams actionBar$LayoutParams) {
        super((ViewGroup$MarginLayoutParams)actionBar$LayoutParams);
        this.gravity = 0;
        this.gravity = actionBar$LayoutParams.gravity;
    }
}
