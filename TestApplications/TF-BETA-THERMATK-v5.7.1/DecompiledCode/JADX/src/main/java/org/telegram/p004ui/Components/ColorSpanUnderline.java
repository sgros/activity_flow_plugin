package org.telegram.p004ui.Components;

import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/* renamed from: org.telegram.ui.Components.ColorSpanUnderline */
public class ColorSpanUnderline extends ForegroundColorSpan {
    public ColorSpanUnderline(int i) {
        super(i);
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(true);
    }
}
