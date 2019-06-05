package org.mozilla.focus.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebViewDatabase;
import android.widget.Toast;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.home.pinsite.SharedPreferencePinSiteDelegate;
import org.mozilla.rocket.privately.PrivateMode;

public class CleanBrowsingDataPreference extends MultiSelectListPreference {
    public CleanBrowsingDataPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CleanBrowsingDataPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* Access modifiers changed, original: protected */
    public void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z) {
            Resources resources = getContext().getResources();
            for (String str : getValues()) {
                if (resources.getString(C0769R.string.pref_value_clear_browsing_history).equals(str)) {
                    BrowsingHistoryManager.getInstance().deleteAll(null);
                    TopSitesUtils.clearTopSiteData(getContext());
                    SharedPreferencePinSiteDelegate.Companion.resetPinSiteData(getContext());
                } else if (resources.getString(C0769R.string.pref_value_clear_cookies).equals(str)) {
                    CookieManager.getInstance().removeAllCookies(null);
                    if (PrivateMode.hasPrivateSession(getContext())) {
                        getContext().startActivity(PrivateSessionNotificationService.buildIntent(getContext().getApplicationContext(), true));
                    }
                } else if (resources.getString(C0769R.string.pref_value_clear_cache).equals(str)) {
                    FileUtils.clearCache(getContext());
                } else if (resources.getString(C0769R.string.pref_value_clear_form_history).equals(str)) {
                    WebViewDatabase.getInstance(getContext()).clearFormData();
                }
                TelemetryWrapper.settingsEvent(getKey(), str, false);
            }
            if (getValues().size() > 0) {
                Toast.makeText(getContext(), C0769R.string.message_cleared_browsing_data, 0).show();
            }
        }
    }
}
