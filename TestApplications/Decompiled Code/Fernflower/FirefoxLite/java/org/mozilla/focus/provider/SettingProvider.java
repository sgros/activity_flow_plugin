package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class SettingProvider extends ContentProvider {
   public static final SettingProvider.Companion Companion = new SettingProvider.Companion((DefaultConstructorMarker)null);
   private static final UriMatcher uriMatcher = new UriMatcher(-1);

   static {
      uriMatcher.addURI("org.mozilla.rocket.provider.settingprovider", "getFloat", 1);
      uriMatcher.addURI("org.mozilla.rocket.provider.settingprovider", "getBoolean", 2);
   }

   public int delete(Uri var1, String var2, String[] var3) {
      Intrinsics.checkParameterIsNotNull(var1, "uri");
      throw (Throwable)(new UnsupportedOperationException("Not supported"));
   }

   public String getType(Uri var1) {
      Intrinsics.checkParameterIsNotNull(var1, "uri");
      throw (Throwable)(new UnsupportedOperationException("Not supported"));
   }

   public Uri insert(Uri var1, ContentValues var2) {
      Intrinsics.checkParameterIsNotNull(var1, "uri");
      throw (Throwable)(new UnsupportedOperationException("Not supported"));
   }

   public boolean onCreate() {
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      Intrinsics.checkParameterIsNotNull(var1, "uri");
      var3 = (String)null;
      String var6 = "";
      if (var4 != null) {
         var3 = var4[0];
         var6 = var4[1];
      }

      SharedPreferences var8 = PreferenceManager.getDefaultSharedPreferences(this.getContext());
      Bundle var9 = new Bundle();
      switch(uriMatcher.match(var1)) {
      case 1:
         var9.putFloat("key", var8.getFloat(var3, Float.parseFloat(var6)));
         break;
      case 2:
         var9.putBoolean("key", var8.getBoolean(var3, Boolean.parseBoolean(var6)));
         break;
      default:
         StringBuilder var7 = new StringBuilder();
         var7.append("Unknown uri：");
         var7.append(var1);
         throw (Throwable)(new IllegalArgumentException(var7.toString()));
      }

      return (Cursor)(new SettingProvider.BundleCursor(var9));
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      Intrinsics.checkParameterIsNotNull(var1, "uri");
      throw (Throwable)(new UnsupportedOperationException("Not supported"));
   }

   private static final class BundleCursor extends MatrixCursor {
      private Bundle bundle;

      public BundleCursor(Bundle var1) {
         super(new String[0], 0);
         this.bundle = var1;
      }

      public Bundle getExtras() {
         return this.bundle;
      }

      public Bundle respond(Bundle var1) {
         Intrinsics.checkParameterIsNotNull(var1, "extras");
         this.bundle = var1;
         return this.bundle;
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
