// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.preference.Preference;

public class AboutPreference extends Preference
{
    public AboutPreference(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public AboutPreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setTitle((CharSequence)this.getContext().getResources().getString(2131755341));
    }
}
