// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class LetterSectionCell extends FrameLayout
{
    private TextView textView;
    
    public LetterSectionCell(final Context context) {
        super(context);
        this.setLayoutParams(new ViewGroup$LayoutParams(AndroidUtilities.dp(54.0f), AndroidUtilities.dp(64.0f)));
        (this.textView = new TextView(this.getContext())).setTextSize(1, 22.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.textView.setGravity(17);
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
    }
    
    public void setCellHeight(final int n) {
        this.setLayoutParams(new ViewGroup$LayoutParams(AndroidUtilities.dp(54.0f), n));
    }
    
    public void setLetter(final String s) {
        this.textView.setText((CharSequence)s.toUpperCase());
    }
}
