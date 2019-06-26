package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.BuildConfig;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class AboutActivity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0501R.layout.activity_about);
        RunStore.setContext(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        View findViewById = findViewById(C0501R.C0499id.main_content);
        if (findViewById != null) {
            findViewById.setAlpha(0.0f);
            findViewById.animate().alpha(1.0f).setDuration(250);
        }
        overridePendingTransition(0, 0);
        ((TextView) findViewById(C0501R.C0499id.javaAPIURL)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(C0501R.C0499id.APIURL)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(C0501R.C0499id.secusoWebsite)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(C0501R.C0499id.githubURL)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(C0501R.C0499id.textFieldVersionName)).setText(BuildConfig.VERSION_NAME);
    }
}
