package org.mozilla.focus.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;

public class TurboSwitchPreference extends Preference {
   public TurboSwitchPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public TurboSwitchPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      this.setWidgetLayoutResource(2131493013);
      this.setPersistent(false);
   }

   protected void onBindView(View var1) {
      super.onBindView(var1);
      final Switch var2 = (Switch)var1.findViewById(2131296677);
      var2.setChecked(Settings.getInstance(this.getContext()).shouldUseTurboMode());
      var2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            Settings.getInstance(TurboSwitchPreference.this.getContext()).setTurboMode(var2);
         }
      });
      TextView var5 = (TextView)var1.findViewById(16908304);
      TypedValue var3 = new TypedValue();
      TypedArray var6 = this.getContext().obtainStyledAttributes(var3.data, new int[]{16842907});
      int var4 = var6.getColor(0, 0);
      var6.recycle();
      var5.setTextColor(var4);
      var5.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            String var2 = SupportUtils.getSumoURLForTopic(TurboSwitchPreference.this.getContext(), "turbo");
            String var3 = TurboSwitchPreference.this.getTitle().toString();
            Intent var4 = InfoActivity.getIntentFor(TurboSwitchPreference.this.getContext(), var2, var3);
            TurboSwitchPreference.this.getContext().startActivity(var4);
            TelemetryWrapper.settingsLearnMoreClickEvent(TurboSwitchPreference.this.getContext().getString(2131755332));
         }
      });
      this.setOnPreferenceClickListener(new OnPreferenceClickListener() {
         public boolean onPreferenceClick(Preference var1) {
            var2.toggle();
            return true;
         }
      });
   }
}
