// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.ui.Components.RadialProgressView;
import android.widget.FrameLayout;

public class ChatLoadingCell extends FrameLayout
{
    private FrameLayout frameLayout;
    private RadialProgressView progressBar;
    
    public ChatLoadingCell(final Context context) {
        super(context);
        (this.frameLayout = new FrameLayout(context)).setBackgroundResource(2131165872);
        this.frameLayout.getBackground().setColorFilter((ColorFilter)Theme.colorFilter);
        this.addView((View)this.frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 17));
        (this.progressBar = new RadialProgressView(context)).setSize(AndroidUtilities.dp(28.0f));
        this.progressBar.setProgressColor(Theme.getColor("chat_serviceText"));
        this.frameLayout.addView((View)this.progressBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(32, 32, 17));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
    }
    
    public void setProgressVisible(final boolean b) {
        final FrameLayout frameLayout = this.frameLayout;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        frameLayout.setVisibility(visibility);
    }
}
