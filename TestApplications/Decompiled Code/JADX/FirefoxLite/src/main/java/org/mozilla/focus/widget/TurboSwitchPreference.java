package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.C0769R;

public class TurboSwitchPreference extends Preference {

    /* renamed from: org.mozilla.focus.widget.TurboSwitchPreference$1 */
    class C05771 implements OnCheckedChangeListener {
        C05771() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Settings.getInstance(TurboSwitchPreference.this.getContext()).setTurboMode(z);
        }
    }

    /* renamed from: org.mozilla.focus.widget.TurboSwitchPreference$2 */
    class C05782 implements OnClickListener {
        C05782() {
        }

        public void onClick(View view) {
            TurboSwitchPreference.this.getContext().startActivity(InfoActivity.getIntentFor(TurboSwitchPreference.this.getContext(), SupportUtils.getSumoURLForTopic(TurboSwitchPreference.this.getContext(), "turbo"), TurboSwitchPreference.this.getTitle().toString()));
            TelemetryWrapper.settingsLearnMoreClickEvent(TurboSwitchPreference.this.getContext().getString(C0769R.string.pref_key_turbo_mode));
        }
    }

    public TurboSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public TurboSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setWidgetLayoutResource(C0769R.layout.preference_turbo);
        setPersistent(false);
    }

    /* Access modifiers changed, original: protected */
    public void onBindView(View view) {
        super.onBindView(view);
        final Switch switchR = (Switch) view.findViewById(C0427R.C0426id.switch_widget);
        switchR.setChecked(Settings.getInstance(getContext()).shouldUseTurboMode());
        switchR.setOnCheckedChangeListener(new C05771());
        TextView textView = (TextView) view.findViewById(16908304);
        TypedValue typedValue = new TypedValue();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(typedValue.data, new int[]{16842907});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        textView.setTextColor(color);
        textView.setOnClickListener(new C05782());
        setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                switchR.toggle();
                return true;
            }
        });
    }
}
