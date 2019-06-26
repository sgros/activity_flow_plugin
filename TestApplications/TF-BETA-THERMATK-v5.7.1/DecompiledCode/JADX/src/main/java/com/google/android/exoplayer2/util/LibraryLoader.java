package com.google.android.exoplayer2.util;

public final class LibraryLoader {
    private boolean isAvailable;
    private boolean loadAttempted;
    private String[] nativeLibraries;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x0023 in {6, 13, 14, 18, 21} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public synchronized boolean isAvailable() {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.loadAttempted;	 Catch:{ all -> 0x0020 }
        if (r0 == 0) goto L_0x0009;	 Catch:{ all -> 0x0020 }
        r0 = r5.isAvailable;	 Catch:{ all -> 0x0020 }
        monitor-exit(r5);
        return r0;
        r0 = 1;
        r5.loadAttempted = r0;	 Catch:{ all -> 0x0020 }
        r1 = r5.nativeLibraries;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        r2 = r1.length;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        r3 = 0;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        if (r3 >= r2) goto L_0x001a;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        r4 = r1[r3];	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        java.lang.System.loadLibrary(r4);	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        r3 = r3 + 1;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        goto L_0x0010;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
        r5.isAvailable = r0;	 Catch:{ UnsatisfiedLinkError -> 0x001c }
    L_0x001c:
        r0 = r5.isAvailable;	 Catch:{ all -> 0x0020 }
        monitor-exit(r5);
        return r0;
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.LibraryLoader.isAvailable():boolean");
    }

    public LibraryLoader(String... strArr) {
        this.nativeLibraries = strArr;
    }

    public synchronized void setLibraries(String... strArr) {
        Assertions.checkState(!this.loadAttempted, "Cannot set libraries after loading");
        this.nativeLibraries = strArr;
    }
}
