package org.mozilla.rocket.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.components.ComponentToggleService;

public class ConfigActivity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startService(new Intent(getApplicationContext(), ComponentToggleService.class));
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
        finishAndRemoveTask();
    }
}
