// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.view.View;

public class ShadowSectionCell extends View
{
    private int size;
    
    public ShadowSectionCell(final Context context) {
        this(context, 12);
    }
    
    public ShadowSectionCell(final Context context, final int size) {
        super(context);
        this.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
        this.size = size;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.size), 1073741824));
    }
}
