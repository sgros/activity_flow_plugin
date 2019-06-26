package org.greenrobot.greendao.async;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.database.Database;

public class AsyncOperation {
   public static final int FLAG_MERGE_TX = 1;
   public static final int FLAG_STOP_QUEUE_ON_EXCEPTION = 2;
   public static final int FLAG_TRACK_CREATOR_STACKTRACE = 4;
   private volatile boolean completed;
   final Exception creatorStacktrace;
   final AbstractDao dao;
   private final Database database;
   final int flags;
   volatile int mergedOperationsCount;
   final Object parameter;
   volatile Object result;
   int sequenceNumber;
   volatile Throwable throwable;
   volatile long timeCompleted;
   volatile long timeStarted;
   final AsyncOperation.OperationType type;

   AsyncOperation(AsyncOperation.OperationType var1, AbstractDao var2, Database var3, Object var4, int var5) {
      this.type = var1;
      this.flags = var5;
      this.dao = var2;
      this.database = var3;
      this.parameter = var4;
      Exception var6;
      if ((var5 & 4) != 0) {
         var6 = new Exception("AsyncOperation was created here");
      } else {
         var6 = null;
      }

      this.creatorStacktrace = var6;
   }

   public Exception getCreatorStacktrace() {
      return this.creatorStacktrace;
   }

   Database getDatabase() {
      Database var1;
      if (this.database != null) {
         var1 = this.database;
      } else {
         var1 = this.dao.getDatabase();
      }

      return var1;
   }

   public long getDuration() {
      if (this.timeCompleted == 0L) {
         throw new DaoException("This operation did not yet complete");
      } else {
         return this.timeCompleted - this.timeStarted;
      }
   }

   public int getMergedOperationsCount() {
      return this.mergedOperationsCount;
   }

   public Object getParameter() {
      return this.parameter;
   }

   public Object getResult() {
      synchronized(this){}

      Object var1;
      try {
         if (!this.completed) {
            this.waitForCompletion();
         }

         if (this.throwable != null) {
            AsyncDaoException var4 = new AsyncDaoException(this, this.throwable);
            throw var4;
         }

         var1 = this.result;
      } finally {
         ;
      }

      return var1;
   }

   public int getSequenceNumber() {
      return this.sequenceNumber;
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   public long getTimeCompleted() {
      return this.timeCompleted;
   }

   public long getTimeStarted() {
      return this.timeStarted;
   }

   public AsyncOperation.OperationType getType() {
      return this.type;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public boolean isCompletedSucessfully() {
      boolean var1;
      if (this.completed && this.throwable == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isFailed() {
      boolean var1;
      if (this.throwable != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isMergeTx() {
      int var1 = this.flags;
      boolean var2 = true;
      if ((var1 & 1) == 0) {
         var2 = false;
      }

      return var2;
   }

   boolean isMergeableWith(AsyncOperation var1) {
      boolean var2;
      if (var1 != null && this.isMergeTx() && var1.isMergeTx() && this.getDatabase() == var1.getDatabase()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void reset() {
      this.timeStarted = 0L;
      this.timeCompleted = 0L;
      this.completed = false;
      this.throwable = null;
      this.result = null;
      this.mergedOperationsCount = 0;
   }

   void setCompleted() {
      synchronized(this){}

      try {
         this.completed = true;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void setThrowable(Throwable var1) {
      this.throwable = var1;
   }

   public Object waitForCompletion() {
      // $FF: Couldn't be decompiled
   }

   public boolean waitForCompletion(int param1) {
      // $FF: Couldn't be decompiled
   }

   public static enum OperationType {
      Count,
      Delete,
      DeleteAll,
      DeleteByKey,
      DeleteInTxArray,
      DeleteInTxIterable,
      Insert,
      InsertInTxArray,
      InsertInTxIterable,
      InsertOrReplace,
      InsertOrReplaceInTxArray,
      InsertOrReplaceInTxIterable,
      Load,
      LoadAll,
      QueryList,
      QueryUnique,
      Refresh,
      TransactionCallable,
      TransactionRunnable,
      Update,
      UpdateInTxArray,
      UpdateInTxIterable;
   }
}
