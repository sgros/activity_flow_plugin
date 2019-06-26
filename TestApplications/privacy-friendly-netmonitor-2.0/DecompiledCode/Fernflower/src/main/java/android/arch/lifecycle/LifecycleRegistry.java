package android.arch.lifecycle;

import android.arch.core.internal.FastSafeIterableMap;
import android.arch.core.internal.SafeIterableMap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class LifecycleRegistry extends Lifecycle {
   private int mAddingObserverCounter = 0;
   private boolean mHandlingEvent = false;
   private final LifecycleOwner mLifecycleOwner;
   private boolean mNewEventOccurred = false;
   private FastSafeIterableMap mObserverMap = new FastSafeIterableMap();
   private ArrayList mParentStates = new ArrayList();
   private Lifecycle.State mState;

   public LifecycleRegistry(@NonNull LifecycleOwner var1) {
      this.mLifecycleOwner = var1;
      this.mState = Lifecycle.State.INITIALIZED;
   }

   private void backwardPass() {
      Iterator var1 = this.mObserverMap.descendingIterator();

      while(var1.hasNext() && !this.mNewEventOccurred) {
         Entry var2 = (Entry)var1.next();
         LifecycleRegistry.ObserverWithState var3 = (LifecycleRegistry.ObserverWithState)var2.getValue();

         while(var3.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(var2.getKey())) {
            Lifecycle.Event var4 = downEvent(var3.mState);
            this.pushParentState(getStateAfter(var4));
            var3.dispatchEvent(this.mLifecycleOwner, var4);
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

   private void forwardPass() {
      SafeIterableMap.IteratorWithAdditions var1 = this.mObserverMap.iteratorWithAdditions();

      while(var1.hasNext() && !this.mNewEventOccurred) {
         Entry var2 = (Entry)var1.next();
         LifecycleRegistry.ObserverWithState var3 = (LifecycleRegistry.ObserverWithState)var2.getValue();

         while(var3.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(var2.getKey())) {
            this.pushParentState(var3.mState);
            var3.dispatchEvent(this.mLifecycleOwner, upEvent(var3.mState));
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

   static Lifecycle.State min(@NonNull Lifecycle.State var0, @Nullable Lifecycle.State var1) {
      Lifecycle.State var2 = var0;
      if (var1 != null) {
         var2 = var0;
         if (var1.compareTo(var0) < 0) {
            var2 = var1;
         }
      }

      return var2;
   }

   private void popParentState() {
      this.mParentStates.remove(this.mParentStates.size() - 1);
   }

   private void pushParentState(Lifecycle.State var1) {
      this.mParentStates.add(var1);
   }

   private void sync() {
      while(!this.isSynced()) {
         this.mNewEventOccurred = false;
         if (this.mState.compareTo(((LifecycleRegistry.ObserverWithState)this.mObserverMap.eldest().getValue()).mState) < 0) {
            this.backwardPass();
         }

         Entry var1 = this.mObserverMap.newest();
         if (!this.mNewEventOccurred && var1 != null && this.mState.compareTo(((LifecycleRegistry.ObserverWithState)var1.getValue()).mState) > 0) {
            this.forwardPass();
         }
      }

      this.mNewEventOccurred = false;
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
         boolean var4;
         if (this.mAddingObserverCounter == 0 && !this.mHandlingEvent) {
            var4 = false;
         } else {
            var4 = true;
         }

         var2 = this.calculateTargetState(var1);
         ++this.mAddingObserverCounter;

         while(var3.mState.compareTo(var2) < 0 && this.mObserverMap.contains(var1)) {
            this.pushParentState(var3.mState);
            var3.dispatchEvent(this.mLifecycleOwner, upEvent(var3.mState));
            this.popParentState();
            var2 = this.calculateTargetState(var1);
         }

         if (!var4) {
            this.sync();
         }

         --this.mAddingObserverCounter;
      }
   }

   public Lifecycle.State getCurrentState() {
      return this.mState;
   }

   public int getObserverCount() {
      return this.mObserverMap.size();
   }

   public void handleLifecycleEvent(Lifecycle.Event var1) {
      this.mState = getStateAfter(var1);
      if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
         this.mHandlingEvent = true;
         this.sync();
         this.mHandlingEvent = false;
      } else {
         this.mNewEventOccurred = true;
      }
   }

   public void markState(Lifecycle.State var1) {
      this.mState = var1;
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
