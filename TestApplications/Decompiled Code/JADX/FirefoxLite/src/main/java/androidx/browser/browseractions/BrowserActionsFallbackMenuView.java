package androidx.browser.browseractions;

import android.content.Context;
import android.support.customtabs.C0025R;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class BrowserActionsFallbackMenuView extends LinearLayout {
    private final int mBrowserActionsMenuMaxWidthPx = getResources().getDimensionPixelOffset(C0025R.dimen.browser_actions_context_menu_max_width);
    private final int mBrowserActionsMenuMinPaddingPx = getResources().getDimensionPixelOffset(C0025R.dimen.browser_actions_context_menu_min_padding);

    public BrowserActionsFallbackMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(Math.min(getResources().getDisplayMetrics().widthPixels - (this.mBrowserActionsMenuMinPaddingPx * 2), this.mBrowserActionsMenuMaxWidthPx), 1073741824), i2);
    }
}
