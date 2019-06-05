package org.greenrobot.greendao.async;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

class AsyncOperationExecutor implements Runnable, Callback {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private int countOperationsCompleted;
    private int countOperationsEnqueued;
    private volatile boolean executorRunning;
    private Handler handlerMainThread;
    private int lastSequenceNumber;
    private volatile AsyncOperationListener listener;
    private volatile AsyncOperationListener listenerMainThread;
    private volatile int maxOperationCountToMerge = 50;
    private final BlockingQueue<AsyncOperation> queue = new LinkedBlockingQueue();
    private volatile int waitForMergeMillis = 50;

    AsyncOperationExecutor() {
    }

    public void enqueue(AsyncOperation asyncOperation) {
        synchronized (this) {
            int i = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = i;
            asyncOperation.sequenceNumber = i;
            this.queue.add(asyncOperation);
            this.countOperationsEnqueued++;
            if (!this.executorRunning) {
                this.executorRunning = true;
                executorService.execute(this);
            }
        }
    }

    public int getMaxOperationCountToMerge() {
        return this.maxOperationCountToMerge;
    }

    public void setMaxOperationCountToMerge(int i) {
        this.maxOperationCountToMerge = i;
    }

    public int getWaitForMergeMillis() {
        return this.waitForMergeMillis;
    }

    public void setWaitForMergeMillis(int i) {
        this.waitForMergeMillis = i;
    }

    public AsyncOperationListener getListener() {
        return this.listener;
    }

    public void setListener(AsyncOperationListener asyncOperationListener) {
        this.listener = asyncOperationListener;
    }

    public AsyncOperationListener getListenerMainThread() {
        return this.listenerMainThread;
    }

    public void setListenerMainThread(AsyncOperationListener asyncOperationListener) {
        this.listenerMainThread = asyncOperationListener;
    }

    public synchronized boolean isCompleted() {
        return this.countOperationsEnqueued == this.countOperationsCompleted;
    }

