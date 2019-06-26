// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import java.nio.ByteOrder;
import android.graphics.PointF;
import java.nio.ByteBuffer;

public class RenderState
{
    private static final int DEFAULT_STATE_SIZE = 256;
    private int allocatedCount;
    public float alpha;
    public float angle;
    public float baseWeight;
    private ByteBuffer buffer;
    private int count;
    public double remainder;
    public float scale;
    public float spacing;
    
    public boolean addPoint(final PointF pointF, final float n, final float n2, final float n3, final int n4) {
        if ((n4 != -1 && n4 >= this.allocatedCount) || this.buffer.position() == this.buffer.limit()) {
            this.resizeBuffer();
            return false;
        }
        if (n4 != -1) {
            this.buffer.position(n4 * 5 * 4);
        }
        this.buffer.putFloat(pointF.x);
        this.buffer.putFloat(pointF.y);
        this.buffer.putFloat(n);
        this.buffer.putFloat(n2);
        this.buffer.putFloat(n3);
        return true;
    }
    
    public void appendValuesCount(int count) {
        count += this.count;
        if (count > this.allocatedCount || this.buffer == null) {
            this.resizeBuffer();
        }
        this.count = count;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void prepare() {
        this.count = 0;
        if (this.buffer != null) {
            return;
        }
        this.allocatedCount = 256;
        (this.buffer = ByteBuffer.allocateDirect(this.allocatedCount * 5 * 4)).order(ByteOrder.nativeOrder());
        this.buffer.position(0);
    }
    
    public float read() {
        return this.buffer.getFloat();
    }
    
    public void reset() {
        this.count = 0;
        this.remainder = 0.0;
        final ByteBuffer buffer = this.buffer;
        if (buffer != null) {
            buffer.position(0);
        }
    }
    
    public void resizeBuffer() {
        if (this.buffer != null) {
            this.buffer = null;
        }
        this.allocatedCount = Math.max(this.allocatedCount * 2, 256);
        (this.buffer = ByteBuffer.allocateDirect(this.allocatedCount * 5 * 4)).order(ByteOrder.nativeOrder());
        this.buffer.position(0);
    }
    
    public void setPosition(final int n) {
        final ByteBuffer buffer = this.buffer;
        if (buffer != null && n >= 0) {
            if (n < this.allocatedCount) {
                buffer.position(n * 5 * 4);
            }
        }
    }
}
