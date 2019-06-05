package org.mozilla.focus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p004v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.InfoFragment;
import org.mozilla.focus.locale.Locales;
import org.mozilla.rocket.C0769R;

public class InfoActivity extends BaseActivity {
    private InfoFragment infoFragment;

    /* renamed from: org.mozilla.focus.activity.InfoActivity$1 */
    class C04341 implements OnClickListener {
        C04341() {
        }

        public void onClick(View view) {
            InfoActivity.this.finish();
        }
    }

    public void applyLocale() {
    }

    public static final Intent getIntentFor(Context context, String str, String str2) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("extra_url", str);
        intent.putExtra("extra_title", str2);
        return intent;
    }

    public static final Intent getAboutIntent(Context context) {
        return getIntentFor(context, "focusabout:", Locales.getLocalizedResources(context).getString(C0769R.string.menu_about));
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0769R.layout.activity_info);
        String stringExtra = getIntent().getStringExtra("extra_url");
        CharSequence stringExtra2 = getIntent().getStringExtra("extra_title");
        this.infoFragment = InfoFragment.create(stringExtra);
        getSupportFragmentManager().beginTransaction().replace(C0427R.C0426id.infofragment, this.infoFragment).commit();
        Toolbar toolbar = (Toolbar) findViewById(C0427R.C0426id.toolbar);
        toolbar.setTitle(stringExtra2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new C04341());
    }

    public void onBackPressed() {
        if (this.infoFragment == null || !this.infoFragment.canGoBack()) {
            super.onBackPressed();
        } else {
            this.infoFragment.goBack();
        }
    }
}
