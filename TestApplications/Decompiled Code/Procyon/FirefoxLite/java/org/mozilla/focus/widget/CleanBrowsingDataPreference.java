// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.app.AlertDialog$Builder;
import java.util.Iterator;
import android.content.res.Resources;
import android.widget.Toast;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.webkit.WebViewDatabase;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.privately.PrivateMode;
import android.webkit.ValueCallback;
import android.webkit.CookieManager;
import org.mozilla.rocket.home.pinsite.SharedPreferencePinSiteDelegate;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.history.BrowsingHistoryManager;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.MultiSelectListPreference;

public class CleanBrowsingDataPreference extends MultiSelectListPreference
{
    public CleanBrowsingDataPreference(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public CleanBrowsingDataPreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onDialogClosed(final boolean b) {
        super.onDialogClosed(b);
        if (b) {
            final Resources resources = this.getContext().getResources();
            for (final String s : this.getValues()) {
                if (resources.getString(2131755335).equals(s)) {
                    BrowsingHistoryManager.getInstance().deleteAll(null);
                    TopSitesUtils.clearTopSiteData(this.getContext());
                    SharedPreferencePinSiteDelegate.Companion.resetPinSiteData(this.getContext());
                }
                else if (resources.getString(2131755337).equals(s)) {
                    CookieManager.getInstance().removeAllCookies((ValueCallback)null);
                    if (PrivateMode.hasPrivateSession(this.getContext())) {
                        this.getContext().startActivity(PrivateSessionNotificationService.buildIntent(this.getContext().getApplicationContext(), true));
                    }
                }
                else if (resources.getString(2131755336).equals(s)) {
                    FileUtils.clearCache(this.getContext());
                }
                else if (resources.getString(2131755338).equals(s)) {
                    WebViewDatabase.getInstance(this.getContext()).clearFormData();
                }
                TelemetryWrapper.settingsEvent(this.getKey(), s, false);
            }
            if (this.getValues().size() > 0) {
                Toast.makeText(this.getContext(), 2131755256, 0).show();
            }
        }
    }
    
    protected void onPrepareDialogBuilder(final AlertDialog$Builder alertDialog$Builder) {
        super.onPrepareDialogBuilder(alertDialog$Builder);
        alertDialog$Builder.setTitle((CharSequence)null);
    }
}
