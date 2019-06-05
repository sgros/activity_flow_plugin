// 
// Decompiled by Procyon v0.5.34
// 

package androidx.browser.browseractions;

import android.view.View$MeasureSpec;
import android.support.customtabs.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class BrowserActionsFallbackMenuView extends LinearLayout
{
    private final int mBrowserActionsMenuMaxWidthPx;
    private final int mBrowserActionsMenuMinPaddingPx;
    
    public BrowserActionsFallbackMenuView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mBrowserActionsMenuMinPaddingPx = this.getResources().getDimensionPixelOffset(R.dimen.browser_actions_context_menu_min_padding);
        this.mBrowserActionsMenuMaxWidthPx = this.getResources().getDimensionPixelOffset(R.dimen.browser_actions_context_menu_max_width);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(Math.min(this.getResources().getDisplayMetrics().widthPixels - this.mBrowserActionsMenuMinPaddingPx * 2, this.mBrowserActionsMenuMaxWidthPx), 1073741824), n2);
    }
}
