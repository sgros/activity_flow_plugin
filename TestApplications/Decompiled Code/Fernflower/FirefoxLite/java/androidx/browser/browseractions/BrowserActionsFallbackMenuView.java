package androidx.browser.browseractions;

import android.content.Context;
import android.support.customtabs.R;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class BrowserActionsFallbackMenuView extends LinearLayout {
   private final int mBrowserActionsMenuMaxWidthPx;
   private final int mBrowserActionsMenuMinPaddingPx;

   public BrowserActionsFallbackMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mBrowserActionsMenuMinPaddingPx = this.getResources().getDimensionPixelOffset(R.dimen.browser_actions_context_menu_min_padding);
      this.mBrowserActionsMenuMaxWidthPx = this.getResources().getDimensionPixelOffset(R.dimen.browser_actions_context_menu_max_width);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(Math.min(this.getResources().getDisplayMetrics().widthPixels - this.mBrowserActionsMenuMinPaddingPx * 2, this.mBrowserActionsMenuMaxWidthPx), 1073741824), var2);
   }
}
