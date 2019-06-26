package org.telegram.messenger;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LruCache<T> {
    private final LinkedHashMap<String, T> map;
    private final LinkedHashMap<String, ArrayList<String>> mapFilters;
    private int maxSize;
    private int size;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:28:0x0076 in {9, 14, 21, 22, 24, 27} preds:[]
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
    private void trimToSize(int r9, java.lang.String r10) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.map;	 Catch:{ all -> 0x0073 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0073 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0073 }
        r1 = r0.hasNext();	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x0071;	 Catch:{ all -> 0x0073 }
        r1 = r8.size;	 Catch:{ all -> 0x0073 }
        if (r1 <= r9) goto L_0x0071;	 Catch:{ all -> 0x0073 }
        r1 = r8.map;	 Catch:{ all -> 0x0073 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x001e;	 Catch:{ all -> 0x0073 }
        goto L_0x0071;	 Catch:{ all -> 0x0073 }
        r1 = r0.next();	 Catch:{ all -> 0x0073 }
        r1 = (java.util.Map.Entry) r1;	 Catch:{ all -> 0x0073 }
        r2 = r1.getKey();	 Catch:{ all -> 0x0073 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x0073 }
        if (r10 == 0) goto L_0x0033;	 Catch:{ all -> 0x0073 }
        r3 = r10.equals(r2);	 Catch:{ all -> 0x0073 }
        if (r3 == 0) goto L_0x0033;	 Catch:{ all -> 0x0073 }
        goto L_0x000b;	 Catch:{ all -> 0x0073 }
        r1 = r1.getValue();	 Catch:{ all -> 0x0073 }
        r3 = r8.size;	 Catch:{ all -> 0x0073 }
        r4 = r8.safeSizeOf(r2, r1);	 Catch:{ all -> 0x0073 }
        r3 = r3 - r4;	 Catch:{ all -> 0x0073 }
        r8.size = r3;	 Catch:{ all -> 0x0073 }
        r0.remove();	 Catch:{ all -> 0x0073 }
        r3 = "@";	 Catch:{ all -> 0x0073 }
        r3 = r2.split(r3);	 Catch:{ all -> 0x0073 }
        r4 = r3.length;	 Catch:{ all -> 0x0073 }
        r5 = 1;	 Catch:{ all -> 0x0073 }
        if (r4 <= r5) goto L_0x006c;	 Catch:{ all -> 0x0073 }
        r4 = r8.mapFilters;	 Catch:{ all -> 0x0073 }
        r6 = 0;	 Catch:{ all -> 0x0073 }
        r7 = r3[r6];	 Catch:{ all -> 0x0073 }
        r4 = r4.get(r7);	 Catch:{ all -> 0x0073 }
        r4 = (java.util.ArrayList) r4;	 Catch:{ all -> 0x0073 }
        if (r4 == 0) goto L_0x006c;	 Catch:{ all -> 0x0073 }
        r7 = r3[r5];	 Catch:{ all -> 0x0073 }
        r4.remove(r7);	 Catch:{ all -> 0x0073 }
        r4 = r4.isEmpty();	 Catch:{ all -> 0x0073 }
        if (r4 == 0) goto L_0x006c;	 Catch:{ all -> 0x0073 }
        r4 = r8.mapFilters;	 Catch:{ all -> 0x0073 }
        r3 = r3[r6];	 Catch:{ all -> 0x0073 }
        r4.remove(r3);	 Catch:{ all -> 0x0073 }
        r3 = 0;	 Catch:{ all -> 0x0073 }
        r8.entryRemoved(r5, r2, r1, r3);	 Catch:{ all -> 0x0073 }
        goto L_0x000b;	 Catch:{ all -> 0x0073 }
        monitor-exit(r8);	 Catch:{ all -> 0x0073 }
        return;	 Catch:{ all -> 0x0073 }
        r9 = move-exception;	 Catch:{ all -> 0x0073 }
        monitor-exit(r8);	 Catch:{ all -> 0x0073 }
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.LruCache.trimToSize(int, java.lang.String):void");
    }

    /* Access modifiers changed, original: protected */
    public void entryRemoved(boolean z, String str, T t, T t2) {
    }

    /* Access modifiers changed, original: protected */
    public int sizeOf(String str, T t) {
        return 1;
    }

    public LruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap(0, 0.75f, true);
            this.mapFilters = new LinkedHashMap();
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    public final T get(String str) {
        if (str != null) {
            synchronized (this) {
                Object obj = this.map.get(str);
                if (obj != null) {
                    return obj;
                }
                return null;
            }
        }
        throw new NullPointerException("key == null");
    }

    public ArrayList<String> getFilterKeys(String str) {
        ArrayList arrayList = (ArrayList) this.mapFilters.get(str);
        return arrayList != null ? new ArrayList(arrayList) : null;
    }

    public T put(String str, T t) {
        if (str == null || t == null) {
            throw new NullPointerException("key == null || value == null");
        }
        Object put;
        synchronized (this) {
            this.size += safeSizeOf(str, t);
            put = this.map.put(str, t);
            if (put != null) {
                this.size -= safeSizeOf(str, put);
            }
        }
        String[] split = str.split("@");
        if (split.length > 1) {
            ArrayList arrayList = (ArrayList) this.mapFilters.get(split[0]);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mapFilters.put(split[0], arrayList);
            }
            if (!arrayList.contains(split[1])) {
                arrayList.add(split[1]);
            }
        }
        if (put != null) {
            entryRemoved(false, str, put, t);
        }
        trimToSize(this.maxSize, str);
        return put;
    }

    public final T remove(String str) {
        if (str != null) {
            Object remove;
            synchronized (this) {
                remove = this.map.remove(str);
                if (remove != null) {
                    this.size -= safeSizeOf(str, remove);
                }
            }
            if (remove != null) {
                String[] split = str.split("@");
                if (split.length > 1) {
                    ArrayList arrayList = (ArrayList) this.mapFilters.get(split[0]);
                    if (arrayList != null) {
                        arrayList.remove(split[1]);
                        if (arrayList.isEmpty()) {
                            this.mapFilters.remove(split[0]);
                        }
                    }
                }
                entryRemoved(false, str, remove, null);
            }
            return remove;
        }
        throw new NullPointerException("key == null");
    }

    public boolean contains(String str) {
        return this.map.containsKey(str);
    }

    private int safeSizeOf(String str, T t) {
        int sizeOf = sizeOf(str, t);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Negative size: ");
        stringBuilder.append(str);
        stringBuilder.append("=");
        stringBuilder.append(t);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public final void evictAll() {
        trimToSize(-1, null);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }
}
