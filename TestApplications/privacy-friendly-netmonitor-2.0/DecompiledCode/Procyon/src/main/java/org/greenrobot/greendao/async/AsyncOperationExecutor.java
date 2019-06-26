// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.async;

import java.util.concurrent.TimeUnit;
import android.os.Message;
import java.util.Iterator;
import org.greenrobot.greendao.DaoLog;
import java.util.ArrayList;
import android.os.Looper;
import org.greenrobot.greendao.database.Database;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.DaoException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.os.Handler$Callback;

class AsyncOperationExecutor implements Runnable, Handler$Callback
{
    private static ExecutorService executorService;
    private int countOperationsCompleted;
    private int countOperationsEnqueued;
    private volatile boolean executorRunning;
    private Handler handlerMainThread;
    private int lastSequenceNumber;
    private volatile AsyncOperationListener listener;
    private volatile AsyncOperationListener listenerMainThread;
    private volatile int maxOperationCountToMerge;
    private final BlockingQueue<AsyncOperation> queue;
    private volatile int waitForMergeMillis;
    
    static {
        AsyncOperationExecutor.executorService = Executors.newCachedThreadPool();
    }
    
    AsyncOperationExecutor() {
        this.queue = new LinkedBlockingQueue<AsyncOperation>();
        this.maxOperationCountToMerge = 50;
        this.waitForMergeMillis = 50;
    }
    
    private void executeOperation(final AsyncOperation asyncOperation) {
        asyncOperation.timeStarted = System.currentTimeMillis();
        try {
            switch (AsyncOperationExecutor$1.$SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[asyncOperation.type.ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unsupported operation: ");
                    sb.append(asyncOperation.type);
                    throw new DaoException(sb.toString());
                }
                case 22: {
                    asyncOperation.dao.refresh(asyncOperation.parameter);
                    break;
                }
                case 21: {
                    asyncOperation.result = asyncOperation.dao.count();
                    break;
                }
                case 20: {
                    asyncOperation.result = asyncOperation.dao.loadAll();
                    break;
                }
                case 19: {
                    asyncOperation.result = asyncOperation.dao.load(asyncOperation.parameter);
                    break;
                }
                case 18: {
                    asyncOperation.dao.deleteAll();
                    break;
                }
                case 17: {
                    asyncOperation.dao.deleteByKey(asyncOperation.parameter);
                    break;
                }
                case 16: {
                    asyncOperation.result = ((Query)asyncOperation.parameter).forCurrentThread().unique();
                    break;
                }
                case 15: {
                    asyncOperation.result = ((Query)asyncOperation.parameter).forCurrentThread().list();
                    break;
                }
                case 14: {
                    this.executeTransactionCallable(asyncOperation);
                    break;
                }
                case 13: {
                    this.executeTransactionRunnable(asyncOperation);
                    break;
                }
                case 12: {
                    asyncOperation.dao.updateInTx((Object[])asyncOperation.parameter);
                    break;
                }
                case 11: {
                    asyncOperation.dao.updateInTx((Iterable<Object>)asyncOperation.parameter);
                    break;
                }
                case 10: {
                    asyncOperation.dao.update(asyncOperation.parameter);
                    break;
                }
                case 9: {
                    asyncOperation.dao.insertOrReplaceInTx((Object[])asyncOperation.parameter);
                    break;
                }
                case 8: {
                    asyncOperation.dao.insertOrReplaceInTx((Iterable<Object>)asyncOperation.parameter);
                    break;
                }
                case 7: {
                    asyncOperation.dao.insertOrReplace(asyncOperation.parameter);
                    break;
                }
                case 6: {
                    asyncOperation.dao.insertInTx((Object[])asyncOperation.parameter);
                    break;
                }
                case 5: {
                    asyncOperation.dao.insertInTx((Iterable<Object>)asyncOperation.parameter);
                    break;
                }
                case 4: {
                    asyncOperation.dao.insert(asyncOperation.parameter);
                    break;
                }
                case 3: {
                    asyncOperation.dao.deleteInTx((Object[])asyncOperation.parameter);
                    break;
                }
                case 2: {
                    asyncOperation.dao.deleteInTx((Iterable<Object>)asyncOperation.parameter);
                    break;
                }
                case 1: {
                    asyncOperation.dao.delete(asyncOperation.parameter);
                    break;
                }
            }
        }
        catch (Throwable throwable) {
            asyncOperation.throwable = throwable;
        }
        asyncOperation.timeCompleted = System.currentTimeMillis();
    }
    
