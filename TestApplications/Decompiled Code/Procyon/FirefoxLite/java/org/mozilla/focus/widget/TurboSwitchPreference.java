// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.content.res.TypedArray;
import android.preference.Preference$OnPreferenceClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;
import android.view.View$OnClickListener;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import org.mozilla.focus.utils.Settings;
import android.widget.Switch;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.Preference;

public class TurboSwitchPreference extends Preference
{
    public TurboSwitchPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public TurboSwitchPreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        this.setWidgetLayoutResource(2131493013);
        this.setPersistent(false);
    }
    
    protected void onBindView(final View view) {
        super.onBindView(view);
        final Switch switch1 = (Switch)view.findViewById(2131296677);
        switch1.setChecked(Settings.getInstance(this.getContext()).shouldUseTurboMode());
        switch1.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean turboMode) {
                Settings.getInstance(TurboSwitchPreference.this.getContext()).setTurboMode(turboMode);
            }
        });
        final TextView textView = (TextView)view.findViewById(16908304);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(new TypedValue().data, new int[] { 16842907 });
        final int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        textView.setTextColor(color);
        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                TurboSwitchPreference.this.getContext().startActivity(InfoActivity.getIntentFor(TurboSwitchPreference.this.getContext(), SupportUtils.getSumoURLForTopic(TurboSwitchPreference.this.getContext(), "turbo"), TurboSwitchPreference.this.getTitle().toString()));
                TelemetryWrapper.settingsLearnMoreClickEvent(TurboSwitchPreference.this.getContext().getString(2131755332));
            }
        });
        this.setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                switch1.toggle();
                return true;
            }
        });
    }
}
