package androidx.collection;

import java.util.LinkedHashMap;
import java.util.Locale;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    /* Access modifiers changed, original: protected */
    public V create(K k) {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void entryRemoved(boolean z, K k, V v, V v2) {
    }

    /* Access modifiers changed, original: protected */
    public int sizeOf(K k, V v) {
        return 1;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x0074 in {7, 12, 15, 18, 20, 23} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void trimToSize(int r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.size;	 Catch:{ all -> 0x0071 }
        if (r0 < 0) goto L_0x0052;	 Catch:{ all -> 0x0071 }
        r0 = r4.map;	 Catch:{ all -> 0x0071 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0071 }
        if (r0 == 0) goto L_0x0011;	 Catch:{ all -> 0x0071 }
        r0 = r4.size;	 Catch:{ all -> 0x0071 }
        if (r0 != 0) goto L_0x0052;	 Catch:{ all -> 0x0071 }
        r0 = r4.size;	 Catch:{ all -> 0x0071 }
        if (r0 <= r5) goto L_0x0050;	 Catch:{ all -> 0x0071 }
        r0 = r4.map;	 Catch:{ all -> 0x0071 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0071 }
        if (r0 == 0) goto L_0x001e;	 Catch:{ all -> 0x0071 }
        goto L_0x0050;	 Catch:{ all -> 0x0071 }
        r0 = r4.map;	 Catch:{ all -> 0x0071 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0071 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0071 }
        r0 = r0.next();	 Catch:{ all -> 0x0071 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0071 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0071 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0071 }
        r2 = r4.map;	 Catch:{ all -> 0x0071 }
        r2.remove(r1);	 Catch:{ all -> 0x0071 }
        r2 = r4.size;	 Catch:{ all -> 0x0071 }
        r3 = r4.safeSizeOf(r1, r0);	 Catch:{ all -> 0x0071 }
        r2 = r2 - r3;	 Catch:{ all -> 0x0071 }
        r4.size = r2;	 Catch:{ all -> 0x0071 }
        r2 = r4.evictionCount;	 Catch:{ all -> 0x0071 }
        r3 = 1;	 Catch:{ all -> 0x0071 }
        r2 = r2 + r3;	 Catch:{ all -> 0x0071 }
        r4.evictionCount = r2;	 Catch:{ all -> 0x0071 }
        monitor-exit(r4);	 Catch:{ all -> 0x0071 }
        r2 = 0;
        r4.entryRemoved(r3, r1, r0, r2);
        goto L_0x0000;
        monitor-exit(r4);	 Catch:{ all -> 0x0071 }
        return;	 Catch:{ all -> 0x0071 }
        r5 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0071 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
        r0.<init>();	 Catch:{ all -> 0x0071 }
        r1 = r4.getClass();	 Catch:{ all -> 0x0071 }
        r1 = r1.getName();	 Catch:{ all -> 0x0071 }
        r0.append(r1);	 Catch:{ all -> 0x0071 }
        r1 = ".sizeOf() is reporting inconsistent results!";	 Catch:{ all -> 0x0071 }
        r0.append(r1);	 Catch:{ all -> 0x0071 }
        r0 = r0.toString();	 Catch:{ all -> 0x0071 }
        r5.<init>(r0);	 Catch:{ all -> 0x0071 }
        throw r5;	 Catch:{ all -> 0x0071 }
        r5 = move-exception;	 Catch:{ all -> 0x0071 }
        monitor-exit(r4);	 Catch:{ all -> 0x0071 }
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LruCache.trimToSize(int):void");
    }

    public LruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0 = create(r5);
     */
    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            if (r0 != null) goto L_0x0022;
     */
    /* JADX WARNING: Missing block: B:13:0x0021, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:14:0x0022, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            r4.createCount++;
            r1 = r4.map.put(r5, r0);
     */
    /* JADX WARNING: Missing block: B:17:0x002f, code skipped:
            if (r1 == null) goto L_0x0037;
     */
    /* JADX WARNING: Missing block: B:18:0x0031, code skipped:
            r4.map.put(r5, r1);
     */
    /* JADX WARNING: Missing block: B:19:0x0037, code skipped:
            r4.size += safeSizeOf(r5, r0);
     */
    /* JADX WARNING: Missing block: B:20:0x0040, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:21:0x0041, code skipped:
            if (r1 == null) goto L_0x0048;
     */
    /* JADX WARNING: Missing block: B:22:0x0043, code skipped:
            entryRemoved(false, r5, r0, r1);
     */
    /* JADX WARNING: Missing block: B:23:0x0047, code skipped:
            return r1;
     */
    /* JADX WARNING: Missing block: B:24:0x0048, code skipped:
            trimToSize(r4.maxSize);
     */
    /* JADX WARNING: Missing block: B:25:0x004d, code skipped:
            return r0;
     */
    public final V get(K r5) {
        /*
        r4 = this;
        if (r5 == 0) goto L_0x0054;
    L_0x0002:
        monitor-enter(r4);
        r0 = r4.map;	 Catch:{ all -> 0x0051 }
        r0 = r0.get(r5);	 Catch:{ all -> 0x0051 }
        if (r0 == 0) goto L_0x0013;
    L_0x000b:
        r5 = r4.hitCount;	 Catch:{ all -> 0x0051 }
        r5 = r5 + 1;
        r4.hitCount = r5;	 Catch:{ all -> 0x0051 }
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
        return r0;
    L_0x0013:
        r0 = r4.missCount;	 Catch:{ all -> 0x0051 }
        r0 = r0 + 1;
        r4.missCount = r0;	 Catch:{ all -> 0x0051 }
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
        r0 = r4.create(r5);
        if (r0 != 0) goto L_0x0022;
    L_0x0020:
        r5 = 0;
        return r5;
    L_0x0022:
        monitor-enter(r4);
        r1 = r4.createCount;	 Catch:{ all -> 0x004e }
        r1 = r1 + 1;
        r4.createCount = r1;	 Catch:{ all -> 0x004e }
        r1 = r4.map;	 Catch:{ all -> 0x004e }
        r1 = r1.put(r5, r0);	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0037;
    L_0x0031:
        r2 = r4.map;	 Catch:{ all -> 0x004e }
        r2.put(r5, r1);	 Catch:{ all -> 0x004e }
        goto L_0x0040;
    L_0x0037:
        r2 = r4.size;	 Catch:{ all -> 0x004e }
        r3 = r4.safeSizeOf(r5, r0);	 Catch:{ all -> 0x004e }
        r2 = r2 + r3;
        r4.size = r2;	 Catch:{ all -> 0x004e }
    L_0x0040:
        monitor-exit(r4);	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0048;
    L_0x0043:
        r2 = 0;
        r4.entryRemoved(r2, r5, r0, r1);
        return r1;
    L_0x0048:
        r5 = r4.maxSize;
        r4.trimToSize(r5);
        return r0;
    L_0x004e:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x004e }
        throw r5;
    L_0x0051:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
        throw r5;
    L_0x0054:
        r5 = new java.lang.NullPointerException;
        r0 = "key == null";
        r5.<init>(r0);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LruCache.get(java.lang.Object):java.lang.Object");
    }

    public final V put(K k, V v) {
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        Object put;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(k, v);
            put = this.map.put(k, v);
            if (put != null) {
                this.size -= safeSizeOf(k, put);
            }
        }
        if (put != null) {
            entryRemoved(false, k, put, v);
        }
        trimToSize(this.maxSize);
        return put;
    }

    private int safeSizeOf(K k, V v) {
        int sizeOf = sizeOf(k, v);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Negative size: ");
        stringBuilder.append(k);
        stringBuilder.append("=");
        stringBuilder.append(v);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public final synchronized String toString() {
        int i;
        i = this.hitCount + this.missCount;
        i = i != 0 ? (this.hitCount * 100) / i : 0;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(i)});
    }
}
