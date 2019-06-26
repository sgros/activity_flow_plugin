package menion.android.whereyougo.maps.mapsforge;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

public class InfoView extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      WebView var2 = new WebView(this);
      var2.loadUrl("file:///android_asset/info.xml");
      this.setContentView(var2);
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
