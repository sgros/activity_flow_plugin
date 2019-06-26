// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.ui.Components.ColorSpanUnderline;
import org.telegram.messenger.Emoji;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class StickerSetNameCell extends FrameLayout
{
    private ImageView buttonView;
    private boolean empty;
    private boolean isEmoji;
    private TextView textView;
    private TextView urlTextView;
    
    public StickerSetNameCell(final Context context, final boolean isEmoji) {
        super(context);
        this.isEmoji = isEmoji;
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("chat_emojiPanelStickerSetName"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        this.textView.setSingleLine(true);
        final TextView textView = this.textView;
        float n;
        if (isEmoji) {
            n = 15.0f;
        }
        else {
            n = 17.0f;
        }
        this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, n, 4.0f, 57.0f, 0.0f));
        (this.urlTextView = new TextView(context)).setTextColor(Theme.getColor("chat_emojiPanelStickerSetName"));
        this.urlTextView.setTextSize(1, 12.0f);
        this.urlTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.urlTextView.setSingleLine(true);
        this.urlTextView.setVisibility(4);
        this.addView((View)this.urlTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 53, 17.0f, 6.0f, 17.0f, 0.0f));
        (this.buttonView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.buttonView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelStickerSetNameIcon"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.buttonView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 0.0f, 16.0f, 0.0f));
    }
    
    public void invalidate() {
        this.textView.invalidate();
        super.invalidate();
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        if (this.empty) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824), View$MeasureSpec.makeMeasureSpec(1, 1073741824));
        }
        else {
            measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
            float n2;
            if (this.isEmoji) {
                n2 = 28.0f;
            }
            else {
                n2 = 24.0f;
            }
            super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2), 1073741824));
        }
    }
    
    public void setOnIconClickListener(final View$OnClickListener onClickListener) {
        this.buttonView.setOnClickListener(onClickListener);
    }
    
    public void setText(final CharSequence charSequence, final int n) {
        this.setText(charSequence, n, 0, 0);
    }
    
    public void setText(CharSequence text, final int imageResource, final int n, final int n2) {
        if (text == null) {
            this.empty = true;
            this.textView.setText((CharSequence)"");
            this.buttonView.setVisibility(4);
            return;
        }
        Label_0082: {
            if (n2 == 0) {
                break Label_0082;
            }
            text = (CharSequence)new SpannableStringBuilder(text);
            while (true) {
                try {
                    ((SpannableStringBuilder)text).setSpan((Object)new ForegroundColorSpan(Theme.getColor("chat_emojiPanelStickerSetNameHighlight")), n, n2 + n, 33);
                    this.textView.setText(text);
                    while (true) {
                        if (imageResource != 0) {
                            this.buttonView.setImageResource(imageResource);
                            this.buttonView.setVisibility(0);
                        }
                        else {
                            this.buttonView.setVisibility(4);
                        }
                        return;
                        final TextView textView = this.textView;
                        textView.setText(Emoji.replaceEmoji(text, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                        continue;
                    }
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public void setUrl(final CharSequence charSequence, final int n) {
        Label_0087: {
            if (charSequence == null) {
                break Label_0087;
            }
            final SpannableStringBuilder text = new SpannableStringBuilder(charSequence);
            while (true) {
                try {
                    text.setSpan((Object)new ColorSpanUnderline(Theme.getColor("chat_emojiPanelStickerSetNameHighlight")), 0, n, 33);
                    text.setSpan((Object)new ColorSpanUnderline(Theme.getColor("chat_emojiPanelStickerSetName")), n, charSequence.length(), 33);
                    this.urlTextView.setText((CharSequence)text);
                    this.urlTextView.setVisibility(0);
                    return;
                    this.urlTextView.setVisibility(8);
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
}
