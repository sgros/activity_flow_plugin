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
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class ChatUnreadCell extends FrameLayout
{
    private FrameLayout backgroundLayout;
    private ImageView imageView;
    private TextView textView;
    
    public ChatUnreadCell(final Context context) {
        super(context);
        (this.backgroundLayout = new FrameLayout(context)).setBackgroundResource(2131165688);
        this.backgroundLayout.getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_unreadMessagesStartBackground"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.backgroundLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 27.0f, 51, 0.0f, 7.0f, 0.0f, 0.0f));
        (this.imageView = new ImageView(context)).setImageResource(2131165415);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_unreadMessagesStartArrowIcon"), PorterDuff$Mode.MULTIPLY));
        this.imageView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        this.backgroundLayout.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 10.0f, 0.0f));
        (this.textView = new TextView(context)).setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setTextColor(Theme.getColor("chat_unreadMessagesStartText"));
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
    }
    
    public FrameLayout getBackgroundLayout() {
        return this.backgroundLayout;
    }
    
    public ImageView getImageView() {
        return this.imageView;
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
}
