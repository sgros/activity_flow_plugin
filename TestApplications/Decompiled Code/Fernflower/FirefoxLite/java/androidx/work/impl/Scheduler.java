package androidx.work.impl;

import androidx.work.impl.model.WorkSpec;

public interface Scheduler {
   void cancel(String var1);

   void schedule(WorkSpec... var1);
}
