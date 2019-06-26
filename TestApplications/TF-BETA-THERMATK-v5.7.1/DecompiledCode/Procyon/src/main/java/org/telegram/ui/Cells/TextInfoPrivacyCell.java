// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.text.method.LinkMovementMethod;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class TextInfoPrivacyCell extends FrameLayout
{
    private int bottomPadding;
    private int fixedSize;
    private String linkTextColorKey;
    private TextView textView;
    
    public TextInfoPrivacyCell(final Context context) {
        this(context, 21);
    }
    
    public TextInfoPrivacyCell(final Context context, final int n) {
        super(context);
        this.linkTextColorKey = "windowBackgroundWhiteLinkText";
        this.bottomPadding = 17;
        (this.textView = new TextView(context)).setTextSize(1, 14.0f);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        this.textView.setPadding(0, AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(17.0f));
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.textView.setLinkTextColor(Theme.getColor(this.linkTextColorKey));
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n2;
        }
        else {
            n3 = 3;
        }
        final float n4 = (float)n;
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n3 | 0x30, n4, 0.0f, n4, 0.0f));
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    public int length() {
        return this.textView.length();
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.fixedSize != 0) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.fixedSize), 1073741824));
        }
        else {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
    }
    
    public void setBottomPadding(final int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }
    
    public void setEnabled(final boolean b, final ArrayList<Animator> list) {
        float alpha = 1.0f;
        if (list != null) {
            final TextView textView = this.textView;
            if (!b) {
                alpha = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { alpha }));
        }
        else {
            final TextView textView2 = this.textView;
            if (!b) {
                alpha = 0.5f;
            }
            textView2.setAlpha(alpha);
        }
    }
    
    public void setFixedSize(final int fixedSize) {
        this.fixedSize = fixedSize;
    }
    
    public void setLinkTextColorKey(final String linkTextColorKey) {
        this.linkTextColorKey = linkTextColorKey;
    }
    
    public void setText(final CharSequence text) {
        if (text == null) {
            this.textView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        }
        else {
            this.textView.setPadding(0, AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp((float)this.bottomPadding));
        }
        this.textView.setText(text);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTextColor(final String tag) {
        this.textView.setTextColor(Theme.getColor(tag));
        this.textView.setTag((Object)tag);
    }
}
