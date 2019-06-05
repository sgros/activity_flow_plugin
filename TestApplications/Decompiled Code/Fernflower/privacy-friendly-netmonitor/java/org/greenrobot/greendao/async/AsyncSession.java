package org.greenrobot.greendao.async;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

public class AsyncSession {
   private final AbstractDaoSession daoSession;
   private final AsyncOperationExecutor executor;
   private int sessionFlags;

   public AsyncSession(AbstractDaoSession var1) {
      this.daoSession = var1;
      this.executor = new AsyncOperationExecutor();
   }

   private AsyncOperation enqueEntityOperation(AsyncOperation.OperationType var1, Class var2, Object var3, int var4) {
      AsyncOperation var5 = new AsyncOperation(var1, this.daoSession.getDao(var2), (Database)null, var3, var4 | this.sessionFlags);
      this.executor.enqueue(var5);
      return var5;
   }

   private AsyncOperation enqueueDatabaseOperation(AsyncOperation.OperationType var1, Object var2, int var3) {
      AsyncOperation var4 = new AsyncOperation(var1, (AbstractDao)null, this.daoSession.getDatabase(), var2, var3 | this.sessionFlags);
      this.executor.enqueue(var4);
      return var4;
   }

   private AsyncOperation enqueueEntityOperation(AsyncOperation.OperationType var1, Object var2, int var3) {
      return this.enqueEntityOperation(var1, var2.getClass(), var2, var3);
   }

   public AsyncOperation callInTx(Callable var1) {
      return this.callInTx(var1, 0);
   }

   public AsyncOperation callInTx(Callable var1, int var2) {
      return this.enqueueDatabaseOperation(AsyncOperation.OperationType.TransactionCallable, var1, var2);
   }

   public AsyncOperation count(Class var1) {
      return this.count(var1, 0);
   }

