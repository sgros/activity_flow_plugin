package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.SimpleTextView;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Cells.InviteTextCell */
public class InviteTextCell extends FrameLayout {
    private ImageView imageView;
    private SimpleTextView textView;

    public InviteTextCell(Context context) {
        super(context);
        this.textView = new SimpleTextView(context);
        this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.textView.setTextSize(17);
        this.textView.setGravity(LocaleController.isRTL ? 5 : 3);
        addView(this.textView);
        this.imageView = new ImageView(context);
        this.imageView.setScaleType(ScaleType.CENTER);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), Mode.MULTIPLY));
        addView(this.imageView);
    }

    public SimpleTextView getTextView() {
        return this.textView;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        i = MeasureSpec.getSize(i);
        int dp = AndroidUtilities.m26dp(72.0f);
        this.textView.measure(MeasureSpec.makeMeasureSpec(i - AndroidUtilities.m26dp(95.0f), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(20.0f), 1073741824));
        this.imageView.measure(MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE));
        setMeasuredDimension(i, AndroidUtilities.m26dp(72.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        i4 -= i2;
        i3 -= i;
        int textHeight = (i4 - this.textView.getTextHeight()) / 2;
        i = AndroidUtilities.m26dp(!LocaleController.isRTL ? 71.0f : 24.0f);
        SimpleTextView simpleTextView = this.textView;
        simpleTextView.layout(i, textHeight, simpleTextView.getMeasuredWidth() + i, this.textView.getMeasuredHeight() + textHeight);
        i4 = (i4 - this.imageView.getMeasuredHeight()) / 2;
        textHeight = !LocaleController.isRTL ? AndroidUtilities.m26dp(20.0f) : (i3 - this.imageView.getMeasuredWidth()) - AndroidUtilities.m26dp(20.0f);
        ImageView imageView = this.imageView;
        imageView.layout(textHeight, i4, imageView.getMeasuredWidth() + textHeight, this.imageView.getMeasuredHeight() + i4);
    }

    public void setTextColor(int i) {
        this.textView.setTextColor(i);
    }

    public void setTextAndIcon(String str, int i) {
        this.textView.setText(str);
        this.imageView.setImageResource(i);
    }
}
