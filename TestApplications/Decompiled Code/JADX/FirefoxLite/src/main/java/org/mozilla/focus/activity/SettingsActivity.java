package org.mozilla.focus.activity;

import android.os.Bundle;
import android.support.p004v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.settings.SettingsFragment;
import org.mozilla.rocket.C0769R;

public class SettingsActivity extends BaseActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: org.mozilla.focus.activity.SettingsActivity$1 */
    class C04401 implements OnClickListener {
        C04401() {
        }

        public void onClick(View view) {
            SettingsActivity.this.finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0769R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(C0427R.C0426id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new C04401());
        getFragmentManager().beginTransaction().replace(2131296374, new SettingsFragment()).commit();
        applyLocale();
    }

    public void applyLocale() {
        setTitle(C0769R.string.menu_settings);
    }
}
