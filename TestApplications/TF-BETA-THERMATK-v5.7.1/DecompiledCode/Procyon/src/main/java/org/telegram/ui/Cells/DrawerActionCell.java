// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.FileLog;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class DrawerActionCell extends FrameLayout
{
    private TextView textView;
    
    public DrawerActionCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("chats_menuItemText"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(19);
        this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(29.0f));
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 19.0f, 0.0f, 16.0f, 0.0f));
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
    }
    
    public void setTextAndIcon(final String text, final int n) {
        try {
            this.textView.setText((CharSequence)text);
            final Drawable mutate = this.getResources().getDrawable(n).mutate();
            if (mutate != null) {
                mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_menuItemIcon"), PorterDuff$Mode.MULTIPLY));
            }
            this.textView.setCompoundDrawablesWithIntrinsicBounds(mutate, (Drawable)null, (Drawable)null, (Drawable)null);
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
}
