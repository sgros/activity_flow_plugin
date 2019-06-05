package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;

public class SplashActivity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        Intent intent;
        super.onCreate(bundle);
        PrefManager prefManager = new PrefManager(this);
        if (PrefManager.isFirstTimeLaunch()) {
            intent = new Intent(this, TutorialActivity.class);
            PrefManager.setFirstTimeLaunch(false);
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(67108864);
        }
        startActivity(intent);
        finish();
    }
}
