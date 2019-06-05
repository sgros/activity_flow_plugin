// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.preference.Preference$OnPreferenceClickListener;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;
import android.view.View$OnClickListener;
import android.widget.TextView;
import org.mozilla.focus.utils.FirebaseHelper;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.widget.Switch;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.Preference;

public class TelemetrySwitchPreference extends Preference
{
    public TelemetrySwitchPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public TelemetrySwitchPreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        this.setPersistent(false);
    }
    
    protected void onBindView(final View view) {
        final Switch switch1 = (Switch)view.findViewById(2131296677);
        switch1.setChecked(TelemetryWrapper.isTelemetryEnabled(this.getContext()));
        switch1.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                TelemetryWrapper.setTelemetryEnabled(TelemetrySwitchPreference.this.getContext(), b);
                FirebaseHelper.bind(TelemetrySwitchPreference.this.getContext(), b);
            }
        });
        ((TextView)view.findViewById(2131296490)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                TelemetrySwitchPreference.this.getContext().startActivity(InfoActivity.getIntentFor(TelemetrySwitchPreference.this.getContext(), SupportUtils.getSumoURLForTopic(TelemetrySwitchPreference.this.getContext(), "usage-data"), TelemetrySwitchPreference.this.getTitle().toString()));
                TelemetryWrapper.settingsLearnMoreClickEvent(TelemetrySwitchPreference.this.getContext().getString(2131755331));
            }
        });
        this.setOnPreferenceClickListener((Preference$OnPreferenceClickListener)new Preference$OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                switch1.toggle();
                return true;
            }
        });
        this.setSummary((CharSequence)this.getContext().getResources().getString(2131755351, new Object[] { this.getContext().getResources().getString(2131755062), this.getContext().getResources().getString(2131755267) }));
        super.onBindView(view);
    }
}
