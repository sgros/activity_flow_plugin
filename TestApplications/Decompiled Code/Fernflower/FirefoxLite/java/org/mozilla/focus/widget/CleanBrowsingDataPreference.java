package org.mozilla.focus.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebViewDatabase;
import android.widget.Toast;
import java.util.Iterator;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.home.pinsite.SharedPreferencePinSiteDelegate;
import org.mozilla.rocket.privately.PrivateMode;

public class CleanBrowsingDataPreference extends MultiSelectListPreference {
   public CleanBrowsingDataPreference(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CleanBrowsingDataPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onDialogClosed(boolean var1) {
      super.onDialogClosed(var1);
      if (var1) {
         Resources var2 = this.getContext().getResources();

         String var4;
         for(Iterator var3 = this.getValues().iterator(); var3.hasNext(); TelemetryWrapper.settingsEvent(this.getKey(), var4, false)) {
            var4 = (String)var3.next();
            if (var2.getString(2131755335).equals(var4)) {
               BrowsingHistoryManager.getInstance().deleteAll((QueryHandler.AsyncDeleteListener)null);
               TopSitesUtils.clearTopSiteData(this.getContext());
               SharedPreferencePinSiteDelegate.Companion.resetPinSiteData(this.getContext());
            } else if (var2.getString(2131755337).equals(var4)) {
               CookieManager.getInstance().removeAllCookies((ValueCallback)null);
               if (PrivateMode.hasPrivateSession(this.getContext())) {
                  Intent var5 = PrivateSessionNotificationService.buildIntent(this.getContext().getApplicationContext(), true);
                  this.getContext().startActivity(var5);
               }
            } else if (var2.getString(2131755336).equals(var4)) {
               FileUtils.clearCache(this.getContext());
            } else if (var2.getString(2131755338).equals(var4)) {
               WebViewDatabase.getInstance(this.getContext()).clearFormData();
            }
         }

         if (this.getValues().size() > 0) {
            Toast.makeText(this.getContext(), 2131755256, 0).show();
         }
      }

   }

   protected void onPrepareDialogBuilder(Builder var1) {
      super.onPrepareDialogBuilder(var1);
      var1.setTitle((CharSequence)null);
   }
}
