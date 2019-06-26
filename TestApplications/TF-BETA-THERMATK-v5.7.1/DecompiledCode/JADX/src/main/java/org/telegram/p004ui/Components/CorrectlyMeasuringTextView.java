package org.telegram.p004ui.Components;

import android.content.Context;
import android.text.Layout;
import android.widget.TextView;

/* renamed from: org.telegram.ui.Components.CorrectlyMeasuringTextView */
public class CorrectlyMeasuringTextView extends TextView {
    public CorrectlyMeasuringTextView(Context context) {
        super(context);
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        try {
            Layout layout = getLayout();
            if (layout.getLineCount() > 1) {
                i2 = 0;
                for (int lineCount = layout.getLineCount() - 1; lineCount >= 0; lineCount--) {
                    i2 = Math.max(i2, Math.round(layout.getPaint().measureText(getText(), layout.getLineStart(lineCount), layout.getLineEnd(lineCount))));
                }
                super.onMeasure(Math.min((i2 + getPaddingLeft()) + getPaddingRight(), getMeasuredWidth()) | 1073741824, 1073741824 | getMeasuredHeight());
            }
        } catch (Exception unused) {
        }
    }
}
