// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.support.annotation.StyleRes;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class TextViewCompatApi23
{
    public static void setTextAppearance(@NonNull final TextView textView, @StyleRes final int textAppearance) {
        textView.setTextAppearance(textAppearance);
    }
}
