// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.widget.FrameLayout$LayoutParams;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class HeaderCell extends FrameLayout
{
    private int height;
    private TextView textView;
    private SimpleTextView textView2;
    
    public HeaderCell(final Context context) {
        this(context, false, 21, 15, false);
    }
    
    public HeaderCell(final Context context, final int n) {
        this(context, false, n, 15, false);
    }
    
    public HeaderCell(final Context context, final boolean b, int n, final int n2, final boolean b2) {
        super(context);
        this.height = 40;
        (this.textView = new TextView(this.getContext())).setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n3 = 5;
        int n4;
        if (isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        textView.setGravity(n4 | 0x10);
        this.textView.setMinHeight(AndroidUtilities.dp((float)(this.height - n2)));
        if (b) {
            this.textView.setTextColor(Theme.getColor("dialogTextBlue2"));
        }
        else {
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
        }
        final TextView textView2 = this.textView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        final float n6 = (float)n;
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n5 | 0x30, n6, (float)n2, n6, 0.0f));
        if (b2) {
            (this.textView2 = new SimpleTextView(this.getContext())).setTextSize(13);
            final SimpleTextView textView3 = this.textView2;
            if (LocaleController.isRTL) {
                n = 3;
            }
            else {
                n = 5;
            }
            textView3.setGravity(n | 0x30);
            final SimpleTextView textView4 = this.textView2;
            n = n3;
            if (LocaleController.isRTL) {
                n = 3;
            }
            this.addView((View)textView4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n | 0x30, n6, 21.0f, n6, 0.0f));
        }
    }
    
    public SimpleTextView getTextView2() {
        return this.textView2;
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo$CollectionItemInfo collectionItemInfo = accessibilityNodeInfo.getCollectionItemInfo();
            if (collectionItemInfo != null) {
                accessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo$CollectionItemInfo.obtain(collectionItemInfo.getRowIndex(), collectionItemInfo.getRowSpan(), collectionItemInfo.getColumnIndex(), collectionItemInfo.getColumnSpan(), true));
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
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
    
    public void setHeight(final int n) {
        this.textView.setMinHeight(AndroidUtilities.dp((float)this.height) - ((FrameLayout$LayoutParams)this.textView.getLayoutParams()).topMargin);
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
    
    public void setText2(final String text) {
        final SimpleTextView textView2 = this.textView2;
        if (textView2 == null) {
            return;
        }
        textView2.setText(text);
    }
}
