package menion.android.whereyougo.maps.mapsforge.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import menion.android.whereyougo.C0254R;

public class EditPreferences extends PreferenceActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(C0254R.xml.mapsforge_preferences);
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
