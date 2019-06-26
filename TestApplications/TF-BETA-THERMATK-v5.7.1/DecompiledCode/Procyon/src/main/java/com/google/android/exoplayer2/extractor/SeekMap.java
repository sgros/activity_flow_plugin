// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Assertions;

public interface SeekMap
{
    long getDurationUs();
    
    SeekPoints getSeekPoints(final long p0);
    
    boolean isSeekable();
    
    public static final class SeekPoints
    {
        public final SeekPoint first;
        public final SeekPoint second;
        
        public SeekPoints(final SeekPoint seekPoint) {
            this(seekPoint, seekPoint);
        }
        
        public SeekPoints(final SeekPoint seekPoint, final SeekPoint seekPoint2) {
            Assertions.checkNotNull(seekPoint);
            this.first = seekPoint;
            Assertions.checkNotNull(seekPoint2);
            this.second = seekPoint2;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && SeekPoints.class == o.getClass()) {
                final SeekPoints seekPoints = (SeekPoints)o;
                if (!this.first.equals(seekPoints.first) || !this.second.equals(seekPoints.second)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.first.hashCode() * 31 + this.second.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.first);
            String string;
            if (this.first.equals(this.second)) {
                string = "";
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(", ");
                sb2.append(this.second);
                string = sb2.toString();
            }
            sb.append(string);
            sb.append("]");
            return sb.toString();
        }
    }
    
    public static final class Unseekable implements SeekMap
    {
        private final long durationUs;
        private final SeekPoints startSeekPoints;
        
        public Unseekable(final long n) {
            this(n, 0L);
        }
        
        public Unseekable(final long durationUs, final long n) {
            this.durationUs = durationUs;
            SeekPoint start;
            if (n == 0L) {
                start = SeekPoint.START;
            }
            else {
                start = new SeekPoint(0L, n);
            }
            this.startSeekPoints = new SeekPoints(start);
        }
        
        @Override
        public long getDurationUs() {
            return this.durationUs;
        }
        
        @Override
        public SeekPoints getSeekPoints(final long n) {
            return this.startSeekPoints;
        }
        
        @Override
        public boolean isSeekable() {
            return false;
        }
    }
}
