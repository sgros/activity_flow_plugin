// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.collections.IntIterator;

public final class IntProgressionIterator extends IntIterator
{
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;
    
    public IntProgressionIterator(int finalElement, final int finalElement2, int step) {
        this.step = step;
        this.finalElement = finalElement2;
        step = this.step;
        boolean hasNext = false;
        Label_0045: {
            if (step > 0) {
                if (finalElement > finalElement2) {
                    break Label_0045;
                }
            }
            else if (finalElement < finalElement2) {
                break Label_0045;
            }
            hasNext = true;
        }
        this.hasNext = hasNext;
        if (!this.hasNext) {
            finalElement = this.finalElement;
        }
        this.next = finalElement;
    }
    
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public int nextInt() {
        final int next = this.next;
        if (next == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        }
        else {
            this.next += this.step;
        }
        return next;
    }
}
