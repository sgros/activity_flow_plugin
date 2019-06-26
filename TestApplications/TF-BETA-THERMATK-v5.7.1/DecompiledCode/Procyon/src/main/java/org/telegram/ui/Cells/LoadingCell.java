// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Components.RadialProgressView;
import android.widget.FrameLayout;

public class LoadingCell extends FrameLayout
{
    private int height;
    private RadialProgressView progressBar;
    
    public LoadingCell(final Context context) {
        this(context, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(54.0f));
    }
    
    public LoadingCell(final Context context, final int size, final int height) {
        super(context);
        this.height = height;
        (this.progressBar = new RadialProgressView(context)).setSize(size);
        this.addView((View)this.progressBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(this.height, 1073741824));
    }
}
