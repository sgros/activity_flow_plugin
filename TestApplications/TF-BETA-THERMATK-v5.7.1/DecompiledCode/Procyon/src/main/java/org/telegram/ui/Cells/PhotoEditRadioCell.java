// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.RadioButton;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import android.content.Context;
import android.widget.LinearLayout;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.widget.FrameLayout;

public class PhotoEditRadioCell extends FrameLayout
{
    private int currentColor;
    private int currentType;
    private TextView nameTextView;
    private View$OnClickListener onClickListener;
    private LinearLayout tintButtonsContainer;
    private final int[] tintHighlighsColors;
    private final int[] tintShadowColors;
    
    public PhotoEditRadioCell(final Context context) {
        super(context);
        this.tintShadowColors = new int[] { 0, -45747, -753630, -13056, -8269183, -9321002, -16747844, -10080879 };
        this.tintHighlighsColors = new int[] { 0, -1076602, -1388894, -859780, -5968466, -7742235, -13726776, -3303195 };
        (this.nameTextView = new TextView(context)).setGravity(5);
        this.nameTextView.setTextColor(-1);
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        (this.tintButtonsContainer = new LinearLayout(context)).setOrientation(0);
        for (int i = 0; i < this.tintShadowColors.length; ++i) {
            final RadioButton radioButton = new RadioButton(context);
            radioButton.setSize(AndroidUtilities.dp(20.0f));
            radioButton.setTag((Object)i);
            this.tintButtonsContainer.addView((View)radioButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f / this.tintShadowColors.length));
            radioButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final RadioButton radioButton = (RadioButton)view;
                    if (PhotoEditRadioCell.this.currentType == 0) {
                        final PhotoEditRadioCell this$0 = PhotoEditRadioCell.this;
                        this$0.currentColor = this$0.tintShadowColors[(int)radioButton.getTag()];
                    }
                    else {
                        final PhotoEditRadioCell this$2 = PhotoEditRadioCell.this;
                        this$2.currentColor = this$2.tintHighlighsColors[(int)radioButton.getTag()];
                    }
                    PhotoEditRadioCell.this.updateSelectedTintButton(true);
                    PhotoEditRadioCell.this.onClickListener.onClick((View)PhotoEditRadioCell.this);
                }
            });
        }
        this.addView((View)this.tintButtonsContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f, 51, 96.0f, 0.0f, 24.0f, 0.0f));
    }
    
    private void updateSelectedTintButton(final boolean b) {
        for (int childCount = this.tintButtonsContainer.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.tintButtonsContainer.getChildAt(i);
            if (child instanceof RadioButton) {
                final RadioButton radioButton = (RadioButton)child;
                final int intValue = (int)radioButton.getTag();
                int n;
                if (this.currentType == 0) {
                    n = this.tintShadowColors[intValue];
                }
                else {
                    n = this.tintHighlighsColors[intValue];
                }
                radioButton.setChecked(this.currentColor == n, b);
                int n2 = -1;
                int n3;
                if (intValue == 0) {
                    n3 = -1;
                }
                else if (this.currentType == 0) {
                    n3 = this.tintShadowColors[intValue];
                }
                else {
                    n3 = this.tintHighlighsColors[intValue];
                }
                if (intValue != 0) {
                    if (this.currentType == 0) {
                        n2 = this.tintShadowColors[intValue];
                    }
                    else {
                        n2 = this.tintHighlighsColors[intValue];
                    }
                }
                radioButton.setColor(n3, n2);
            }
        }
    }
    
    public int getCurrentColor() {
        return this.currentColor;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }
    
    public void setIconAndTextAndValue(final String s, final int currentType, final int currentColor) {
        this.currentType = currentType;
        this.currentColor = currentColor;
        final TextView nameTextView = this.nameTextView;
        final StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, 1).toUpperCase());
        sb.append(s.substring(1).toLowerCase());
        nameTextView.setText((CharSequence)sb.toString());
        this.updateSelectedTintButton(false);
    }
    
    public void setOnClickListener(final View$OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