    public synchronized void waitForCompletion() {
        while (!isCompleted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
    }

    public synchronized boolean waitForCompletion(int i) {
        if (!isCompleted()) {
            try {
                wait((long) i);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
        return isCompleted();
    }

    public void run() {
        while (true) {
            try {
                AsyncOperation asyncOperation = (AsyncOperation) this.queue.poll(1, TimeUnit.SECONDS);
                if (asyncOperation == null) {
                    synchronized (this) {
                        asyncOperation = (AsyncOperation) this.queue.poll();
                        if (asyncOperation == null) {
                            this.executorRunning = false;
                            this.executorRunning = false;
                            return;
                        }
                    }
                }
                if (asyncOperation.isMergeTx()) {
                    AsyncOperation asyncOperation2 = (AsyncOperation) this.queue.poll((long) this.waitForMergeMillis, TimeUnit.MILLISECONDS);
                    if (asyncOperation2 != null) {
                        if (asyncOperation.isMergeableWith(asyncOperation2)) {
                            mergeTxAndExecute(asyncOperation, asyncOperation2);
                        } else {
                            executeOperationAndPostCompleted(asyncOperation);
                            executeOperationAndPostCompleted(asyncOperation2);
                        }
                    }
                }
                executeOperationAndPostCompleted(asyncOperation);
            } catch (InterruptedException e) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Thread.currentThread().getName());
                    stringBuilder.append(" was interruppted");
                    DaoLog.m20w(stringBuilder.toString(), e);
                    return;
                } finally {
                    this.executorRunning = false;
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:20:0x0061, code skipped:
            r3 = false;
     */
    private void mergeTxAndExecute(org.greenrobot.greendao.async.AsyncOperation r7, org.greenrobot.greendao.async.AsyncOperation r8) {
        /*
        r6 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r0.add(r7);
        r0.add(r8);
        r7 = r7.getDatabase();
        r7.beginTransaction();
        r8 = 0;
        r1 = r8;
    L_0x0014:
        r2 = r0.size();	 Catch:{ all -> 0x00b5 }
        r3 = 1;
        if (r1 >= r2) goto L_0x0061;
    L_0x001b:
        r2 = r0.get(r1);	 Catch:{ all -> 0x00b5 }
        r2 = (org.greenrobot.greendao.async.AsyncOperation) r2;	 Catch:{ all -> 0x00b5 }
        r6.executeOperation(r2);	 Catch:{ all -> 0x00b5 }
        r4 = r2.isFailed();	 Catch:{ all -> 0x00b5 }
        if (r4 == 0) goto L_0x002b;
    L_0x002a:
        goto L_0x0061;
    L_0x002b:
        r4 = r0.size();	 Catch:{ all -> 0x00b5 }
        r4 = r4 - r3;
        if (r1 != r4) goto L_0x005e;
    L_0x0032:
        r4 = r6.queue;	 Catch:{ all -> 0x00b5 }
        r4 = r4.peek();	 Catch:{ all -> 0x00b5 }
        r4 = (org.greenrobot.greendao.async.AsyncOperation) r4;	 Catch:{ all -> 0x00b5 }
        r5 = r6.maxOperationCountToMerge;	 Catch:{ all -> 0x00b5 }
        if (r1 >= r5) goto L_0x005a;
    L_0x003e:
        r2 = r2.isMergeableWith(r4);	 Catch:{ all -> 0x00b5 }
        if (r2 == 0) goto L_0x005a;
    L_0x0044:
        r2 = r6.queue;	 Catch:{ all -> 0x00b5 }
        r2 = r2.remove();	 Catch:{ all -> 0x00b5 }
        r2 = (org.greenrobot.greendao.async.AsyncOperation) r2;	 Catch:{ all -> 0x00b5 }
        if (r2 == r4) goto L_0x0056;
    L_0x004e:
        r0 = new org.greenrobot.greendao.DaoException;	 Catch:{ all -> 0x00b5 }
        r1 = "Internal error: peeked op did not match removed op";
        r0.<init>(r1);	 Catch:{ all -> 0x00b5 }
        throw r0;	 Catch:{ all -> 0x00b5 }
    L_0x0056:
        r0.add(r2);	 Catch:{ all -> 0x00b5 }
        goto L_0x005e;
    L_0x005a:
        r7.setTransactionSuccessful();	 Catch:{ all -> 0x00b5 }
        goto L_0x0062;
    L_0x005e:
        r1 = r1 + 1;
        goto L_0x0014;
    L_0x0061:
        r3 = r8;
    L_0x0062:
        r7.endTransaction();	 Catch:{ RuntimeException -> 0x0067 }
        r8 = r3;
        goto L_0x007c;
    L_0x0067:
        r7 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Async transaction could not be ended, success so far was: ";
        r1.append(r2);
        r1.append(r3);
        r1 = r1.toString();
        org.greenrobot.greendao.DaoLog.m16i(r1, r7);
    L_0x007c:
        if (r8 == 0) goto L_0x0098;
    L_0x007e:
        r7 = r0.size();
        r8 = r0.iterator();
    L_0x0086:
        r0 = r8.hasNext();
        if (r0 == 0) goto L_0x00b4;
    L_0x008c:
        r0 = r8.next();
        r0 = (org.greenrobot.greendao.async.AsyncOperation) r0;
        r0.mergedOperationsCount = r7;
        r6.handleOperationCompleted(r0);
        goto L_0x0086;
    L_0x0098:
        r7 = "Reverted merged transaction because one of the operations failed. Executing operations one by one instead...";
        org.greenrobot.greendao.DaoLog.m15i(r7);
        r7 = r0.iterator();
    L_0x00a1:
        r8 = r7.hasNext();
        if (r8 == 0) goto L_0x00b4;
    L_0x00a7:
        r8 = r7.next();
        r8 = (org.greenrobot.greendao.async.AsyncOperation) r8;
        r8.reset();
        r6.executeOperationAndPostCompleted(r8);
        goto L_0x00a1;
    L_0x00b4:
        return;
    L_0x00b5:
        r0 = move-exception;
        r7.endTransaction();	 Catch:{ RuntimeException -> 0x00ba }
        goto L_0x00cf;
    L_0x00ba:
        r7 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Async transaction could not be ended, success so far was: ";
        r1.append(r2);
        r1.append(r8);
        r8 = r1.toString();
        org.greenrobot.greendao.DaoLog.m16i(r8, r7);
    L_0x00cf:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.async.AsyncOperationExecutor.mergeTxAndExecute(org.greenrobot.greendao.async.AsyncOperation, org.greenrobot.greendao.async.AsyncOperation):void");
    }

    private void handleOperationCompleted(AsyncOperation asyncOperation) {
        asyncOperation.setCompleted();
        AsyncOperationListener asyncOperationListener = this.listener;
        if (asyncOperationListener != null) {
            asyncOperationListener.onAsyncOperationCompleted(asyncOperation);
        }
        if (this.listenerMainThread != null) {
            if (this.handlerMainThread == null) {
                this.handlerMainThread = new Handler(Looper.getMainLooper(), this);
            }
            this.handlerMainThread.sendMessage(this.handlerMainThread.obtainMessage(1, asyncOperation));
        }
        synchronized (this) {
            this.countOperationsCompleted++;
            if (this.countOperationsCompleted == this.countOperationsEnqueued) {
                notifyAll();
            }
        }
    }

    private void executeOperationAndPostCompleted(AsyncOperation asyncOperation) {
        executeOperation(asyncOperation);
        handleOperationCompleted(asyncOperation);
    }

    private void executeOperation(AsyncOperation asyncOperation) {
        asyncOperation.timeStarted = System.currentTimeMillis();
        try {
            switch (asyncOperation.type) {
                case Delete:
                    asyncOperation.dao.delete(asyncOperation.parameter);
                    break;
                case DeleteInTxIterable:
                    asyncOperation.dao.deleteInTx((Iterable) asyncOperation.parameter);
                    break;
                case DeleteInTxArray:
                    asyncOperation.dao.deleteInTx((Object[]) asyncOperation.parameter);
                    break;
                case Insert:
                    asyncOperation.dao.insert(asyncOperation.parameter);
                    break;
                case InsertInTxIterable:
                    asyncOperation.dao.insertInTx((Iterable) asyncOperation.parameter);
                    break;
                case InsertInTxArray:
                    asyncOperation.dao.insertInTx((Object[]) asyncOperation.parameter);
                    break;
                case InsertOrReplace:
                    asyncOperation.dao.insertOrReplace(asyncOperation.parameter);
                    break;
                case InsertOrReplaceInTxIterable:
                    asyncOperation.dao.insertOrReplaceInTx((Iterable) asyncOperation.parameter);
                    break;
                case InsertOrReplaceInTxArray:
                    asyncOperation.dao.insertOrReplaceInTx((Object[]) asyncOperation.parameter);
                    break;
                case Update:
                    asyncOperation.dao.update(asyncOperation.parameter);
                    break;
                case UpdateInTxIterable:
                    asyncOperation.dao.updateInTx((Iterable) asyncOperation.parameter);
                    break;
                case UpdateInTxArray:
                    asyncOperation.dao.updateInTx((Object[]) asyncOperation.parameter);
                    break;
                case TransactionRunnable:
                    executeTransactionRunnable(asyncOperation);
                    break;
                case TransactionCallable:
                    executeTransactionCallable(asyncOperation);
                    break;
                case QueryList:
                    asyncOperation.result = ((Query) asyncOperation.parameter).forCurrentThread().list();
                    break;
                case QueryUnique:
                    asyncOperation.result = ((Query) asyncOperation.parameter).forCurrentThread().unique();
                    break;
                case DeleteByKey:
                    asyncOperation.dao.deleteByKey(asyncOperation.parameter);
                    break;
                case DeleteAll:
                    asyncOperation.dao.deleteAll();
                    break;
                case Load:
                    asyncOperation.result = asyncOperation.dao.load(asyncOperation.parameter);
                    break;
                case LoadAll:
                    asyncOperation.result = asyncOperation.dao.loadAll();
                    break;
                case Count:
                    asyncOperation.result = Long.valueOf(asyncOperation.dao.count());
                    break;
                case Refresh:
                    asyncOperation.dao.refresh(asyncOperation.parameter);
                    break;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported operation: ");
                    stringBuilder.append(asyncOperation.type);
                    throw new DaoException(stringBuilder.toString());
            }
        } catch (Throwable th) {
            asyncOperation.throwable = th;
        }
        asyncOperation.timeCompleted = System.currentTimeMillis();
    }

    private void executeTransactionRunnable(AsyncOperation asyncOperation) {
        Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            ((Runnable) asyncOperation.parameter).run();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void executeTransactionCallable(AsyncOperation asyncOperation) throws Exception {
        Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            asyncOperation.result = ((Callable) asyncOperation.parameter).call();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public boolean handleMessage(Message message) {
        AsyncOperationListener asyncOperationListener = this.listenerMainThread;
        if (asyncOperationListener != null) {
            asyncOperationListener.onAsyncOperationCompleted((AsyncOperation) message.obj);
        }
        return false;
    }
}
