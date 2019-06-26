package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.ColorSpanUnderline;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.StickerSetNameCell */
public class StickerSetNameCell extends FrameLayout {
    private ImageView buttonView;
    private boolean empty;
    private boolean isEmoji;
    private TextView textView;
    private TextView urlTextView;

    public StickerSetNameCell(Context context, boolean z) {
        super(context);
        this.isEmoji = z;
        this.textView = new TextView(context);
        TextView textView = this.textView;
        String str = Theme.key_chat_emojiPanelStickerSetName;
        textView.setTextColor(Theme.getColor(str));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setEllipsize(TruncateAt.END);
        this.textView.setSingleLine(true);
        addView(this.textView, LayoutHelper.createFrame(-2, -2.0f, 51, z ? 15.0f : 17.0f, 4.0f, 57.0f, 0.0f));
        this.urlTextView = new TextView(context);
        this.urlTextView.setTextColor(Theme.getColor(str));
        this.urlTextView.setTextSize(1, 12.0f);
        this.urlTextView.setEllipsize(TruncateAt.END);
        this.urlTextView.setSingleLine(true);
        this.urlTextView.setVisibility(4);
        addView(this.urlTextView, LayoutHelper.createFrame(-2, -2.0f, 53, 17.0f, 6.0f, 17.0f, 0.0f));
        this.buttonView = new ImageView(context);
        this.buttonView.setScaleType(ScaleType.CENTER);
        this.buttonView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelStickerSetNameIcon), Mode.MULTIPLY));
        addView(this.buttonView, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 0.0f, 16.0f, 0.0f));
    }

    public void setUrl(CharSequence charSequence, int i) {
        if (charSequence != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            try {
                spannableStringBuilder.setSpan(new ColorSpanUnderline(Theme.getColor(Theme.key_chat_emojiPanelStickerSetNameHighlight)), 0, i, 33);
                spannableStringBuilder.setSpan(new ColorSpanUnderline(Theme.getColor(Theme.key_chat_emojiPanelStickerSetName)), i, charSequence.length(), 33);
            } catch (Exception unused) {
            }
            this.urlTextView.setText(spannableStringBuilder);
            this.urlTextView.setVisibility(0);
            return;
        }
        this.urlTextView.setVisibility(8);
    }

    public void setText(CharSequence charSequence, int i) {
        setText(charSequence, i, 0, 0);
    }

    public void setText(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence == null) {
            this.empty = true;
            this.textView.setText("");
            this.buttonView.setVisibility(4);
            return;
        }
        if (i3 != 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            try {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_chat_emojiPanelStickerSetNameHighlight)), i2, i3 + i2, 33);
            } catch (Exception unused) {
            }
            this.textView.setText(spannableStringBuilder);
        } else {
            TextView textView = this.textView;
            textView.setText(Emoji.replaceEmoji(charSequence, textView.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(14.0f), false));
        }
        if (i != 0) {
            this.buttonView.setImageResource(i);
            this.buttonView.setVisibility(0);
            return;
        }
        this.buttonView.setVisibility(4);
    }

    public void setOnIconClickListener(OnClickListener onClickListener) {
        this.buttonView.setOnClickListener(onClickListener);
    }

    public void invalidate() {
        this.textView.invalidate();
        super.invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        if (this.empty) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(1, 1073741824));
        } else {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(this.isEmoji ? 28.0f : 24.0f), 1073741824));
        }
    }
}