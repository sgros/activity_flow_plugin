// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityRecord;
import android.view.View;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityRecordCompatJellyBean
{
    public static void setSource(final Object o, final View view, final int n) {
        ((AccessibilityRecord)o).setSource(view, n);
    }
}
