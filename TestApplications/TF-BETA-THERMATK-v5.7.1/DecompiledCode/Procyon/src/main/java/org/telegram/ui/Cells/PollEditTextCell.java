// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.TextWatcher;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.View$OnClickListener;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class PollEditTextCell extends FrameLayout
{
    private ImageView deleteImageView;
    private boolean needDivider;
    private boolean showNextButton;
    private EditTextBoldCursor textView;
    private SimpleTextView textView2;
    
    public PollEditTextCell(final Context context, final View$OnClickListener onClickListener) {
        super(context);
        (this.textView = new EditTextBoldCursor(context) {
            public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
                final InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
                if (PollEditTextCell.this.showNextButton) {
                    editorInfo.imeOptions &= 0xBFFFFFFF;
                }
                return onCreateInputConnection;
            }
        }).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.textView.setTextSize(1, 16.0f);
        final EditTextBoldCursor textView = this.textView;
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
        this.textView.setBackgroundDrawable((Drawable)null);
        this.textView.setPadding(0, AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f));
        final EditTextBoldCursor textView2 = this.textView;
        textView2.setImeOptions(textView2.getImeOptions() | 0x10000000);
        final EditTextBoldCursor textView3 = this.textView;
        textView3.setInputType(textView3.getInputType() | 0x4000);
        final EditTextBoldCursor textView4 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL && onClickListener != null) {
            n4 = 58.0f;
        }
        else {
            n4 = 21.0f;
        }
        float n5;
        if (!LocaleController.isRTL && onClickListener != null) {
            n5 = 58.0f;
        }
        else {
            n5 = 21.0f;
        }
        this.addView((View)textView4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n3 | 0x10, n4, 0.0f, n5, 0.0f));
        if (onClickListener != null) {
            (this.deleteImageView = new ImageView(context)).setFocusable(false);
            this.deleteImageView.setScaleType(ImageView$ScaleType.CENTER);
            this.deleteImageView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
            this.deleteImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff$Mode.MULTIPLY));
            this.deleteImageView.setImageResource(2131165652);
            this.deleteImageView.setOnClickListener(onClickListener);
            this.deleteImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText"), PorterDuff$Mode.MULTIPLY));
            this.deleteImageView.setContentDescription((CharSequence)LocaleController.getString("Delete", 2131559227));
            final ImageView deleteImageView = this.deleteImageView;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 3;
            }
            else {
                n6 = 5;
            }
            float n7;
            if (LocaleController.isRTL) {
                n7 = 3.0f;
            }
            else {
                n7 = 0.0f;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 0.0f;
            }
            else {
                n8 = 3.0f;
            }
            this.addView((View)deleteImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 50.0f, n6 | 0x30, n7, 0.0f, n8, 0.0f));
            (this.textView2 = new SimpleTextView(this.getContext())).setTextSize(13);
            final SimpleTextView textView5 = this.textView2;
            int n9;
            if (LocaleController.isRTL) {
                n9 = 3;
            }
            else {
                n9 = 5;
            }
            textView5.setGravity(n9 | 0x30);
            final SimpleTextView textView6 = this.textView2;
            int n10 = n;
            if (LocaleController.isRTL) {
                n10 = 3;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 20.0f;
            }
            else {
                n11 = 0.0f;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 0.0f;
            }
            else {
                n12 = 20.0f;
            }
            this.addView((View)textView6, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 24.0f, n10 | 0x30, n11, 43.0f, n12, 0.0f));
        }
    }
    
    public void addTextWatcher(final TextWatcher textWatcher) {
        this.textView.addTextChangedListener(textWatcher);
    }
    
    public void callOnDelete() {
        final ImageView deleteImageView = this.deleteImageView;
        if (deleteImageView == null) {
            return;
        }
        deleteImageView.callOnClick();
    }
    
    protected boolean drawDivider() {
        return true;
    }
    
    public String getText() {
        return this.textView.getText().toString();
    }
    
    public EditTextBoldCursor getTextView() {
        return this.textView;
    }
    
    public SimpleTextView getTextView2() {
        return this.textView2;
    }
    
    public int length() {
        return this.textView.length();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider && this.drawDivider()) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(int size, int n) {
        size = View$MeasureSpec.getSize(size);
        final ImageView deleteImageView = this.deleteImageView;
        if (deleteImageView != null) {
            deleteImageView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
            this.textView2.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        }
        final EditTextBoldCursor textView = this.textView;
        final int paddingLeft = this.getPaddingLeft();
        n = this.getPaddingRight();
        float n2;
        if (this.deleteImageView != null) {
            n2 = 79.0f;
        }
        else {
            n2 = 42.0f;
        }
        textView.measure(View$MeasureSpec.makeMeasureSpec(size - paddingLeft - n - AndroidUtilities.dp(n2), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        n = this.textView.getMeasuredHeight();
        this.setMeasuredDimension(size, Math.max(AndroidUtilities.dp(50.0f), this.textView.getMeasuredHeight()) + (this.needDivider ? 1 : 0));
        final SimpleTextView textView2 = this.textView2;
        if (textView2 != null) {
            float alpha;
            if (n >= AndroidUtilities.dp(52.0f)) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.0f;
            }
            textView2.setAlpha(alpha);
        }
    }
    
    public void setEnabled(final boolean enabled, final ArrayList<Animator> list) {
        this.setEnabled(enabled);
    }
    
    public void setShowNextButton(final boolean showNextButton) {
        this.showNextButton = showNextButton;
    }
    
    public void setText(final String text, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setText2(final String text) {
        this.textView2.setText(text);
    }
    
    public void setTextAndHint(final String text, final String hint, final boolean needDivider) {
        final ImageView deleteImageView = this.deleteImageView;
        if (deleteImageView != null) {
            deleteImageView.setTag((Object)null);
        }
        this.textView.setText((CharSequence)text);
        this.textView.setHint((CharSequence)hint);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
