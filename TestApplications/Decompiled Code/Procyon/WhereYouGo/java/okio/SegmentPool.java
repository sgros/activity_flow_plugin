// 
// Decompiled by Procyon v0.5.34
// 

package okio;

final class SegmentPool
{
    static final long MAX_SIZE = 65536L;
    static long byteCount;
    static Segment next;
    
    private SegmentPool() {
    }
    
    static void recycle(final Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        }
        if (!segment.shared) {
            synchronized (SegmentPool.class) {
                if (SegmentPool.byteCount + 8192L > 65536L) {
                    return;
                }
            }
            SegmentPool.byteCount += 8192L;
            final Segment next;
            next.next = SegmentPool.next;
            next.limit = 0;
            next.pos = 0;
            SegmentPool.next = next;
        }
        // monitorexit(SegmentPool.class)
    }
    
    static Segment take() {
        synchronized (SegmentPool.class) {
            Segment next;
            if (SegmentPool.next != null) {
                next = SegmentPool.next;
                SegmentPool.next = next.next;
                next.next = null;
                SegmentPool.byteCount -= 8192L;
            }
            else {
                // monitorexit(SegmentPool.class)
                next = new Segment();
            }
            return next;
        }
    }
}
