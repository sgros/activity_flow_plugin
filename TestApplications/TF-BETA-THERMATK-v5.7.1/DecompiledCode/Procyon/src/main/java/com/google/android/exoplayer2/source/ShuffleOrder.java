// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

public interface ShuffleOrder
{
    int getFirstIndex();
    
    int getLastIndex();
    
    int getLength();
    
    int getNextIndex(final int p0);
    
    int getPreviousIndex(final int p0);
    
    public static final class UnshuffledShuffleOrder implements ShuffleOrder
    {
        private final int length;
        
        public UnshuffledShuffleOrder(final int length) {
            this.length = length;
        }
        
        @Override
        public int getFirstIndex() {
            int n;
            if (this.length > 0) {
                n = 0;
            }
            else {
                n = -1;
            }
            return n;
        }
        
        @Override
        public int getLastIndex() {
            int length = this.length;
            if (length > 0) {
                --length;
            }
            else {
                length = -1;
            }
            return length;
        }
        
        @Override
        public int getLength() {
            return this.length;
        }
        
        @Override
        public int getNextIndex(int n) {
            if (++n >= this.length) {
                n = -1;
            }
            return n;
        }
        
        @Override
        public int getPreviousIndex(int n) {
            if (--n < 0) {
                n = -1;
            }
            return n;
        }
    }
}
