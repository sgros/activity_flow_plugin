package android.support.v4.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SimpleArrayMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportActivity extends Activity implements LifecycleOwner {
   private SimpleArrayMap mExtraDataMap = new SimpleArrayMap();
   private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public SupportActivity.ExtraData getExtraData(Class var1) {
      return (SupportActivity.ExtraData)this.mExtraDataMap.get(var1);
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   protected void onCreate(@Nullable Bundle var1) {
      super.onCreate(var1);
      ReportFragment.injectIfNeededIn(this);
   }

   @CallSuper
   protected void onSaveInstanceState(Bundle var1) {
      this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
      super.onSaveInstanceState(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void putExtraData(SupportActivity.ExtraData var1) {
      this.mExtraDataMap.put(var1.getClass(), var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class ExtraData {
   }
}
