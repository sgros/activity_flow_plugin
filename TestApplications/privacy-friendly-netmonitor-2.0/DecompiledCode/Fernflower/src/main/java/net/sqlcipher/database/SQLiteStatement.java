package net.sqlcipher.database;

import android.os.SystemClock;

public class SQLiteStatement extends SQLiteProgram {
   SQLiteStatement(SQLiteDatabase var1, String var2) {
      super(var1, var2);
   }

   private final native long native_1x1_long();

   private final native String native_1x1_string();

   private final native void native_execute();

   public void execute() {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("database ");
         var1.append(this.mDatabase.getPath());
         var1.append(" already closed");
         throw new IllegalStateException(var1.toString());
      } else {
         SystemClock.uptimeMillis();
         this.mDatabase.lock();
         this.acquireReference();

         try {
            this.native_execute();
         } finally {
            this.releaseReference();
            this.mDatabase.unlock();
         }

      }
   }

   public long executeInsert() {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("database ");
         var1.append(this.mDatabase.getPath());
         var1.append(" already closed");
         throw new IllegalStateException(var1.toString());
      } else {
         SystemClock.uptimeMillis();
         this.mDatabase.lock();
         this.acquireReference();
         boolean var5 = false;

         long var2;
         label43: {
            try {
               var5 = true;
               this.native_execute();
               if (this.mDatabase.lastChangeCount() > 0) {
                  var2 = this.mDatabase.lastInsertRow();
                  var5 = false;
                  break label43;
               }

               var5 = false;
            } finally {
               if (var5) {
                  this.releaseReference();
                  this.mDatabase.unlock();
               }
            }

            var2 = -1L;
         }

         this.releaseReference();
         this.mDatabase.unlock();
         return var2;
      }
   }

   public int executeUpdateDelete() {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("database ");
         var1.append(this.mDatabase.getPath());
         var1.append(" already closed");
         throw new IllegalStateException(var1.toString());
      } else {
         SystemClock.uptimeMillis();
         this.mDatabase.lock();
         this.acquireReference();

         int var2;
         try {
            this.native_execute();
            var2 = this.mDatabase.lastChangeCount();
         } finally {
            this.releaseReference();
            this.mDatabase.unlock();
         }

         return var2;
      }
   }

   public long simpleQueryForLong() {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("database ");
         var1.append(this.mDatabase.getPath());
         var1.append(" already closed");
         throw new IllegalStateException(var1.toString());
      } else {
         SystemClock.uptimeMillis();
         this.mDatabase.lock();
         this.acquireReference();

         long var2;
         try {
            var2 = this.native_1x1_long();
         } finally {
            this.releaseReference();
            this.mDatabase.unlock();
         }

         return var2;
      }
   }

   public String simpleQueryForString() {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("database ");
         var4.append(this.mDatabase.getPath());
         var4.append(" already closed");
         throw new IllegalStateException(var4.toString());
      } else {
         SystemClock.uptimeMillis();
         this.mDatabase.lock();
         this.acquireReference();

         String var1;
         try {
            var1 = this.native_1x1_string();
         } finally {
            this.releaseReference();
            this.mDatabase.unlock();
         }

         return var1;
      }
   }
}
