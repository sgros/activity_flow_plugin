package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder implements Iterable {
   private final ArrayList mIntents = new ArrayList();
   private final Context mSourceContext;

   private TaskStackBuilder(Context var1) {
      this.mSourceContext = var1;
   }

   public static TaskStackBuilder create(Context var0) {
      return new TaskStackBuilder(var0);
   }

   public TaskStackBuilder addNextIntent(Intent var1) {
      this.mIntents.add(var1);
      return this;
   }

   public TaskStackBuilder addParentStack(Activity var1) {
      Intent var2;
      if (var1 instanceof TaskStackBuilder.SupportParentable) {
         var2 = ((TaskStackBuilder.SupportParentable)var1).getSupportParentActivityIntent();
      } else {
         var2 = null;
      }

      Intent var3 = var2;
      if (var2 == null) {
         var3 = NavUtils.getParentActivityIntent(var1);
      }

      if (var3 != null) {
         ComponentName var5 = var3.getComponent();
         ComponentName var4 = var5;
         if (var5 == null) {
            var4 = var3.resolveActivity(this.mSourceContext.getPackageManager());
         }

         this.addParentStack(var4);
         this.addNextIntent(var3);
      }

      return this;
   }

   public TaskStackBuilder addParentStack(ComponentName var1) {
      int var2 = this.mIntents.size();

      NameNotFoundException var10000;
      label24: {
         boolean var10001;
         Intent var5;
         try {
            var5 = NavUtils.getParentActivityIntent(this.mSourceContext, var1);
         } catch (NameNotFoundException var4) {
            var10000 = var4;
            var10001 = false;
            break label24;
         }

         while(true) {
            if (var5 == null) {
               return this;
            }

            try {
               this.mIntents.add(var2, var5);
               var5 = NavUtils.getParentActivityIntent(this.mSourceContext, var5.getComponent());
            } catch (NameNotFoundException var3) {
               var10000 = var3;
               var10001 = false;
               break;
            }
         }
      }

      NameNotFoundException var6 = var10000;
      Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
      throw new IllegalArgumentException(var6);
   }

   @Deprecated
   public Iterator iterator() {
      return this.mIntents.iterator();
   }

   public void startActivities() {
      this.startActivities((Bundle)null);
   }

   public void startActivities(Bundle var1) {
      if (!this.mIntents.isEmpty()) {
         Intent[] var2 = (Intent[])this.mIntents.toArray(new Intent[this.mIntents.size()]);
         var2[0] = (new Intent(var2[0])).addFlags(268484608);
         if (!ContextCompat.startActivities(this.mSourceContext, var2, var1)) {
            Intent var3 = new Intent(var2[var2.length - 1]);
            var3.addFlags(268435456);
            this.mSourceContext.startActivity(var3);
         }

      } else {
         throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
      }
   }

   public interface SupportParentable {
      Intent getSupportParentActivityIntent();
   }
}
