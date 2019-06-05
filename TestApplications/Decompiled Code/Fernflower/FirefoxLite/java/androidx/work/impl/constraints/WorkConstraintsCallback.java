package androidx.work.impl.constraints;

import java.util.List;

public interface WorkConstraintsCallback {
   void onAllConstraintsMet(List var1);

   void onAllConstraintsNotMet(List var1);
}
