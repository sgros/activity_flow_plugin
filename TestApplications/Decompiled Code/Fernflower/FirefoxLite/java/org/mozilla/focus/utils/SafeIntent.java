package org.mozilla.focus.utils;

import android.content.Intent;
import android.util.Log;

public class SafeIntent {
   private static final String LOGTAG;
   private final Intent intent;

   static {
      StringBuilder var0 = new StringBuilder();
      var0.append("Gecko");
      var0.append(SafeIntent.class.getSimpleName());
      LOGTAG = var0.toString();
   }

   public SafeIntent(Intent var1) {
      this.intent = var1;
   }

   public String getAction() {
      return this.intent.getAction();
   }

   public boolean getBooleanExtra(String var1, boolean var2) {
      try {
         boolean var3 = this.intent.getBooleanExtra(var1, var2);
         return var3;
      } catch (OutOfMemoryError var4) {
         Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
         return var2;
      } catch (RuntimeException var5) {
         Log.w(LOGTAG, "Couldn't get intent extras.", var5);
         return var2;
      }
   }

   public CharSequence getCharSequenceExtra(String var1) {
      try {
         CharSequence var4 = this.intent.getCharSequenceExtra(var1);
         return var4;
      } catch (OutOfMemoryError var2) {
         Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
         return null;
      } catch (RuntimeException var3) {
         Log.w(LOGTAG, "Couldn't get intent extras.", var3);
         return null;
      }
   }

   public String getDataString() {
      try {
         String var1 = this.intent.getDataString();
         return var1;
      } catch (OutOfMemoryError var2) {
         Log.w(LOGTAG, "Couldn't get intent data string: OOM. Malformed?");
         return null;
      } catch (RuntimeException var3) {
         Log.w(LOGTAG, "Couldn't get intent data string.", var3);
         return null;
      }
   }

   public String getStringExtra(String var1) {
      try {
         var1 = this.intent.getStringExtra(var1);
         return var1;
      } catch (OutOfMemoryError var2) {
         Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
         return null;
      } catch (RuntimeException var3) {
         Log.w(LOGTAG, "Couldn't get intent extras.", var3);
         return null;
      }
   }
}
