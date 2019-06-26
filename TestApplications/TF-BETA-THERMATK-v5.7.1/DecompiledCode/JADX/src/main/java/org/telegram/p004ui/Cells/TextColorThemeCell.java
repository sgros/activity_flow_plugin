package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.TextColorThemeCell */
public class TextColorThemeCell extends FrameLayout {
    private static Paint colorPaint;
    private float alpha = 1.0f;
    private int currentColor;
    private boolean needDivider;
    private TextView textView;

    public TextColorThemeCell(Context context) {
        super(context);
        if (colorPaint == null) {
            colorPaint = new Paint(1);
        }
        this.textView = new TextView(context);
        this.textView.setTextColor(-14606047);
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        int i = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.textView.setPadding(0, 0, 0, AndroidUtilities.m26dp(3.0f));
        TextView textView = this.textView;
        if (!LocaleController.isRTL) {
            i = 3;
        }
        int i2 = i | 48;
        i = 21;
        float f = (float) (LocaleController.isRTL ? 21 : 57);
        if (LocaleController.isRTL) {
            i = 57;
        }
        addView(textView, LayoutHelper.createFrame(-1, -1.0f, i2, f, 0.0f, (float) i, 0.0f));
    }

    public void setAlpha(float f) {
        this.alpha = f;
        invalidate();
    }

    public float getAlpha() {
        return this.alpha;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(50.0f) + this.needDivider, 1073741824));
    }

    public void setTextAndColor(CharSequence charSequence, int i) {
        this.textView.setText(charSequence);
        this.currentColor = i;
        boolean z = !this.needDivider && this.currentColor == 0;
        setWillNotDraw(z);
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int i = this.currentColor;
        if (i != 0) {
            colorPaint.setColor(i);
            colorPaint.setAlpha((int) (this.alpha * 255.0f));
            canvas.drawCircle((float) (!LocaleController.isRTL ? AndroidUtilities.m26dp(28.0f) : getMeasuredWidth() - AndroidUtilities.m26dp(28.0f)), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.m26dp(10.0f), colorPaint);
        }
    }
}