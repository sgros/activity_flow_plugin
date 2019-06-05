package org.mozilla.focus.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import org.mozilla.rocket.C0769R;

public class AboutPreference extends Preference {
    public AboutPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setTitle(getContext().getResources().getString(C0769R.string.preference_about));
    }

    public AboutPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
