package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ConstraintsCommandHandler {
   private static final String TAG = Logger.tagWithPrefix("ConstraintsCmdHandler");
   private final Context mContext;
   private final SystemAlarmDispatcher mDispatcher;
   private final int mStartId;
   private final WorkConstraintsTracker mWorkConstraintsTracker;

   ConstraintsCommandHandler(Context var1, int var2, SystemAlarmDispatcher var3) {
      this.mContext = var1;
      this.mStartId = var2;
      this.mDispatcher = var3;
      this.mWorkConstraintsTracker = new WorkConstraintsTracker(this.mContext, (WorkConstraintsCallback)null);
   }

   void handleConstraintsChanged() {
      List var1 = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getScheduledWork();
      ConstraintProxy.updateAll(this.mContext, var1);
      this.mWorkConstraintsTracker.replace(var1);
      ArrayList var2 = new ArrayList(var1.size());
      long var3 = System.currentTimeMillis();
      Iterator var5 = var1.iterator();

      while(true) {
         WorkSpec var6;
         String var7;
         do {
            do {
               if (!var5.hasNext()) {
                  Iterator var8 = var2.iterator();

                  while(var8.hasNext()) {
                     String var9 = ((WorkSpec)var8.next()).id;
                     Intent var10 = CommandHandler.createDelayMetIntent(this.mContext, var9);
                     Logger.get().debug(TAG, String.format("Creating a delay_met command for workSpec with id (%s)", var9));
                     this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, var10, this.mStartId));
                  }

                  this.mWorkConstraintsTracker.reset();
                  return;
               }

               var6 = (WorkSpec)var5.next();
               var7 = var6.id;
            } while(var3 < var6.calculateNextRunTime());
         } while(var6.hasConstraints() && !this.mWorkConstraintsTracker.areAllConstraintsMet(var7));

         var2.add(var6);
      }
   }
}
