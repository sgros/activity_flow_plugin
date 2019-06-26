package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.GroupCreateSectionCell */
public class GroupCreateSectionCell extends FrameLayout {
    private Drawable drawable = getResources().getDrawable(C1067R.C1065drawable.shadowdown);
    private TextView textView;

    public GroupCreateSectionCell(Context context) {
        super(context);
        setBackgroundColor(Theme.getColor(Theme.key_graySection));
        this.drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_groupcreate_sectionShadow), Mode.MULTIPLY));
        this.textView = new TextView(getContext());
        this.textView.setTextSize(1, 14.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setTextColor(Theme.getColor(Theme.key_groupcreate_sectionText));
        int i = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        TextView textView = this.textView;
        if (!LocaleController.isRTL) {
            i = 3;
        }
        addView(textView, LayoutHelper.createFrame(-1, -1.0f, i | 48, 16.0f, 0.0f, 16.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(40.0f), 1073741824));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        this.drawable.setBounds(0, getMeasuredHeight() - AndroidUtilities.m26dp(3.0f), getMeasuredWidth(), getMeasuredHeight());
        this.drawable.draw(canvas);
    }

    public void setText(String str) {
        this.textView.setText(str);
    }
}
