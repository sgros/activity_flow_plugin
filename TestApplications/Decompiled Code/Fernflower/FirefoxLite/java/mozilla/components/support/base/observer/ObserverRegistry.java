package mozilla.components.support.base.observer;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class ObserverRegistry implements Observable {
   private final WeakHashMap lifecycleObservers = new WeakHashMap();
   private final List observers = (List)(new ArrayList());
   private final Set pausedObservers = Collections.newSetFromMap((Map)(new WeakHashMap()));
   private final WeakHashMap viewObservers = new WeakHashMap();

   public void notifyObservers(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      List var2 = this.observers;
      synchronized(var2){}

      Throwable var10000;
      label152: {
         boolean var10001;
         Iterator var3;
         try {
            var3 = ((Iterable)this.observers).iterator();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label152;
         }

         while(true) {
            try {
               if (!var3.hasNext()) {
                  break;
               }

               Object var4 = var3.next();
               if (!this.pausedObservers.contains(var4)) {
                  var1.invoke(var4);
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label152;
            }
         }

         label134:
         try {
            Unit var18 = Unit.INSTANCE;
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label134;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }

   public void pauseObserver(Object var1) {
      List var2 = this.observers;
      synchronized(var2){}

      try {
         this.pausedObservers.add(var1);
      } finally {
         ;
      }

   }

   public void register(Object var1) {
      List var2 = this.observers;
      synchronized(var2){}

      try {
         this.observers.add(var1);
      } finally {
         ;
      }

   }

   public void register(Object var1, LifecycleOwner var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var2, "owner");
      Lifecycle var4 = var2.getLifecycle();
      Intrinsics.checkExpressionValueIsNotNull(var4, "owner.lifecycle");
      if (var4.getCurrentState() != Lifecycle.State.DESTROYED) {
         this.register(var1);
         ObserverRegistry.LifecycleBoundObserver var5 = new ObserverRegistry.LifecycleBoundObserver(var2, this, var1, var3);
         ((Map)this.lifecycleObservers).put(var1, var5);
         var2.getLifecycle().addObserver((LifecycleObserver)var5);
      }
   }

   public void resumeObserver(Object var1) {
      List var2 = this.observers;
      synchronized(var2){}

      try {
         this.pausedObservers.remove(var1);
      } finally {
         ;
      }

   }

   public void unregister(Object var1) {
      List var2 = this.observers;
      synchronized(var2){}

      try {
         this.observers.remove(var1);
         this.pausedObservers.remove(var1);
      } finally {
         ;
      }

      ObserverRegistry.LifecycleBoundObserver var6 = (ObserverRegistry.LifecycleBoundObserver)this.lifecycleObservers.get(var1);
      if (var6 != null) {
         var6.remove();
      }

      ObserverRegistry.ViewBoundObserver var5 = (ObserverRegistry.ViewBoundObserver)this.viewObservers.get(var1);
      if (var5 != null) {
         var5.remove();
      }

   }

   public void unregisterObservers() {
      List var1 = this.observers;
      synchronized(var1){}

      Throwable var10000;
      label200: {
         Iterator var2;
         boolean var10001;
         try {
            var2 = ((Iterable)this.observers).iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label200;
         }

         while(true) {
            ObserverRegistry.LifecycleBoundObserver var26;
            try {
               if (!var2.hasNext()) {
                  break;
               }

               Object var3 = var2.next();
               var26 = (ObserverRegistry.LifecycleBoundObserver)this.lifecycleObservers.get(var3);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label200;
            }

            if (var26 != null) {
               try {
                  var26.remove();
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label200;
               }
            }
         }

         label181:
         try {
            this.observers.clear();
            this.pausedObservers.clear();
            Unit var25 = Unit.INSTANCE;
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label181;
         }
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public List wrapConsumers(final Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      final List var2 = (List)(new ArrayList());
      List var3 = this.observers;
      synchronized(var3){}

      Throwable var10000;
      label132: {
         Iterator var4;
         boolean var10001;
         try {
            var4 = ((Iterable)this.observers).iterator();
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label132;
         }

         while(true) {
            try {
               if (var4.hasNext()) {
                  final Object var5 = var4.next();
                  Function1 var6 = new Function1() {
                     public final boolean invoke(Object var1x) {
                        return (Boolean)var1.invoke(var5, var1x);
                     }
                  };
                  var2.add(var6);
                  continue;
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break;
            }

            try {
               Unit var20 = Unit.INSTANCE;
               return var2;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var19 = var10000;
      throw var19;
   }

   private static final class LifecycleBoundObserver implements GenericLifecycleObserver {
      private final boolean autoPause;
      private final Object observer;
      private final LifecycleOwner owner;
      private final ObserverRegistry registry;

      public LifecycleBoundObserver(LifecycleOwner var1, ObserverRegistry var2, Object var3, boolean var4) {
         Intrinsics.checkParameterIsNotNull(var1, "owner");
         Intrinsics.checkParameterIsNotNull(var2, "registry");
         super();
         this.owner = var1;
         this.registry = var2;
         this.observer = var3;
         this.autoPause = var4;
      }

      public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
         if (this.autoPause) {
            if (var2 == Lifecycle.Event.ON_PAUSE) {
               this.registry.pauseObserver(this.observer);
            } else if (var2 == Lifecycle.Event.ON_RESUME) {
               this.registry.resumeObserver(this.observer);
            }
         }

         Lifecycle var3 = this.owner.getLifecycle();
         Intrinsics.checkExpressionValueIsNotNull(var3, "owner.lifecycle");
         if (var3.getCurrentState() == Lifecycle.State.DESTROYED) {
            this.registry.unregister(this.observer);
         }

      }

      public final void remove() {
         this.owner.getLifecycle().removeObserver((LifecycleObserver)this);
      }
   }

   private static final class ViewBoundObserver implements OnAttachStateChangeListener {
      private final Object observer;
      private final ObserverRegistry registry;
      private final View view;

      public void onViewAttachedToWindow(View var1) {
         Intrinsics.checkParameterIsNotNull(var1, "view");
      }

      public void onViewDetachedFromWindow(View var1) {
         Intrinsics.checkParameterIsNotNull(var1, "view");
         this.registry.unregister(this.observer);
      }

      public final void remove() {
         this.view.removeOnAttachStateChangeListener((OnAttachStateChangeListener)this);
      }
   }
}
