// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLog;
import java.lang.reflect.Field;
import android.text.SpannableString;

public class SpannableStringLight extends SpannableString
{
    private static boolean fieldsAvailable;
    private static Field mSpanCountField;
    private static Field mSpanDataField;
    private static Field mSpansField;
    private int mSpanCountOverride;
    private int[] mSpanDataOverride;
    private Object[] mSpansOverride;
    private int num;
    
    public SpannableStringLight(final CharSequence charSequence) {
        super(charSequence);
        try {
            this.mSpansOverride = (Object[])SpannableStringLight.mSpansField.get(this);
            this.mSpanDataOverride = (int[])SpannableStringLight.mSpanDataField.get(this);
            this.mSpanCountOverride = (int)SpannableStringLight.mSpanCountField.get(this);
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
    
    public static boolean isFieldsAvailable() {
        final boolean fieldsAvailable = SpannableStringLight.fieldsAvailable;
        boolean b = true;
        if (!fieldsAvailable && SpannableStringLight.mSpansField == null) {
            try {
                (SpannableStringLight.mSpansField = SpannableString.class.getSuperclass().getDeclaredField("mSpans")).setAccessible(true);
                (SpannableStringLight.mSpanDataField = SpannableString.class.getSuperclass().getDeclaredField("mSpanData")).setAccessible(true);
                (SpannableStringLight.mSpanCountField = SpannableString.class.getSuperclass().getDeclaredField("mSpanCount")).setAccessible(true);
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            SpannableStringLight.fieldsAvailable = true;
        }
        if (SpannableStringLight.mSpansField == null) {
            b = false;
        }
        return b;
    }
    
    public void removeSpan(final Object o) {
        super.removeSpan(o);
    }
    
    public void setSpanLight(final Object o, final int n, final int n2, final int n3) {
        final Object[] mSpansOverride = this.mSpansOverride;
        final int num = this.num;
        mSpansOverride[num] = o;
        final int[] mSpanDataOverride = this.mSpanDataOverride;
        mSpanDataOverride[num * 3] = n;
        mSpanDataOverride[num * 3 + 1] = n2;
        mSpanDataOverride[num * 3 + 2] = n3;
        this.num = num + 1;
    }
    
    public void setSpansCount(int mSpanCountOverride) {
        final int mSpanCountOverride2 = this.mSpanCountOverride;
        mSpanCountOverride += mSpanCountOverride2;
        this.mSpansOverride = new Object[mSpanCountOverride];
        this.mSpanDataOverride = new int[mSpanCountOverride * 3];
        this.num = mSpanCountOverride2;
        this.mSpanCountOverride = mSpanCountOverride;
        try {
            SpannableStringLight.mSpansField.set(this, this.mSpansOverride);
            SpannableStringLight.mSpanDataField.set(this, this.mSpanDataOverride);
            SpannableStringLight.mSpanCountField.set(this, this.mSpanCountOverride);
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
}