    private void executeOperationAndPostCompleted(final AsyncOperation asyncOperation) {
        this.executeOperation(asyncOperation);
        this.handleOperationCompleted(asyncOperation);
    }
    
    private void executeTransactionCallable(final AsyncOperation asyncOperation) throws Exception {
        final Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            asyncOperation.result = ((Callable)asyncOperation.parameter).call();
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }
    
    private void executeTransactionRunnable(final AsyncOperation asyncOperation) {
        final Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            ((Runnable)asyncOperation.parameter).run();
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }
    
    private void handleOperationCompleted(final AsyncOperation asyncOperation) {
        asyncOperation.setCompleted();
        final AsyncOperationListener listener = this.listener;
        if (listener != null) {
            listener.onAsyncOperationCompleted(asyncOperation);
        }
        if (this.listenerMainThread != null) {
            if (this.handlerMainThread == null) {
                this.handlerMainThread = new Handler(Looper.getMainLooper(), (Handler$Callback)this);
            }
            this.handlerMainThread.sendMessage(this.handlerMainThread.obtainMessage(1, (Object)asyncOperation));
        }
        synchronized (this) {
            ++this.countOperationsCompleted;
            if (this.countOperationsCompleted == this.countOperationsEnqueued) {
                this.notifyAll();
            }
        }
    }
    
    private void mergeTxAndExecute(AsyncOperation e, AsyncOperation e2) {
        final ArrayList<AsyncOperation> list = new ArrayList<AsyncOperation>();
        list.add(e);
        list.add(e2);
        e = (AsyncOperation)e.getDatabase();
        ((Database)e).beginTransaction();
        final boolean b = false;
        int index = 0;
        try {
            boolean b2 = false;
            Label_0184: {
                while (true) {
                    final int size = list.size();
                    b2 = true;
                    if (index >= size) {
                        break;
                    }
                    final AsyncOperation asyncOperation = list.get(index);
                    this.executeOperation(asyncOperation);
                    if (asyncOperation.isFailed()) {
                        break;
                    }
                    if (index == list.size() - 1) {
                        e2 = this.queue.peek();
                        if (index >= this.maxOperationCountToMerge || !asyncOperation.isMergeableWith(e2)) {
                            ((Database)e).setTransactionSuccessful();
                            break Label_0184;
                        }
                        final AsyncOperation e3 = this.queue.remove();
                        if (e3 != e2) {
                            throw new DaoException("Internal error: peeked op did not match removed op");
                        }
                        list.add(e3);
                    }
                    ++index;
                }
                b2 = false;
                try {
                    ((Database)e).endTransaction();
                }
                catch (RuntimeException ex) {
                    e = (AsyncOperation)new StringBuilder();
                    ((StringBuilder)e).append("Async transaction could not be ended, success so far was: ");
                    ((StringBuilder)e).append(b2);
                    DaoLog.i(((StringBuilder)e).toString(), ex);
                    b2 = b;
                }
            }
            if (b2) {
                final int size2 = list.size();
                e = (AsyncOperation)list.iterator();
                while (((Iterator)e).hasNext()) {
                    e2 = ((Iterator<AsyncOperation>)e).next();
                    e2.mergedOperationsCount = size2;
                    this.handleOperationCompleted(e2);
                }
            }
            else {
                DaoLog.i("Reverted merged transaction because one of the operations failed. Executing operations one by one instead...");
                final Iterator<AsyncOperation> iterator = list.iterator();
                while (iterator.hasNext()) {
                    e = iterator.next();
                    e.reset();
                    this.executeOperationAndPostCompleted(e);
                }
            }
        }
        finally {
            try {
                ((Database)e).endTransaction();
            }
            catch (RuntimeException ex2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Async transaction could not be ended, success so far was: ");
                sb.append(false);
                DaoLog.i(sb.toString(), ex2);
            }
        }
    }
    
