package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.GraySectionCell */
public class GraySectionCell extends FrameLayout {
    private TextView righTextView;
    private TextView textView = new TextView(getContext());

    public GraySectionCell(Context context) {
        super(context);
        setBackgroundColor(Theme.getColor(Theme.key_graySection));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        TextView textView = this.textView;
        String str = Theme.key_graySectionText;
        textView.setTextColor(Theme.getColor(str));
        int i = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, 16.0f, 0.0f, 16.0f, 0.0f));
        this.righTextView = new TextView(getContext());
        this.righTextView.setTextSize(1, 14.0f);
        this.righTextView.setTextColor(Theme.getColor(str));
        this.righTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 16);
        textView = this.righTextView;
        if (LocaleController.isRTL) {
            i = 3;
        }
        addView(textView, LayoutHelper.createFrame(-2, -1.0f, i | 48, 16.0f, 0.0f, 16.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(32.0f), 1073741824));
    }

    public void setText(String str) {
        this.textView.setText(str);
        this.righTextView.setVisibility(8);
    }

    public void setText(String str, String str2, OnClickListener onClickListener) {
        this.textView.setText(str);
        this.righTextView.setText(str2);
        this.righTextView.setOnClickListener(onClickListener);
        this.righTextView.setVisibility(0);
    }
}
