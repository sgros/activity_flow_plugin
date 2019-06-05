// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

final class DiskCacheWriteLocker
{
    private final Map<String, WriteLock> locks;
    private final WriteLockPool writeLockPool;
    
    DiskCacheWriteLocker() {
        this.locks = new HashMap<String, WriteLock>();
        this.writeLockPool = new WriteLockPool();
    }
    
    void acquire(final String s) {
        synchronized (this) {
            Object obtain;
            if ((obtain = this.locks.get(s)) == null) {
                obtain = this.writeLockPool.obtain();
                this.locks.put(s, (WriteLock)obtain);
            }
            ++((WriteLock)obtain).interestedThreads;
            // monitorexit(this)
            ((WriteLock)obtain).lock.lock();
        }
    }
    
    void release(final String s) {
        synchronized (this) {
            final WriteLock writeLock = Preconditions.checkNotNull(this.locks.get(s));
            if (writeLock.interestedThreads >= 1) {
                --writeLock.interestedThreads;
                if (writeLock.interestedThreads == 0) {
                    final WriteLock obj = this.locks.remove(s);
                    if (!obj.equals(writeLock)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Removed the wrong lock, expected to remove: ");
                        sb.append(writeLock);
                        sb.append(", but actually removed: ");
                        sb.append(obj);
                        sb.append(", safeKey: ");
                        sb.append(s);
                        throw new IllegalStateException(sb.toString());
                    }
                    this.writeLockPool.offer(obj);
                }
                // monitorexit(this)
                writeLock.lock.unlock();
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot release a lock that is not held, safeKey: ");
            sb2.append(s);
            sb2.append(", interestedThreads: ");
            sb2.append(writeLock.interestedThreads);
            throw new IllegalStateException(sb2.toString());
        }
    }
    
    private static class WriteLock
    {
        int interestedThreads;
        final Lock lock;
        
        WriteLock() {
            this.lock = new ReentrantLock();
        }
    }
    
    private static class WriteLockPool
    {
        private final Queue<WriteLock> pool;
        
        WriteLockPool() {
            this.pool = new ArrayDeque<WriteLock>();
        }
        
        WriteLock obtain() {
            Object pool = this.pool;
            synchronized (pool) {
                final WriteLock writeLock = this.pool.poll();
                // monitorexit(pool)
                pool = writeLock;
                if (writeLock == null) {
                    pool = new WriteLock();
                }
                return (WriteLock)pool;
            }
        }
        
        void offer(final WriteLock writeLock) {
            synchronized (this.pool) {
                if (this.pool.size() < 10) {
                    this.pool.offer(writeLock);
                }
            }
        }
    }
}