   public AsyncOperation count(Class var1, int var2) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.Count, var1, (Object)null, var2);
   }

   public AsyncOperation delete(Object var1) {
      return this.delete(var1, 0);
   }

   public AsyncOperation delete(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.Delete, var1, var2);
   }

   public AsyncOperation deleteAll(Class var1) {
      return this.deleteAll(var1, 0);
   }

   public AsyncOperation deleteAll(Class var1, int var2) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.DeleteAll, var1, (Object)null, var2);
   }

   public AsyncOperation deleteByKey(Object var1) {
      return this.deleteByKey(var1, 0);
   }

   public AsyncOperation deleteByKey(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.DeleteByKey, var1, var2);
   }

   public AsyncOperation deleteInTx(Class var1, int var2, Object... var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.DeleteInTxArray, var1, var3, var2);
   }

   public AsyncOperation deleteInTx(Class var1, Iterable var2) {
      return this.deleteInTx(var1, var2, 0);
   }

   public AsyncOperation deleteInTx(Class var1, Iterable var2, int var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.DeleteInTxIterable, var1, var2, var3);
   }

   public AsyncOperation deleteInTx(Class var1, Object... var2) {
      return this.deleteInTx(var1, 0, var2);
   }

   public AsyncOperationListener getListener() {
      return this.executor.getListener();
   }

   public AsyncOperationListener getListenerMainThread() {
      return this.executor.getListenerMainThread();
   }

   public int getMaxOperationCountToMerge() {
      return this.executor.getMaxOperationCountToMerge();
   }

   public int getSessionFlags() {
      return this.sessionFlags;
   }

   public int getWaitForMergeMillis() {
      return this.executor.getWaitForMergeMillis();
   }

   public AsyncOperation insert(Object var1) {
      return this.insert(var1, 0);
   }

   public AsyncOperation insert(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.Insert, var1, var2);
   }

   public AsyncOperation insertInTx(Class var1, int var2, Object... var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.InsertInTxArray, var1, var3, var2);
   }

   public AsyncOperation insertInTx(Class var1, Iterable var2) {
      return this.insertInTx(var1, var2, 0);
   }

   public AsyncOperation insertInTx(Class var1, Iterable var2, int var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.InsertInTxIterable, var1, var2, var3);
   }

   public AsyncOperation insertInTx(Class var1, Object... var2) {
      return this.insertInTx(var1, 0, var2);
   }

   public AsyncOperation insertOrReplace(Object var1) {
      return this.insertOrReplace(var1, 0);
   }

   public AsyncOperation insertOrReplace(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.InsertOrReplace, var1, var2);
   }

   public AsyncOperation insertOrReplaceInTx(Class var1, int var2, Object... var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.InsertOrReplaceInTxArray, var1, var3, var2);
   }

   public AsyncOperation insertOrReplaceInTx(Class var1, Iterable var2) {
      return this.insertOrReplaceInTx(var1, var2, 0);
   }

   public AsyncOperation insertOrReplaceInTx(Class var1, Iterable var2, int var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.InsertOrReplaceInTxIterable, var1, var2, var3);
   }

   public AsyncOperation insertOrReplaceInTx(Class var1, Object... var2) {
      return this.insertOrReplaceInTx(var1, 0, var2);
   }

   public boolean isCompleted() {
      return this.executor.isCompleted();
   }

   public AsyncOperation load(Class var1, Object var2) {
      return this.load(var1, var2, 0);
   }

   public AsyncOperation load(Class var1, Object var2, int var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.Load, var1, var2, var3);
   }

   public AsyncOperation loadAll(Class var1) {
      return this.loadAll(var1, 0);
   }

   public AsyncOperation loadAll(Class var1, int var2) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.LoadAll, var1, (Object)null, var2);
   }

   public AsyncOperation queryList(Query var1) {
      return this.queryList(var1, 0);
   }

   public AsyncOperation queryList(Query var1, int var2) {
      return this.enqueueDatabaseOperation(AsyncOperation.OperationType.QueryList, var1, var2);
   }

   public AsyncOperation queryUnique(Query var1) {
      return this.queryUnique(var1, 0);
   }

   public AsyncOperation queryUnique(Query var1, int var2) {
      return this.enqueueDatabaseOperation(AsyncOperation.OperationType.QueryUnique, var1, var2);
   }

   public AsyncOperation refresh(Object var1) {
      return this.refresh(var1, 0);
   }

   public AsyncOperation refresh(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.Refresh, var1, var2);
   }

   public AsyncOperation runInTx(Runnable var1) {
      return this.runInTx(var1, 0);
   }

   public AsyncOperation runInTx(Runnable var1, int var2) {
      return this.enqueueDatabaseOperation(AsyncOperation.OperationType.TransactionRunnable, var1, var2);
   }

   public void setListener(AsyncOperationListener var1) {
      this.executor.setListener(var1);
   }

   public void setListenerMainThread(AsyncOperationListener var1) {
      this.executor.setListenerMainThread(var1);
   }

   public void setMaxOperationCountToMerge(int var1) {
      this.executor.setMaxOperationCountToMerge(var1);
   }

   public void setSessionFlags(int var1) {
      this.sessionFlags = var1;
   }

   public void setWaitForMergeMillis(int var1) {
      this.executor.setWaitForMergeMillis(var1);
   }

   public AsyncOperation update(Object var1) {
      return this.update(var1, 0);
   }

   public AsyncOperation update(Object var1, int var2) {
      return this.enqueueEntityOperation(AsyncOperation.OperationType.Update, var1, var2);
   }

   public AsyncOperation updateInTx(Class var1, int var2, Object... var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.UpdateInTxArray, var1, var3, var2);
   }

   public AsyncOperation updateInTx(Class var1, Iterable var2) {
      return this.updateInTx(var1, var2, 0);
   }

   public AsyncOperation updateInTx(Class var1, Iterable var2, int var3) {
      return this.enqueEntityOperation(AsyncOperation.OperationType.UpdateInTxIterable, var1, var2, var3);
   }

   public AsyncOperation updateInTx(Class var1, Object... var2) {
      return this.updateInTx(var1, 0, var2);
   }

   public void waitForCompletion() {
      this.executor.waitForCompletion();
   }

   public boolean waitForCompletion(int var1) {
      return this.executor.waitForCompletion(var1);
   }
}
