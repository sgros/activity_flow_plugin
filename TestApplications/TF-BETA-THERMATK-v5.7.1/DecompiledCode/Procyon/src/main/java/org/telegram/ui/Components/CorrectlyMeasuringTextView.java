// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.Layout;
import android.content.Context;
import android.widget.TextView;

public class CorrectlyMeasuringTextView extends TextView
{
    public CorrectlyMeasuringTextView(final Context context) {
        super(context);
    }
    
    public void onMeasure(int max, int i) {
        super.onMeasure(max, i);
        try {
            final Layout layout = this.getLayout();
            if (layout.getLineCount() <= 1) {
                return;
            }
            max = 0;
            for (i = layout.getLineCount() - 1; i >= 0; --i) {
                max = Math.max(max, Math.round(layout.getPaint().measureText(this.getText(), layout.getLineStart(i), layout.getLineEnd(i))));
            }
            super.onMeasure(Math.min(max + this.getPaddingLeft() + this.getPaddingRight(), this.getMeasuredWidth()) | 0x40000000, 0x40000000 | this.getMeasuredHeight());
        }
        catch (Exception ex) {}
    }
}
