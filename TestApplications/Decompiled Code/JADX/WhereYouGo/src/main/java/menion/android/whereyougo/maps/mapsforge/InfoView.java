package menion.android.whereyougo.maps.mapsforge;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

public class InfoView extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_asset/info.xml");
        setContentView(webView);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
            getWindow().addFlags(1024);
            getWindow().clearFlags(2048);
            return;
        }
        getWindow().clearFlags(1024);
        getWindow().addFlags(2048);
    }
}
