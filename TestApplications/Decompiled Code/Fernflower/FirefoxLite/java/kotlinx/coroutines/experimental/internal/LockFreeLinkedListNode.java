package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class LockFreeLinkedListNode {
   static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_next");
   static final AtomicReferenceFieldUpdater _prev$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_prev");
   private static final AtomicReferenceFieldUpdater _removedRef$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_removedRef");
   volatile Object _next = this;
   volatile Object _prev = this;
   private volatile Object _removedRef = null;

   private final LockFreeLinkedListNode correctPrev(LockFreeLinkedListNode var1, OpDescriptor var2) {
      LockFreeLinkedListNode var3 = (LockFreeLinkedListNode)null;

      while(true) {
         LockFreeLinkedListNode var4 = var3;

         while(true) {
            Object var5 = var1._next;
            if (var5 == var2) {
               return var1;
            }

            if (var5 instanceof OpDescriptor) {
               ((OpDescriptor)var5).perform(var1);
            } else if (var5 instanceof Removed) {
               if (var4 != null) {
                  var1.markPrev();
                  _next$FU.compareAndSet(var4, var1, ((Removed)var5).ref);
                  var1 = var4;
                  break;
               }

               var1 = LockFreeLinkedListKt.unwrap(var1._prev);
            } else {
               Object var6 = this._prev;
               if (var6 instanceof Removed) {
                  return null;
               }

               if (var5 != (LockFreeLinkedListNode)this) {
                  if (var5 == null) {
                     throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                  }

                  LockFreeLinkedListNode var7 = (LockFreeLinkedListNode)var5;
                  var4 = var1;
                  var1 = var7;
               } else {
                  if (var6 == var1) {
                     return null;
                  }

                  if (_prev$FU.compareAndSet(this, var6, var1) && !(var1._prev instanceof Removed)) {
                     return null;
                  }
               }
            }
         }
      }
   }

   private final LockFreeLinkedListNode findHead() {
      LockFreeLinkedListNode var1 = (LockFreeLinkedListNode)this;
      LockFreeLinkedListNode var2 = var1;

      boolean var3;
      do {
         if (var2 instanceof LockFreeLinkedListHead) {
            return var2;
         }

         var2 = var2.getNextNode();
         if (var2 != var1) {
            var3 = true;
         } else {
            var3 = false;
         }
      } while(var3);

      throw (Throwable)(new IllegalStateException("Cannot loop to this while looking for list head".toString()));
   }

   private final void finishAdd(LockFreeLinkedListNode var1) {
      while(true) {
         Object var2 = var1._prev;
         if (!(var2 instanceof Removed) && this.getNext() == var1) {
            if (!_prev$FU.compareAndSet(var1, var2, this)) {
               continue;
            }

            if (this.getNext() instanceof Removed) {
               if (var2 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
               }

               var1.correctPrev((LockFreeLinkedListNode)var2, (OpDescriptor)null);
            }

            return;
         }

         return;
      }
   }

   private final void finishRemove(LockFreeLinkedListNode var1) {
      this.helpDelete();
      var1.correctPrev(LockFreeLinkedListKt.unwrap(this._prev), (OpDescriptor)null);
   }

   private final LockFreeLinkedListNode markPrev() {
      Object var1;
      Removed var3;
      do {
         var1 = this._prev;
         if (var1 instanceof Removed) {
            return ((Removed)var1).ref;
         }

         LockFreeLinkedListNode var2;
         if (var1 == (LockFreeLinkedListNode)this) {
            var2 = this.findHead();
         } else {
            if (var1 == null) {
               throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }

            var2 = (LockFreeLinkedListNode)var1;
         }

         var3 = var2.removed();
      } while(!_prev$FU.compareAndSet(this, var1, var3));

      return (LockFreeLinkedListNode)var1;
   }

   private final Removed removed() {
      Removed var1 = (Removed)this._removedRef;
      if (var1 == null) {
         var1 = new Removed(this);
         _removedRef$FU.lazySet(this, var1);
      }

      return var1;
   }

   public final boolean addOneIfEmpty(LockFreeLinkedListNode var1) {
      Intrinsics.checkParameterIsNotNull(var1, "node");
      _prev$FU.lazySet(var1, this);
      _next$FU.lazySet(var1, this);

      while(this.getNext() == (LockFreeLinkedListNode)this) {
         if (_next$FU.compareAndSet(this, this, var1)) {
            var1.finishAdd(this);
            return true;
         }
      }

      return false;
   }

   public final Object getNext() {
      while(true) {
         Object var1 = this._next;
         if (!(var1 instanceof OpDescriptor)) {
            return var1;
         }

         ((OpDescriptor)var1).perform(this);
      }
   }

   public final LockFreeLinkedListNode getNextNode() {
      return LockFreeLinkedListKt.unwrap(this.getNext());
   }

   public final Object getPrev() {
      while(true) {
         Object var1 = this._prev;
         if (var1 instanceof Removed) {
            return var1;
         }

         if (var1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
         }

         LockFreeLinkedListNode var2 = (LockFreeLinkedListNode)var1;
         if (var2.getNext() == (LockFreeLinkedListNode)this) {
            return var1;
         }

         this.correctPrev(var2, (OpDescriptor)null);
      }
   }

   public final LockFreeLinkedListNode getPrevNode() {
      return LockFreeLinkedListKt.unwrap(this.getPrev());
   }

   public final void helpDelete() {
      LockFreeLinkedListNode var1 = (LockFreeLinkedListNode)null;
      LockFreeLinkedListNode var2 = this.markPrev();
      Object var3 = this._next;
      if (var3 == null) {
         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Removed");
      } else {
         LockFreeLinkedListNode var4 = ((Removed)var3).ref;

         label42:
         while(true) {
            LockFreeLinkedListNode var7 = var1;

            while(true) {
               while(true) {
                  Object var5 = var4.getNext();
                  if (var5 instanceof Removed) {
                     var4.markPrev();
                     var4 = ((Removed)var5).ref;
                  } else {
                     var5 = var2.getNext();
                     if (var5 instanceof Removed) {
                        if (var7 != null) {
                           var2.markPrev();
                           _next$FU.compareAndSet(var7, var2, ((Removed)var5).ref);
                           var2 = var7;
                           continue label42;
                        }

                        var2 = LockFreeLinkedListKt.unwrap(var2._prev);
                     } else if (var5 != (LockFreeLinkedListNode)this) {
                        if (var5 == null) {
                           throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        }

                        LockFreeLinkedListNode var6 = (LockFreeLinkedListNode)var5;
                        if (var6 == var4) {
                           return;
                        }

                        var7 = var2;
                        var2 = var6;
                     } else if (_next$FU.compareAndSet(var2, this, var4)) {
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   public final boolean isRemoved() {
      return this.getNext() instanceof Removed;
   }

   public boolean remove() {
      while(true) {
         Object var1 = this.getNext();
         if (var1 instanceof Removed) {
            return false;
         }

         if (var1 == (LockFreeLinkedListNode)this) {
            return false;
         }

         if (var1 != null) {
            LockFreeLinkedListNode var2 = (LockFreeLinkedListNode)var1;
            Removed var3 = var2.removed();
            if (!_next$FU.compareAndSet(this, var1, var3)) {
               continue;
            }

            this.finishRemove(var2);
            return true;
         }

         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append('@');
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      return var1.toString();
   }

   public final int tryCondAddNext(LockFreeLinkedListNode var1, LockFreeLinkedListNode var2, LockFreeLinkedListNode.CondAddOp var3) {
      Intrinsics.checkParameterIsNotNull(var1, "node");
      Intrinsics.checkParameterIsNotNull(var2, "next");
      Intrinsics.checkParameterIsNotNull(var3, "condAdd");
      _prev$FU.lazySet(var1, this);
      _next$FU.lazySet(var1, var2);
      var3.oldNext = var2;
      if (!_next$FU.compareAndSet(this, var2, var3)) {
         return 0;
      } else {
         byte var4;
         if (var3.perform(this) == null) {
            var4 = 1;
         } else {
            var4 = 2;
         }

         return var4;
      }
   }

   public abstract static class CondAddOp extends AtomicOp {
      public final LockFreeLinkedListNode newNode;
      public LockFreeLinkedListNode oldNext;

      public CondAddOp(LockFreeLinkedListNode var1) {
         Intrinsics.checkParameterIsNotNull(var1, "newNode");
         super();
         this.newNode = var1;
      }

      public void complete(LockFreeLinkedListNode var1, Object var2) {
         Intrinsics.checkParameterIsNotNull(var1, "affected");
         boolean var3;
         if (var2 == null) {
            var3 = true;
         } else {
            var3 = false;
         }

         LockFreeLinkedListNode var4;
         if (var3) {
            var4 = this.newNode;
         } else {
            var4 = this.oldNext;
         }

         if (var4 != null && LockFreeLinkedListNode._next$FU.compareAndSet(var1, this, var4) && var3) {
            var4 = this.newNode;
            var1 = this.oldNext;
            if (var1 == null) {
               Intrinsics.throwNpe();
            }

            var4.finishAdd(var1);
         }

      }
   }
}
