package android.arch.core.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.WeakHashMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SafeIterableMap implements Iterable {
   private SafeIterableMap.Entry mEnd;
   private WeakHashMap mIterators = new WeakHashMap();
   private int mSize = 0;
   private SafeIterableMap.Entry mStart;

   public Iterator descendingIterator() {
      SafeIterableMap.DescendingIterator var1 = new SafeIterableMap.DescendingIterator(this.mEnd, this.mStart);
      this.mIterators.put(var1, false);
      return var1;
   }

   public java.util.Map.Entry eldest() {
      return this.mStart;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SafeIterableMap)) {
         return false;
      } else {
         SafeIterableMap var3 = (SafeIterableMap)var1;
         if (this.size() != var3.size()) {
            return false;
         } else {
            Iterator var6 = this.iterator();
            Iterator var4 = var3.iterator();

            while(true) {
               if (var6.hasNext() && var4.hasNext()) {
                  java.util.Map.Entry var7 = (java.util.Map.Entry)var6.next();
                  Object var5 = var4.next();
                  if ((var7 != null || var5 == null) && (var7 == null || var7.equals(var5))) {
                     continue;
                  }

                  return false;
               }

               if (var6.hasNext() || var4.hasNext()) {
                  var2 = false;
               }

               return var2;
            }
         }
      }
   }

   protected SafeIterableMap.Entry get(Object var1) {
      SafeIterableMap.Entry var2;
      for(var2 = this.mStart; var2 != null && !var2.mKey.equals(var1); var2 = var2.mNext) {
      }

      return var2;
   }

   @NonNull
   public Iterator iterator() {
      SafeIterableMap.AscendingIterator var1 = new SafeIterableMap.AscendingIterator(this.mStart, this.mEnd);
      this.mIterators.put(var1, false);
      return var1;
   }

   public SafeIterableMap.IteratorWithAdditions iteratorWithAdditions() {
      SafeIterableMap.IteratorWithAdditions var1 = new SafeIterableMap.IteratorWithAdditions();
      this.mIterators.put(var1, false);
      return var1;
   }

   public java.util.Map.Entry newest() {
      return this.mEnd;
   }

   protected SafeIterableMap.Entry put(@NonNull Object var1, @NonNull Object var2) {
      SafeIterableMap.Entry var3 = new SafeIterableMap.Entry(var1, var2);
      ++this.mSize;
      if (this.mEnd == null) {
         this.mStart = var3;
         this.mEnd = this.mStart;
         return var3;
      } else {
         this.mEnd.mNext = var3;
         var3.mPrevious = this.mEnd;
         this.mEnd = var3;
         return var3;
      }
   }

   public Object putIfAbsent(@NonNull Object var1, @NonNull Object var2) {
      SafeIterableMap.Entry var3 = this.get(var1);
      if (var3 != null) {
         return var3.mValue;
      } else {
         this.put(var1, var2);
         return null;
      }
   }

   public Object remove(@NonNull Object var1) {
      SafeIterableMap.Entry var2 = this.get(var1);
      if (var2 == null) {
         return null;
      } else {
         --this.mSize;
         if (!this.mIterators.isEmpty()) {
            Iterator var3 = this.mIterators.keySet().iterator();

            while(var3.hasNext()) {
               ((SafeIterableMap.SupportRemove)var3.next()).supportRemove(var2);
            }
         }

         if (var2.mPrevious != null) {
            var2.mPrevious.mNext = var2.mNext;
         } else {
            this.mStart = var2.mNext;
         }

         if (var2.mNext != null) {
            var2.mNext.mPrevious = var2.mPrevious;
         } else {
            this.mEnd = var2.mPrevious;
         }

         var2.mNext = null;
         var2.mPrevious = null;
         return var2.mValue;
      }
   }

   public int size() {
      return this.mSize;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.append(((java.util.Map.Entry)var2.next()).toString());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append("]");
      return var1.toString();
   }

   static class AscendingIterator extends SafeIterableMap.ListIterator {
      AscendingIterator(SafeIterableMap.Entry var1, SafeIterableMap.Entry var2) {
         super(var1, var2);
      }

      SafeIterableMap.Entry backward(SafeIterableMap.Entry var1) {
         return var1.mPrevious;
      }

      SafeIterableMap.Entry forward(SafeIterableMap.Entry var1) {
         return var1.mNext;
      }
   }

   private static class DescendingIterator extends SafeIterableMap.ListIterator {
      DescendingIterator(SafeIterableMap.Entry var1, SafeIterableMap.Entry var2) {
         super(var1, var2);
      }

      SafeIterableMap.Entry backward(SafeIterableMap.Entry var1) {
         return var1.mNext;
      }

      SafeIterableMap.Entry forward(SafeIterableMap.Entry var1) {
         return var1.mPrevious;
      }
   }

   static class Entry implements java.util.Map.Entry {
      @NonNull
      final Object mKey;
      SafeIterableMap.Entry mNext;
      SafeIterableMap.Entry mPrevious;
      @NonNull
      final Object mValue;

      Entry(@NonNull Object var1, @NonNull Object var2) {
         this.mKey = var1;
         this.mValue = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof SafeIterableMap.Entry)) {
            return false;
         } else {
            SafeIterableMap.Entry var3 = (SafeIterableMap.Entry)var1;
            if (!this.mKey.equals(var3.mKey) || !this.mValue.equals(var3.mValue)) {
               var2 = false;
            }

            return var2;
         }
      }

      @NonNull
      public Object getKey() {
         return this.mKey;
      }

      @NonNull
      public Object getValue() {
         return this.mValue;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException("An entry modification is not supported");
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.mKey);
         var1.append("=");
         var1.append(this.mValue);
         return var1.toString();
      }
   }

   private class IteratorWithAdditions implements Iterator, SafeIterableMap.SupportRemove {
      private boolean mBeforeStart;
      private SafeIterableMap.Entry mCurrent;

      private IteratorWithAdditions() {
         this.mBeforeStart = true;
      }

      // $FF: synthetic method
      IteratorWithAdditions(Object var2) {
         this();
      }

      public boolean hasNext() {
         boolean var1 = this.mBeforeStart;
         boolean var2 = false;
         boolean var3 = false;
         if (var1) {
            if (SafeIterableMap.this.mStart != null) {
               var3 = true;
            }

            return var3;
         } else {
            var3 = var2;
            if (this.mCurrent != null) {
               var3 = var2;
               if (this.mCurrent.mNext != null) {
                  var3 = true;
               }
            }

            return var3;
         }
      }

      public java.util.Map.Entry next() {
         if (this.mBeforeStart) {
            this.mBeforeStart = false;
            this.mCurrent = SafeIterableMap.this.mStart;
         } else {
            SafeIterableMap.Entry var1;
            if (this.mCurrent != null) {
               var1 = this.mCurrent.mNext;
            } else {
               var1 = null;
            }

            this.mCurrent = var1;
         }

         return this.mCurrent;
      }

      public void supportRemove(@NonNull SafeIterableMap.Entry var1) {
         if (var1 == this.mCurrent) {
            this.mCurrent = this.mCurrent.mPrevious;
            boolean var2;
            if (this.mCurrent == null) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.mBeforeStart = var2;
         }

      }
   }

   private abstract static class ListIterator implements Iterator, SafeIterableMap.SupportRemove {
      SafeIterableMap.Entry mExpectedEnd;
      SafeIterableMap.Entry mNext;

      ListIterator(SafeIterableMap.Entry var1, SafeIterableMap.Entry var2) {
         this.mExpectedEnd = var2;
         this.mNext = var1;
      }

      private SafeIterableMap.Entry nextNode() {
         return this.mNext != this.mExpectedEnd && this.mExpectedEnd != null ? this.forward(this.mNext) : null;
      }

      abstract SafeIterableMap.Entry backward(SafeIterableMap.Entry var1);

      abstract SafeIterableMap.Entry forward(SafeIterableMap.Entry var1);

      public boolean hasNext() {
         boolean var1;
         if (this.mNext != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public java.util.Map.Entry next() {
         SafeIterableMap.Entry var1 = this.mNext;
         this.mNext = this.nextNode();
         return var1;
      }

      public void supportRemove(@NonNull SafeIterableMap.Entry var1) {
         if (this.mExpectedEnd == var1 && var1 == this.mNext) {
            this.mNext = null;
            this.mExpectedEnd = null;
         }

         if (this.mExpectedEnd == var1) {
            this.mExpectedEnd = this.backward(this.mExpectedEnd);
         }

         if (this.mNext == var1) {
            this.mNext = this.nextNode();
         }

      }
   }

   interface SupportRemove {
      void supportRemove(@NonNull SafeIterableMap.Entry var1);
   }
}
