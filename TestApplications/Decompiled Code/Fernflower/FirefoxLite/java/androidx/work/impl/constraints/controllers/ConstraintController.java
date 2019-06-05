package androidx.work.impl.constraints.controllers;

import androidx.work.impl.constraints.ConstraintListener;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ConstraintController implements ConstraintListener {
   private ConstraintController.OnConstraintUpdatedCallback mCallback;
   private Object mCurrentValue;
   private final List mMatchingWorkSpecIds = new ArrayList();
   private ConstraintTracker mTracker;

   ConstraintController(ConstraintTracker var1) {
      this.mTracker = var1;
   }

   private void updateCallback() {
      if (!this.mMatchingWorkSpecIds.isEmpty() && this.mCallback != null) {
         if (this.mCurrentValue != null && !this.isConstrained(this.mCurrentValue)) {
            this.mCallback.onConstraintMet(this.mMatchingWorkSpecIds);
         } else {
            this.mCallback.onConstraintNotMet(this.mMatchingWorkSpecIds);
         }

      }
   }

   abstract boolean hasConstraint(WorkSpec var1);

   abstract boolean isConstrained(Object var1);

   public boolean isWorkSpecConstrained(String var1) {
      boolean var2;
      if (this.mCurrentValue != null && this.isConstrained(this.mCurrentValue) && this.mMatchingWorkSpecIds.contains(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onConstraintChanged(Object var1) {
      this.mCurrentValue = var1;
      this.updateCallback();
   }

   public void replace(List var1) {
      this.mMatchingWorkSpecIds.clear();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WorkSpec var2 = (WorkSpec)var3.next();
         if (this.hasConstraint(var2)) {
            this.mMatchingWorkSpecIds.add(var2.id);
         }
      }

      if (this.mMatchingWorkSpecIds.isEmpty()) {
         this.mTracker.removeListener(this);
      } else {
         this.mTracker.addListener(this);
      }

      this.updateCallback();
   }

   public void reset() {
      if (!this.mMatchingWorkSpecIds.isEmpty()) {
         this.mMatchingWorkSpecIds.clear();
         this.mTracker.removeListener(this);
      }

   }

   public void setCallback(ConstraintController.OnConstraintUpdatedCallback var1) {
      if (this.mCallback != var1) {
         this.mCallback = var1;
         this.updateCallback();
      }

   }

   public interface OnConstraintUpdatedCallback {
      void onConstraintMet(List var1);

      void onConstraintNotMet(List var1);
   }
}
