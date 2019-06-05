package org.mozilla.focus.widget;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.C0769R;

public class TelemetrySwitchPreference extends Preference {

    /* renamed from: org.mozilla.focus.widget.TelemetrySwitchPreference$1 */
    class C05741 implements OnCheckedChangeListener {
        C05741() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            TelemetryWrapper.setTelemetryEnabled(TelemetrySwitchPreference.this.getContext(), z);
            FirebaseHelper.bind(TelemetrySwitchPreference.this.getContext(), z);
        }
    }

    /* renamed from: org.mozilla.focus.widget.TelemetrySwitchPreference$2 */
    class C05752 implements OnClickListener {
        C05752() {
        }

        public void onClick(View view) {
            TelemetrySwitchPreference.this.getContext().startActivity(InfoActivity.getIntentFor(TelemetrySwitchPreference.this.getContext(), SupportUtils.getSumoURLForTopic(TelemetrySwitchPreference.this.getContext(), "usage-data"), TelemetrySwitchPreference.this.getTitle().toString()));
            TelemetryWrapper.settingsLearnMoreClickEvent(TelemetrySwitchPreference.this.getContext().getString(C0769R.string.pref_key_telemetry));
        }
    }

    public TelemetrySwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public TelemetrySwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setPersistent(false);
    }

    /* Access modifiers changed, original: protected */
    public void onBindView(View view) {
        final Switch switchR = (Switch) view.findViewById(C0427R.C0426id.switch_widget);
        switchR.setChecked(TelemetryWrapper.isTelemetryEnabled(getContext()));
        switchR.setOnCheckedChangeListener(new C05741());
        ((TextView) view.findViewById(C0427R.C0426id.learnMore)).setOnClickListener(new C05752());
        setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                switchR.toggle();
                return true;
            }
        });
        String string = getContext().getResources().getString(C0769R.string.app_name);
        String string2 = getContext().getResources().getString(C0769R.string.mozilla);
        setSummary(getContext().getResources().getString(C0769R.string.preference_mozilla_telemetry_summary, new Object[]{string, string2}));
        super.onBindView(view);
    }
}
