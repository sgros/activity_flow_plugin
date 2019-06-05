package android.arch.lifecycle;

import android.arch.core.internal.FastSafeIterableMap;
import android.arch.core.internal.SafeIterableMap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class LifecycleRegistry extends Lifecycle {
   private int mAddingObserverCounter = 0;
   private boolean mHandlingEvent = false;
   private final WeakReference mLifecycleOwner;
   private boolean mNewEventOccurred = false;
   private FastSafeIterableMap mObserverMap = new FastSafeIterableMap();
   private ArrayList mParentStates = new ArrayList();
   private Lifecycle.State mState;

   public LifecycleRegistry(LifecycleOwner var1) {
      this.mLifecycleOwner = new WeakReference(var1);
      this.mState = Lifecycle.State.INITIALIZED;
   }

   private void backwardPass(LifecycleOwner var1) {
      Iterator var2 = this.mObserverMap.descendingIterator();

      while(var2.hasNext() && !this.mNewEventOccurred) {
         Entry var3 = (Entry)var2.next();
         LifecycleRegistry.ObserverWithState var4 = (LifecycleRegistry.ObserverWithState)var3.getValue();

         while(var4.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(var3.getKey())) {
            Lifecycle.Event var5 = downEvent(var4.mState);
            this.pushParentState(getStateAfter(var5));
            var4.dispatchEvent(var1, var5);
            this.popParentState();
         }
      }

   }

   private Lifecycle.State calculateTargetState(LifecycleObserver var1) {
      Entry var3 = this.mObserverMap.ceil(var1);
      Lifecycle.State var2 = null;
      Lifecycle.State var4;
      if (var3 != null) {
         var4 = ((LifecycleRegistry.ObserverWithState)var3.getValue()).mState;
      } else {
         var4 = null;
      }

      if (!this.mParentStates.isEmpty()) {
         var2 = (Lifecycle.State)this.mParentStates.get(this.mParentStates.size() - 1);
      }

      return min(min(this.mState, var4), var2);
   }

   private static Lifecycle.Event downEvent(Lifecycle.State var0) {
      switch(var0) {
      case INITIALIZED:
         throw new IllegalArgumentException();
      case CREATED:
         return Lifecycle.Event.ON_DESTROY;
      case STARTED:
         return Lifecycle.Event.ON_STOP;
      case RESUMED:
         return Lifecycle.Event.ON_PAUSE;
      case DESTROYED:
         throw new IllegalArgumentException();
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Unexpected state value ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private void forwardPass(LifecycleOwner var1) {
      SafeIterableMap.IteratorWithAdditions var2 = this.mObserverMap.iteratorWithAdditions();

      while(var2.hasNext() && !this.mNewEventOccurred) {
         Entry var3 = (Entry)var2.next();
         LifecycleRegistry.ObserverWithState var4 = (LifecycleRegistry.ObserverWithState)var3.getValue();

         while(var4.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(var3.getKey())) {
            this.pushParentState(var4.mState);
            var4.dispatchEvent(var1, upEvent(var4.mState));
            this.popParentState();
         }
      }

   }

   static Lifecycle.State getStateAfter(Lifecycle.Event var0) {
      switch(var0) {
      case ON_CREATE:
      case ON_STOP:
         return Lifecycle.State.CREATED;
      case ON_START:
      case ON_PAUSE:
         return Lifecycle.State.STARTED;
      case ON_RESUME:
         return Lifecycle.State.RESUMED;
      case ON_DESTROY:
         return Lifecycle.State.DESTROYED;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Unexpected event value ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private boolean isSynced() {
      int var1 = this.mObserverMap.size();
      boolean var2 = true;
      if (var1 == 0) {
         return true;
      } else {
         Lifecycle.State var3 = ((LifecycleRegistry.ObserverWithState)this.mObserverMap.eldest().getValue()).mState;
         Lifecycle.State var4 = ((LifecycleRegistry.ObserverWithState)this.mObserverMap.newest().getValue()).mState;
         if (var3 != var4 || this.mState != var4) {
            var2 = false;
         }

         return var2;
      }
   }

   static Lifecycle.State min(Lifecycle.State var0, Lifecycle.State var1) {
      Lifecycle.State var2 = var0;
      if (var1 != null) {
         var2 = var0;
         if (var1.compareTo(var0) < 0) {
            var2 = var1;
         }
      }

      return var2;
   }

   private void moveToState(Lifecycle.State var1) {
      if (this.mState != var1) {
         this.mState = var1;
         if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
         } else {
            this.mNewEventOccurred = true;
         }
      }
   }

   private void popParentState() {
      this.mParentStates.remove(this.mParentStates.size() - 1);
   }

   private void pushParentState(Lifecycle.State var1) {
      this.mParentStates.add(var1);
   }

   private void sync() {
      LifecycleOwner var1 = (LifecycleOwner)this.mLifecycleOwner.get();
      if (var1 == null) {
         Log.w("LifecycleRegistry", "LifecycleOwner is garbage collected, you shouldn't try dispatch new events from it.");
      } else {
         while(!this.isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo(((LifecycleRegistry.ObserverWithState)this.mObserverMap.eldest().getValue()).mState) < 0) {
               this.backwardPass(var1);
            }

            Entry var2 = this.mObserverMap.newest();
            if (!this.mNewEventOccurred && var2 != null && this.mState.compareTo(((LifecycleRegistry.ObserverWithState)var2.getValue()).mState) > 0) {
               this.forwardPass(var1);
            }
         }

         this.mNewEventOccurred = false;
      }
   }

   private static Lifecycle.Event upEvent(Lifecycle.State var0) {
      switch(var0) {
      case INITIALIZED:
      case DESTROYED:
         return Lifecycle.Event.ON_CREATE;
      case CREATED:
         return Lifecycle.Event.ON_START;
      case STARTED:
         return Lifecycle.Event.ON_RESUME;
      case RESUMED:
         throw new IllegalArgumentException();
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Unexpected state value ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public void addObserver(LifecycleObserver var1) {
      Lifecycle.State var2;
      if (this.mState == Lifecycle.State.DESTROYED) {
         var2 = Lifecycle.State.DESTROYED;
      } else {
         var2 = Lifecycle.State.INITIALIZED;
      }

      LifecycleRegistry.ObserverWithState var3 = new LifecycleRegistry.ObserverWithState(var1, var2);
      if ((LifecycleRegistry.ObserverWithState)this.mObserverMap.putIfAbsent(var1, var3) == null) {
         LifecycleOwner var4 = (LifecycleOwner)this.mLifecycleOwner.get();
         if (var4 != null) {
            boolean var5;
            if (this.mAddingObserverCounter == 0 && !this.mHandlingEvent) {
               var5 = false;
            } else {
               var5 = true;
            }

            var2 = this.calculateTargetState(var1);
            ++this.mAddingObserverCounter;

            while(var3.mState.compareTo(var2) < 0 && this.mObserverMap.contains(var1)) {
               this.pushParentState(var3.mState);
               var3.dispatchEvent(var4, upEvent(var3.mState));
               this.popParentState();
               var2 = this.calculateTargetState(var1);
            }

            if (!var5) {
               this.sync();
            }

            --this.mAddingObserverCounter;
         }
      }
   }

   public Lifecycle.State getCurrentState() {
      return this.mState;
   }

   public void handleLifecycleEvent(Lifecycle.Event var1) {
      this.moveToState(getStateAfter(var1));
   }

   public void markState(Lifecycle.State var1) {
      this.moveToState(var1);
   }

   public void removeObserver(LifecycleObserver var1) {
      this.mObserverMap.remove(var1);
   }

   static class ObserverWithState {
      GenericLifecycleObserver mLifecycleObserver;
      Lifecycle.State mState;

      ObserverWithState(LifecycleObserver var1, Lifecycle.State var2) {
         this.mLifecycleObserver = Lifecycling.getCallback(var1);
         this.mState = var2;
      }

      void dispatchEvent(LifecycleOwner var1, Lifecycle.Event var2) {
         Lifecycle.State var3 = LifecycleRegistry.getStateAfter(var2);
         this.mState = LifecycleRegistry.min(this.mState, var3);
         this.mLifecycleObserver.onStateChanged(var1, var2);
         this.mState = var3;
      }
   }
}
