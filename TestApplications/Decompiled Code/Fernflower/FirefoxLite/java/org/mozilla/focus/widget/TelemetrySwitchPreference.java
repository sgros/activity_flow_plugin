package org.mozilla.focus.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.focus.utils.SupportUtils;

public class TelemetrySwitchPreference extends Preference {
   public TelemetrySwitchPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public TelemetrySwitchPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      this.setPersistent(false);
   }

   protected void onBindView(View var1) {
      final Switch var2 = (Switch)var1.findViewById(2131296677);
      var2.setChecked(TelemetryWrapper.isTelemetryEnabled(this.getContext()));
      var2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            TelemetryWrapper.setTelemetryEnabled(TelemetrySwitchPreference.this.getContext(), var2);
            FirebaseHelper.bind(TelemetrySwitchPreference.this.getContext(), var2);
         }
      });
      ((TextView)var1.findViewById(2131296490)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            String var2 = SupportUtils.getSumoURLForTopic(TelemetrySwitchPreference.this.getContext(), "usage-data");
            String var3 = TelemetrySwitchPreference.this.getTitle().toString();
            Intent var4 = InfoActivity.getIntentFor(TelemetrySwitchPreference.this.getContext(), var2, var3);
            TelemetrySwitchPreference.this.getContext().startActivity(var4);
            TelemetryWrapper.settingsLearnMoreClickEvent(TelemetrySwitchPreference.this.getContext().getString(2131755331));
         }
      });
      this.setOnPreferenceClickListener(new OnPreferenceClickListener() {
         public boolean onPreferenceClick(Preference var1) {
            var2.toggle();
            return true;
         }
      });
      String var4 = this.getContext().getResources().getString(2131755062);
      String var3 = this.getContext().getResources().getString(2131755267);
      this.setSummary(this.getContext().getResources().getString(2131755351, new Object[]{var4, var3}));
      super.onBindView(var1);
   }
}
