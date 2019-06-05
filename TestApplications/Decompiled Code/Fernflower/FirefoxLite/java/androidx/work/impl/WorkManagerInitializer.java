package androidx.work.impl;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.work.Configuration;
import androidx.work.WorkManager;

public class WorkManagerInitializer extends ContentProvider {
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
      WorkManager.initialize(this.getContext(), (new Configuration.Builder()).build());
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      return null;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return 0;
   }
}
