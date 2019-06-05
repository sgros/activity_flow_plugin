package androidx.work.impl;

import androidx.work.impl.model.WorkSpec;

public interface Scheduler {
    void cancel(String str);

    void schedule(WorkSpec... workSpecArr);
}
