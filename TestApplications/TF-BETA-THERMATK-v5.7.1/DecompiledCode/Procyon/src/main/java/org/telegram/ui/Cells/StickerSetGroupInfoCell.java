// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;

public class StickerSetGroupInfoCell extends LinearLayout
{
    private TextView addButton;
    private boolean isLast;
    
    public StickerSetGroupInfoCell(final Context context) {
        super(context);
        this.setOrientation(1);
        final TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor("chat_emojiPanelTrendingDescription"));
        textView.setTextSize(1, 14.0f);
        textView.setText((CharSequence)LocaleController.getString("GroupStickersInfo", 2131559616));
        this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 51, 17, 4, 17, 0));
        (this.addButton = new TextView(context)).setPadding(AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(17.0f), 0);
        this.addButton.setGravity(17);
        this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.addButton.setTextSize(1, 14.0f);
        this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addButton.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        this.addButton.setText((CharSequence)LocaleController.getString("ChooseStickerSet", 2131559092).toUpperCase());
        this.addView((View)this.addButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 28, 51, 17, 10, 14, 8));
    }
    
    protected void onMeasure(int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), n2);
        if (this.isLast) {
            final View view = (View)this.getParent();
            if (view != null) {
                n = view.getMeasuredHeight() - view.getPaddingBottom() - view.getPaddingTop() - AndroidUtilities.dp(24.0f);
                if (this.getMeasuredHeight() < n) {
                    this.setMeasuredDimension(this.getMeasuredWidth(), n);
                }
            }
        }
    }
    
    public void setAddOnClickListener(final View$OnClickListener onClickListener) {
        this.addButton.setOnClickListener(onClickListener);
    }
    
    public void setIsLast(final boolean isLast) {
        this.isLast = isLast;
        this.requestLayout();
    }
}
