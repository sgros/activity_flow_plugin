// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;

public final class DefaultAllocator implements Allocator
{
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;
    private final Allocation[] singleAllocationReleaseHolder;
    private int targetBufferSize;
    private final boolean trimOnReset;
    
    public DefaultAllocator(final boolean b, final int n) {
        this(b, n, 0);
    }
    
    public DefaultAllocator(final boolean trimOnReset, final int individualAllocationSize, final int availableCount) {
        int i = 0;
        Assertions.checkArgument(individualAllocationSize > 0);
        Assertions.checkArgument(availableCount >= 0);
        this.trimOnReset = trimOnReset;
        this.individualAllocationSize = individualAllocationSize;
        this.availableCount = availableCount;
        this.availableAllocations = new Allocation[availableCount + 100];
        if (availableCount > 0) {
            this.initialAllocationBlock = new byte[availableCount * individualAllocationSize];
            while (i < availableCount) {
                this.availableAllocations[i] = new Allocation(this.initialAllocationBlock, i * individualAllocationSize);
                ++i;
            }
        }
        else {
            this.initialAllocationBlock = null;
        }
        this.singleAllocationReleaseHolder = new Allocation[1];
    }
    
    @Override
    public Allocation allocate() {
        synchronized (this) {
            ++this.allocatedCount;
            Allocation allocation;
            if (this.availableCount > 0) {
                final Allocation[] availableAllocations = this.availableAllocations;
                final int availableCount = this.availableCount - 1;
                this.availableCount = availableCount;
                allocation = availableAllocations[availableCount];
                this.availableAllocations[this.availableCount] = null;
            }
            else {
                allocation = new Allocation(new byte[this.individualAllocationSize], 0);
            }
            return allocation;
        }
    }
    
    @Override
    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
    
    public int getTotalBytesAllocated() {
        synchronized (this) {
            return this.allocatedCount * this.individualAllocationSize;
        }
    }
    
    @Override
    public void release(final Allocation allocation) {
        synchronized (this) {
            this.singleAllocationReleaseHolder[0] = allocation;
            this.release(this.singleAllocationReleaseHolder);
        }
    }
    
    @Override
    public void release(final Allocation[] array) {
        synchronized (this) {
            if (this.availableCount + array.length >= this.availableAllocations.length) {
                this.availableAllocations = Arrays.copyOf(this.availableAllocations, Math.max(this.availableAllocations.length * 2, this.availableCount + array.length));
            }
            for (int length = array.length, i = 0; i < length; ++i) {
                this.availableAllocations[this.availableCount++] = array[i];
            }
            this.allocatedCount -= array.length;
            this.notifyAll();
        }
    }
    
    public void reset() {
        synchronized (this) {
            if (this.trimOnReset) {
                this.setTargetBufferSize(0);
            }
        }
    }
    
    public void setTargetBufferSize(final int targetBufferSize) {
        synchronized (this) {
            final boolean b = targetBufferSize < this.targetBufferSize;
            this.targetBufferSize = targetBufferSize;
            if (b) {
                this.trim();
            }
        }
    }
    
    @Override
    public void trim() {
        synchronized (this) {
            final int ceilDivide = Util.ceilDivide(this.targetBufferSize, this.individualAllocationSize);
            final int allocatedCount = this.allocatedCount;
            int i = 0;
            final int max = Math.max(0, ceilDivide - allocatedCount);
            if (max >= this.availableCount) {
                return;
            }
            int max2 = max;
            if (this.initialAllocationBlock != null) {
                int n = this.availableCount - 1;
                while (i <= n) {
                    final Allocation allocation = this.availableAllocations[i];
                    if (allocation.data == this.initialAllocationBlock) {
                        ++i;
                    }
                    else {
                        final Allocation allocation2 = this.availableAllocations[n];
                        if (allocation2.data != this.initialAllocationBlock) {
                            --n;
                        }
                        else {
                            this.availableAllocations[i] = allocation2;
                            this.availableAllocations[n] = allocation;
                            --n;
                            ++i;
                        }
                    }
                }
                if ((max2 = Math.max(max, i)) >= this.availableCount) {
                    return;
                }
            }
            Arrays.fill(this.availableAllocations, max2, this.availableCount, null);
            this.availableCount = max2;
        }
    }
}
