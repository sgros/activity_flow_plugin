// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.widget.ImageButton;

public class VisibilityAwareImageButton extends ImageButton
{
    private int userSetVisibility;
    
    public final int getUserSetVisibility() {
        return this.userSetVisibility;
    }
    
    public final void internalSetVisibility(final int n, final boolean b) {
        super.setVisibility(n);
        if (b) {
            this.userSetVisibility = n;
        }
    }
    
    public void setVisibility(final int n) {
        this.internalSetVisibility(n, true);
    }
}
