// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class GroupCreateSectionCell extends FrameLayout
{
    private Drawable drawable;
    private TextView textView;
    
    public GroupCreateSectionCell(final Context context) {
        super(context);
        this.setBackgroundColor(Theme.getColor("graySection"));
        (this.drawable = this.getResources().getDrawable(2131165817)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("groupcreate_sectionShadow"), PorterDuff$Mode.MULTIPLY));
        (this.textView = new TextView(this.getContext())).setTextSize(1, 14.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setTextColor(Theme.getColor("groupcreate_sectionText"));
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x10);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, 16.0f, 0.0f, 16.0f, 0.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        this.drawable.setBounds(0, this.getMeasuredHeight() - AndroidUtilities.dp(3.0f), this.getMeasuredWidth(), this.getMeasuredHeight());
        this.drawable.draw(canvas);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
}