    public void enqueue(final AsyncOperation asyncOperation) {
        synchronized (this) {
            final int n = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = n;
            asyncOperation.sequenceNumber = n;
            this.queue.add(asyncOperation);
            ++this.countOperationsEnqueued;
            if (!this.executorRunning) {
                this.executorRunning = true;
                AsyncOperationExecutor.executorService.execute(this);
            }
        }
    }
    
    public AsyncOperationListener getListener() {
        return this.listener;
    }
    
    public AsyncOperationListener getListenerMainThread() {
        return this.listenerMainThread;
    }
    
    public int getMaxOperationCountToMerge() {
        return this.maxOperationCountToMerge;
    }
    
    public int getWaitForMergeMillis() {
        return this.waitForMergeMillis;
    }
    
    public boolean handleMessage(final Message message) {
        final AsyncOperationListener listenerMainThread = this.listenerMainThread;
        if (listenerMainThread != null) {
            listenerMainThread.onAsyncOperationCompleted((AsyncOperation)message.obj);
        }
        return false;
    }
    
    public boolean isCompleted() {
        synchronized (this) {
            return this.countOperationsEnqueued == this.countOperationsCompleted;
        }
    }
    
    @Override
    public void run() {
        try {
            try {
                while (true) {
                    final AsyncOperation asyncOperation;
                    if ((asyncOperation = this.queue.poll(1L, TimeUnit.SECONDS)) == null) {
                        synchronized (this) {
                            if (this.queue.poll() == null) {
                                this.executorRunning = false;
                                // monitorexit(this)
                                this.executorRunning = false;
                                return;
                            }
                        }
                    }
                    if (asyncOperation.isMergeTx()) {
                        final AsyncOperation asyncOperation2 = this.queue.poll(this.waitForMergeMillis, TimeUnit.MILLISECONDS);
                        if (asyncOperation2 != null) {
                            if (asyncOperation.isMergeableWith(asyncOperation2)) {
                                this.mergeTxAndExecute(asyncOperation, asyncOperation2);
                                continue;
                            }
                            this.executeOperationAndPostCompleted(asyncOperation);
                            this.executeOperationAndPostCompleted(asyncOperation2);
                            continue;
                        }
                    }
                    this.executeOperationAndPostCompleted(asyncOperation);
                }
            }
            finally {}
        }
        catch (InterruptedException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Thread.currentThread().getName());
            sb.append(" was interruppted");
            DaoLog.w(sb.toString(), ex);
            this.executorRunning = false;
            return;
        }
        this.executorRunning = false;
    }
    
    public void setListener(final AsyncOperationListener listener) {
        this.listener = listener;
    }
    
    public void setListenerMainThread(final AsyncOperationListener listenerMainThread) {
        this.listenerMainThread = listenerMainThread;
    }
    
    public void setMaxOperationCountToMerge(final int maxOperationCountToMerge) {
        this.maxOperationCountToMerge = maxOperationCountToMerge;
    }
    
    public void setWaitForMergeMillis(final int waitForMergeMillis) {
        this.waitForMergeMillis = waitForMergeMillis;
    }
    
    public void waitForCompletion() {
        synchronized (this) {
            while (!this.isCompleted()) {
                try {
                    this.wait();
                    continue;
                }
                catch (InterruptedException ex) {
                    throw new DaoException("Interrupted while waiting for all operations to complete", ex);
                }
                break;
            }
        }
    }
    
    public boolean waitForCompletion(final int n) {
        synchronized (this) {
            if (!this.isCompleted()) {
                final long n2 = n;
                try {
                    this.wait(n2);
                }
                catch (InterruptedException ex) {
                    throw new DaoException("Interrupted while waiting for all operations to complete", ex);
                }
            }
            return this.isCompleted();
        }
    }
}
