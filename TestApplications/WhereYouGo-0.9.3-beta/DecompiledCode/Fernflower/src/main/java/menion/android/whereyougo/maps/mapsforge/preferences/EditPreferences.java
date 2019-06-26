package menion.android.whereyougo.maps.mapsforge.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class EditPreferences extends PreferenceActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.addPreferencesFromResource(2131034112);
   }

   protected void onResume() {
      super.onResume();
      if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
         this.getWindow().addFlags(1024);
         this.getWindow().clearFlags(2048);
      } else {
         this.getWindow().clearFlags(1024);
         this.getWindow().addFlags(2048);
      }

   }
}
