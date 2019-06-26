// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import java.util.List;

public class ObjectPool<T extends Poolable>
{
    private static int ids;
    private int desiredCapacity;
    private T modelObject;
    private Object[] objects;
    private int objectsPointer;
    private int poolId;
    private float replenishPercentage;
    
    private ObjectPool(final int desiredCapacity, final T modelObject) {
        if (desiredCapacity <= 0) {
            throw new IllegalArgumentException("Object Pool must be instantiated with a capacity greater than 0!");
        }
        this.desiredCapacity = desiredCapacity;
        this.objects = new Object[this.desiredCapacity];
        this.objectsPointer = 0;
        this.modelObject = modelObject;
        this.replenishPercentage = 1.0f;
        this.refillPool();
    }
    
    public static ObjectPool create(final int n, final Poolable poolable) {
        synchronized (ObjectPool.class) {
            final ObjectPool objectPool = new ObjectPool(n, (T)poolable);
            objectPool.poolId = ObjectPool.ids;
            ++ObjectPool.ids;
            return objectPool;
        }
    }
    
    private void refillPool() {
        this.refillPool(this.replenishPercentage);
    }
    
    private void refillPool(final float n) {
        final int n2 = (int)(this.desiredCapacity * n);
        int desiredCapacity;
        if (n2 < 1) {
            desiredCapacity = 1;
        }
        else if ((desiredCapacity = n2) > this.desiredCapacity) {
            desiredCapacity = this.desiredCapacity;
        }
        for (int i = 0; i < desiredCapacity; ++i) {
            this.objects[i] = this.modelObject.instantiate();
        }
        this.objectsPointer = desiredCapacity - 1;
    }
    
    private void resizePool() {
        final int desiredCapacity = this.desiredCapacity;
        this.desiredCapacity *= 2;
        final Object[] objects = new Object[this.desiredCapacity];
        for (int i = 0; i < desiredCapacity; ++i) {
            objects[i] = this.objects[i];
        }
        this.objects = objects;
    }
    
    public T get() {
        synchronized (this) {
            if (this.objectsPointer == -1 && this.replenishPercentage > 0.0f) {
                this.refillPool();
            }
            final Poolable poolable = (Poolable)this.objects[this.objectsPointer];
            poolable.currentOwnerId = Poolable.NO_OWNER;
            --this.objectsPointer;
            return (T)poolable;
        }
    }
    
    public int getPoolCapacity() {
        return this.objects.length;
    }
    
    public int getPoolCount() {
        return this.objectsPointer + 1;
    }
    
    public int getPoolId() {
        return this.poolId;
    }
    
    public float getReplenishPercentage() {
        return this.replenishPercentage;
    }
    
    public void recycle(final T t) {
        synchronized (this) {
            if (t.currentOwnerId == Poolable.NO_OWNER) {
                ++this.objectsPointer;
                if (this.objectsPointer >= this.objects.length) {
                    this.resizePool();
                }
                t.currentOwnerId = this.poolId;
                this.objects[this.objectsPointer] = t;
                return;
            }
            if (t.currentOwnerId == this.poolId) {
                throw new IllegalArgumentException("The object passed is already stored in this pool!");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The object to recycle already belongs to poolId ");
            sb.append(t.currentOwnerId);
            sb.append(".  Object cannot belong to two different pool instances simultaneously!");
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void recycle(final List<T> list) {
        synchronized (this) {
            while (list.size() + this.objectsPointer + 1 > this.desiredCapacity) {
                this.resizePool();
            }
            final int size = list.size();
            int i = 0;
            while (i < size) {
                final Poolable poolable = list.get(i);
                if (poolable.currentOwnerId != Poolable.NO_OWNER) {
                    if (poolable.currentOwnerId == this.poolId) {
                        throw new IllegalArgumentException("The object passed is already stored in this pool!");
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("The object to recycle already belongs to poolId ");
                    sb.append(poolable.currentOwnerId);
                    sb.append(".  Object cannot belong to two different pool instances simultaneously!");
                    throw new IllegalArgumentException(sb.toString());
                }
                else {
                    poolable.currentOwnerId = this.poolId;
                    this.objects[this.objectsPointer + 1 + i] = poolable;
                    ++i;
                }
            }
            this.objectsPointer += size;
        }
    }
    
    public void setReplenishPercentage(final float n) {
        float replenishPercentage;
        if (n > 1.0f) {
            replenishPercentage = 1.0f;
        }
        else {
            replenishPercentage = n;
            if (n < 0.0f) {
                replenishPercentage = 0.0f;
            }
        }
        this.replenishPercentage = replenishPercentage;
    }
    
    public abstract static class Poolable
    {
        public static int NO_OWNER = -1;
        int currentOwnerId;
        
        public Poolable() {
            this.currentOwnerId = Poolable.NO_OWNER;
        }
        
        protected abstract Poolable instantiate();
    }
}
