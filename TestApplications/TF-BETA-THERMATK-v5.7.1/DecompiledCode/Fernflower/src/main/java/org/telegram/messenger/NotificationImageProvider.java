package org.telegram.messenger;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;

public class NotificationImageProvider extends ContentProvider implements NotificationCenter.NotificationCenterDelegate {
   public static final String AUTHORITY = "org.telegram.messenger.notification_image_provider";
   private static final UriMatcher matcher = new UriMatcher(-1);
   private HashMap fileStartTimes = new HashMap();
   private final Object sync = new Object();
   private HashSet waitingForFiles = new HashSet();

   static {
      matcher.addURI("org.telegram.messenger.notification_image_provider", "msg_media_raw/#/*", 1);
   }

   public int delete(Uri var1, String var2, String[] var3) {
      return 0;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.fileDidLoad) {
         Object var4 = this.sync;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               String var17 = (String)var3[0];
               if (this.waitingForFiles.remove(var17)) {
                  this.fileStartTimes.remove(var17);
                  this.sync.notifyAll();
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var18 = var10000;

            try {
               throw var18;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public String[] getStreamTypes(Uri var1, String var2) {
      return !var2.startsWith("*/") && !var2.startsWith("image/") ? null : new String[]{"image/jpeg", "image/png", "image/webp"};
   }

   public String getType(Uri var1) {
      return null;
   }

   public Uri insert(Uri var1, ContentValues var2) {
      return null;
   }

   public boolean onCreate() {
      for(int var1 = 0; var1 < UserConfig.getActivatedAccountsCount(); ++var1) {
         NotificationCenter.getInstance(var1).addObserver(this, NotificationCenter.fileDidLoad);
      }

      return true;
   }

   public ParcelFileDescriptor openFile(Uri param1, String param2) throws FileNotFoundException {
      // $FF: Couldn't be decompiled
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      return null;
   }

   public void shutdown() {
      for(int var1 = 0; var1 < UserConfig.getActivatedAccountsCount(); ++var1) {
         NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.fileDidLoad);
      }

   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return 0;
   }
}
