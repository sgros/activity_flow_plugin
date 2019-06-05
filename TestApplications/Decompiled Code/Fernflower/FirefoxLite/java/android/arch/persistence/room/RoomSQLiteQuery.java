package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public class RoomSQLiteQuery implements SupportSQLiteProgram, SupportSQLiteQuery {
   static final TreeMap sQueryPool = new TreeMap();
   int mArgCount;
   private final int[] mBindingTypes;
   final byte[][] mBlobBindings;
   final int mCapacity;
   final double[] mDoubleBindings;
   final long[] mLongBindings;
   private volatile String mQuery;
   final String[] mStringBindings;

   private RoomSQLiteQuery(int var1) {
      this.mCapacity = var1++;
      this.mBindingTypes = new int[var1];
      this.mLongBindings = new long[var1];
      this.mDoubleBindings = new double[var1];
      this.mStringBindings = new String[var1];
      this.mBlobBindings = new byte[var1][];
   }

   public static RoomSQLiteQuery acquire(String var0, int var1) {
      TreeMap var2 = sQueryPool;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label173: {
         Entry var3;
         try {
            var3 = sQueryPool.ceilingEntry(var1);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label173;
         }

         if (var3 != null) {
            label166:
            try {
               sQueryPool.remove(var3.getKey());
               RoomSQLiteQuery var26 = (RoomSQLiteQuery)var3.getValue();
               var26.init(var0, var1);
               return var26;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label166;
            }
         } else {
            label169: {
               try {
                  ;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label169;
               }

               RoomSQLiteQuery var25 = new RoomSQLiteQuery(var1);
               var25.init(var0, var1);
               return var25;
            }
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   private static void prunePoolLocked() {
      if (sQueryPool.size() > 15) {
         int var0 = sQueryPool.size() - 10;

         for(Iterator var1 = sQueryPool.descendingKeySet().iterator(); var0 > 0; --var0) {
            var1.next();
            var1.remove();
         }
      }

   }

   public void bindBlob(int var1, byte[] var2) {
      this.mBindingTypes[var1] = 5;
      this.mBlobBindings[var1] = var2;
   }

   public void bindDouble(int var1, double var2) {
      this.mBindingTypes[var1] = 3;
      this.mDoubleBindings[var1] = var2;
   }

   public void bindLong(int var1, long var2) {
      this.mBindingTypes[var1] = 2;
      this.mLongBindings[var1] = var2;
   }

   public void bindNull(int var1) {
      this.mBindingTypes[var1] = 1;
   }

   public void bindString(int var1, String var2) {
      this.mBindingTypes[var1] = 4;
      this.mStringBindings[var1] = var2;
   }

   public void bindTo(SupportSQLiteProgram var1) {
      for(int var2 = 1; var2 <= this.mArgCount; ++var2) {
         switch(this.mBindingTypes[var2]) {
         case 1:
            var1.bindNull(var2);
            break;
         case 2:
            var1.bindLong(var2, this.mLongBindings[var2]);
            break;
         case 3:
            var1.bindDouble(var2, this.mDoubleBindings[var2]);
            break;
         case 4:
            var1.bindString(var2, this.mStringBindings[var2]);
            break;
         case 5:
            var1.bindBlob(var2, this.mBlobBindings[var2]);
         }
      }

   }

   public void close() {
   }

   public String getSql() {
      return this.mQuery;
   }

   void init(String var1, int var2) {
      this.mQuery = var1;
      this.mArgCount = var2;
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }
}
