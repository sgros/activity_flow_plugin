package android.support.v4.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.KeyEventDispatcher;
import android.view.KeyEvent;
import android.view.View;

public class SupportActivity extends Activity implements LifecycleOwner, KeyEventDispatcher.Component {
   private SimpleArrayMap mExtraDataMap = new SimpleArrayMap();
   private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

   public boolean dispatchKeyEvent(KeyEvent var1) {
      View var2 = this.getWindow().getDecorView();
      return var2 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var2, var1) ? true : KeyEventDispatcher.dispatchKeyEvent(this, var2, this, var1);
   }

   public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
      View var2 = this.getWindow().getDecorView();
      return var2 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var2, var1) ? true : super.dispatchKeyShortcutEvent(var1);
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      ReportFragment.injectIfNeededIn(this);
   }

   protected void onSaveInstanceState(Bundle var1) {
      this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
      super.onSaveInstanceState(var1);
   }

   public boolean superDispatchKeyEvent(KeyEvent var1) {
      return super.dispatchKeyEvent(var1);
   }
}
