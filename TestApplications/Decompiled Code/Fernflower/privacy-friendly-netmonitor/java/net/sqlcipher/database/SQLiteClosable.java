package net.sqlcipher.database;

public abstract class SQLiteClosable {
   private Object mLock = new Object();
   private int mReferenceCount = 1;

   private String getObjInfo() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" (");
      if (this instanceof SQLiteDatabase) {
         var1.append("database = ");
         var1.append(((SQLiteDatabase)this).getPath());
      } else if (this instanceof SQLiteProgram || this instanceof SQLiteStatement || this instanceof SQLiteQuery) {
         var1.append("mSql = ");
         var1.append(((SQLiteProgram)this).mSql);
      }

      var1.append(") ");
      return var1.toString();
   }

   public void acquireReference() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.mReferenceCount <= 0) {
               StringBuilder var16 = new StringBuilder();
               var16.append("attempt to re-open an already-closed object: ");
               var16.append(this.getObjInfo());
               IllegalStateException var2 = new IllegalStateException(var16.toString());
               throw var2;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            ++this.mReferenceCount;
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   protected abstract void onAllReferencesReleased();

   protected void onAllReferencesReleasedFromContainer() {
   }

   public void releaseReference() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            --this.mReferenceCount;
            if (this.mReferenceCount == 0) {
               this.onAllReferencesReleased();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void releaseReferenceFromContainer() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            --this.mReferenceCount;
            if (this.mReferenceCount == 0) {
               this.onAllReferencesReleasedFromContainer();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }
}
