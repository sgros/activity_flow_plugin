// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.widget.TextView;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class TextViewCompatJb
{
    static int getMaxLines(final TextView textView) {
        return textView.getMaxLines();
    }
    
    static int getMinLines(final TextView textView) {
        return textView.getMinLines();
    }
}
