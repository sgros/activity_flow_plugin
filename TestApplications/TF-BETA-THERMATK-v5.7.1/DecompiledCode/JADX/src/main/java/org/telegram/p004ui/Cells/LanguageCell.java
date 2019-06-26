package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.LanguageCell */
public class LanguageCell extends FrameLayout {
    private ImageView checkImage;
    private LocaleInfo currentLocale;
    private boolean isDialog;
    private boolean needDivider;
    private TextView textView;
    private TextView textView2;

    public LanguageCell(Context context, boolean z) {
        Context context2 = context;
        boolean z2 = z;
        super(context);
        setWillNotDraw(false);
        this.isDialog = z2;
        this.textView = new TextView(context2);
        this.textView.setTextColor(Theme.getColor(z2 ? Theme.key_dialogTextBlack : Theme.key_windowBackgroundWhiteBlackText));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TruncateAt.END);
        int i = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 71.0f : 23.0f, (float) (this.isDialog ? 4 : 7), LocaleController.isRTL ? 23.0f : 71.0f, 0.0f));
        this.textView2 = new TextView(context2);
        this.textView2.setTextColor(Theme.getColor(z2 ? Theme.key_dialogTextGray3 : Theme.key_windowBackgroundWhiteGrayText3));
        this.textView2.setTextSize(1, 13.0f);
        this.textView2.setLines(1);
        this.textView2.setMaxLines(1);
        this.textView2.setSingleLine(true);
        this.textView2.setEllipsize(TruncateAt.END);
        this.textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.textView2, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 71.0f : 23.0f, (float) (this.isDialog ? 25 : 29), LocaleController.isRTL ? 23.0f : 71.0f, 0.0f));
        this.checkImage = new ImageView(context2);
        this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), Mode.MULTIPLY));
        this.checkImage.setImageResource(C1067R.C1065drawable.sticker_added);
        ImageView imageView = this.checkImage;
        if (LocaleController.isRTL) {
            i = 3;
        }
        addView(imageView, LayoutHelper.createFrame(19, 14.0f, i | 16, 23.0f, 0.0f, 23.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(this.isDialog ? 50.0f : 54.0f) + this.needDivider, 1073741824));
    }

    public void setLanguage(LocaleInfo localeInfo, String str, boolean z) {
        CharSequence str2;
        TextView textView = this.textView;
        if (str2 == null) {
            str2 = localeInfo.name;
        }
        textView.setText(str2);
        this.textView2.setText(localeInfo.nameEnglish);
        this.currentLocale = localeInfo;
        this.needDivider = z;
    }

    public void setValue(String str, String str2) {
        this.textView.setText(str);
        this.textView2.setText(str2);
        this.checkImage.setVisibility(4);
        this.currentLocale = null;
        this.needDivider = false;
    }

    public LocaleInfo getCurrentLocale() {
        return this.currentLocale;
    }

    public void setLanguageSelected(boolean z) {
        this.checkImage.setVisibility(z ? 0 : 4);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : (float) AndroidUtilities.m26dp(20.0f), (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.m26dp(20.0f) : 0)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
}
