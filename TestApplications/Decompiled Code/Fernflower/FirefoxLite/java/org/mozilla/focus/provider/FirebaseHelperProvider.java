package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.FirebaseHelper;

public class FirebaseHelperProvider extends ContentProvider {
   public int delete(Uri var1, String var2, String[] var3) {
      return 0;
   }

   public String getType(Uri var1) {
      return null;
   }

   public Uri insert(Uri var1, ContentValues var2) {
      return null;
   }

   public boolean onCreate() {
      Context var1 = this.getContext();
      FirebaseHelper.init(var1, TelemetryWrapper.isTelemetryEnabled(var1));
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      return null;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return 0;
   }
}
