package org.osmdroid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GEMFFile {
    private int mCurrentSource;
    private final List<String> mFileNames;
    private final List<Long> mFileSizes;
    private final List<RandomAccessFile> mFiles;
    private final String mLocation;
    private final List<GEMFRange> mRangeData;
    private boolean mSourceLimited;
    private final LinkedHashMap<Integer, String> mSources;

    class GEMFInputStream extends InputStream {
        RandomAccessFile raf;
        int remainingBytes;

        public boolean markSupported() {
            return false;
        }

        public long skip(long j) {
            return 0;
        }

        GEMFInputStream(String str, long j, int i) throws IOException {
            this.raf = new RandomAccessFile(str, "r");
            this.raf.seek(j);
            this.remainingBytes = i;
        }

        public int available() {
            return this.remainingBytes;
        }

        public void close() throws IOException {
            this.raf.close();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            RandomAccessFile randomAccessFile = this.raf;
            int i3 = this.remainingBytes;
            if (i2 > i3) {
                i2 = i3;
            }
            int read = randomAccessFile.read(bArr, i, i2);
            this.remainingBytes -= read;
            return read;
        }

        public int read() throws IOException {
            int i = this.remainingBytes;
            if (i > 0) {
                this.remainingBytes = i - 1;
                return this.raf.read();
            }
            throw new IOException("End of stream");
        }
    }

    private class GEMFRange {
        Long offset;
        Integer sourceIndex;
        Integer xMax;
        Integer xMin;
        Integer yMax;
        Integer yMin;
        Integer zoom;

        private GEMFRange() {
        }

        public String toString() {
            return String.format("GEMF Range: source=%d, zoom=%d, x=%d-%d, y=%d-%d, offset=0x%08X", new Object[]{this.sourceIndex, this.zoom, this.xMin, this.xMax, this.yMin, this.yMax, this.offset});
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:19:0x00e7 in {3, 10, 13, 14, 16, 18} preds:[]
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
    private void readHeader() throws java.io.IOException {
        /*
        r8 = this;
        r0 = r8.mFiles;
        r1 = 0;
        r0 = r0.get(r1);
        r0 = (java.io.RandomAccessFile) r0;
        r2 = r8.mFiles;
        r2 = r2.iterator();
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x0029;
        r3 = r2.next();
        r3 = (java.io.RandomAccessFile) r3;
        r4 = r8.mFileSizes;
        r5 = r3.length();
        r3 = java.lang.Long.valueOf(r5);
        r4.add(r3);
        goto L_0x000f;
        r2 = r0.readInt();
        r3 = 4;
        if (r2 != r3) goto L_0x00d0;
        r2 = r0.readInt();
        r3 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r2 != r3) goto L_0x00b9;
        r2 = r0.readInt();
        r3 = 0;
        if (r3 >= r2) goto L_0x005e;
        r4 = r0.readInt();
        r5 = r0.readInt();
        r6 = new byte[r5];
        r0.read(r6, r1, r5);
        r5 = new java.lang.String;
        r5.<init>(r6);
        r6 = r8.mSources;
        r7 = new java.lang.Integer;
        r7.<init>(r4);
        r6.put(r7, r5);
        r3 = r3 + 1;
        goto L_0x003d;
        r2 = r0.readInt();
        if (r1 >= r2) goto L_0x00b8;
        r3 = new org.osmdroid.util.GEMFFile$GEMFRange;
        r4 = 0;
        r3.<init>();
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.zoom = r4;
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.xMin = r4;
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.xMax = r4;
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.yMin = r4;
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.yMax = r4;
        r4 = r0.readInt();
        r4 = java.lang.Integer.valueOf(r4);
        r3.sourceIndex = r4;
        r4 = r0.readLong();
        r4 = java.lang.Long.valueOf(r4);
        r3.offset = r4;
        r4 = r8.mRangeData;
        r4.add(r3);
        r1 = r1 + 1;
        goto L_0x0062;
        return;
        r0 = new java.io.IOException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "Bad tile size: ";
        r1.append(r3);
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        r0 = new java.io.IOException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "Bad file version: ";
        r1.append(r3);
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.util.GEMFFile.readHeader():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:97:0x016c in {17, 18, 20, 29, 30, 31, 42, 46, 48, 51, 53, 55, 57, 59, 61, 63, 65, 71, 73, 77, 79, 80, 81, 83, 87, 89, 93, 95, 96} preds:[]
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
    public java.io.InputStream getInputStream(int r11, int r12, int r13) {
        /*
        r10 = this;
        r0 = r10.mRangeData;
        r0 = r0.iterator();
        r1 = r0.hasNext();
        r2 = 0;
        if (r1 == 0) goto L_0x004a;
        r1 = r0.next();
        r1 = (org.osmdroid.util.GEMFFile.GEMFRange) r1;
        r3 = r1.zoom;
        r3 = r3.intValue();
        if (r13 != r3) goto L_0x0006;
        r3 = r1.xMin;
        r3 = r3.intValue();
        if (r11 < r3) goto L_0x0006;
        r3 = r1.xMax;
        r3 = r3.intValue();
        if (r11 > r3) goto L_0x0006;
        r3 = r1.yMin;
        r3 = r3.intValue();
        if (r12 < r3) goto L_0x0006;
        r3 = r1.yMax;
        r3 = r3.intValue();
        if (r12 > r3) goto L_0x0006;
        r3 = r10.mSourceLimited;
        if (r3 == 0) goto L_0x004b;
        r3 = r1.sourceIndex;
        r3 = r3.intValue();
        r4 = r10.mCurrentSource;
        if (r3 != r4) goto L_0x0006;
        goto L_0x004b;
        r1 = r2;
        if (r1 != 0) goto L_0x004e;
        return r2;
        r13 = r1.yMax;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13.intValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13 + 1;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r1.yMin;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r0.intValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13 - r0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r1.xMin;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r0.intValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 - r0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r1.yMin;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r0.intValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r12 - r0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 * r13;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 + r12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = (long) r11;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3 = 12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 * r3;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r1.offset;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = r13.longValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 + r0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r10.mFiles;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r0 = 0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13.get(r0);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = (java.io.RandomAccessFile) r13;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13.seek(r11);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r13.readLong();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r8 = r13.readInt();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r10.mFiles;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13.get(r0);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = (java.io.RandomAccessFile) r13;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r1 = r10.mFileSizes;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r1 = r1.get(r0);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r1 = (java.lang.Long) r1;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3 = r1.longValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r1 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1));	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        if (r1 <= 0) goto L_0x00dd;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r10.mFileSizes;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r13.size();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3 = r11;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = 0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r13 + -1;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        if (r11 >= r12) goto L_0x00d2;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r10.mFileSizes;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r12.get(r11);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = (java.lang.Long) r12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r5 = r12.longValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        if (r12 <= 0) goto L_0x00d2;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r10.mFileSizes;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r12.get(r11);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = (java.lang.Long) r12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r5 = r12.longValue();	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3 = r3 - r5;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r11 + 1;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        goto L_0x00ae;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r10.mFiles;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = r12.get(r11);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = (java.io.RandomAccessFile) r13;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r6 = r3;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        goto L_0x00df;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r6 = r11;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = 0;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13.seek(r6);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r12 = new org.osmdroid.util.GEMFFile$GEMFInputStream;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r13 = r10.mFileNames;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = r13.get(r11);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r5 = r11;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r5 = (java.lang.String) r5;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3 = r12;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r4 = r10;	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r3.<init>(r5, r6, r8);	 Catch:{ IOException -> 0x0138, all -> 0x0135 }
        r11 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0131, all -> 0x012f }
        r11.<init>();	 Catch:{ IOException -> 0x0131, all -> 0x012f }
        r13 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r13 = new byte[r13];	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r1 = r12.available();	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        if (r1 <= 0) goto L_0x010b;	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r1 = r12.read(r13);	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        if (r1 <= 0) goto L_0x00fb;	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r11.write(r13, r0, r1);	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        goto L_0x00fb;	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r13 = r11.toByteArray();	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r0 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r0.<init>(r13);	 Catch:{ IOException -> 0x0129, all -> 0x0125 }
        r11.close();	 Catch:{ IOException -> 0x0118 }
        goto L_0x011c;
        r11 = move-exception;
        r11.printStackTrace();
        r12.close();	 Catch:{ IOException -> 0x0120 }
        goto L_0x0153;
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x0153;
        r13 = move-exception;
        r2 = r11;
        r11 = r13;
        goto L_0x0157;
        r13 = move-exception;
        r9 = r12;
        r12 = r11;
        r11 = r13;
        r13 = r9;
        goto L_0x013b;
        r11 = move-exception;
        goto L_0x0157;
        r11 = move-exception;
        r13 = r12;
        r12 = r2;
        goto L_0x013b;
        r11 = move-exception;
        r12 = r2;
        goto L_0x0157;
        r11 = move-exception;
        r12 = r2;
        r13 = r12;
        r11.printStackTrace();	 Catch:{ all -> 0x0154 }
        if (r12 == 0) goto L_0x0148;
        r12.close();	 Catch:{ IOException -> 0x0144 }
        goto L_0x0148;
        r11 = move-exception;
        r11.printStackTrace();
        if (r13 == 0) goto L_0x0152;
        r13.close();	 Catch:{ IOException -> 0x014e }
        goto L_0x0152;
        r11 = move-exception;
        r11.printStackTrace();
        r0 = r2;
        return r0;
        r11 = move-exception;
        r2 = r12;
        r12 = r13;
        if (r2 == 0) goto L_0x0161;
        r2.close();	 Catch:{ IOException -> 0x015d }
        goto L_0x0161;
        r13 = move-exception;
        r13.printStackTrace();
        if (r12 == 0) goto L_0x016b;
        r12.close();	 Catch:{ IOException -> 0x0167 }
        goto L_0x016b;
        r12 = move-exception;
        r12.printStackTrace();
        throw r11;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.util.GEMFFile.getInputStream(int, int, int):java.io.InputStream");
    }

    public GEMFFile(File file) throws FileNotFoundException, IOException {
        this(file.getAbsolutePath());
    }

    public GEMFFile(String str) throws FileNotFoundException, IOException {
        this.mFiles = new ArrayList();
        this.mFileNames = new ArrayList();
        this.mRangeData = new ArrayList();
        this.mFileSizes = new ArrayList();
        this.mSources = new LinkedHashMap();
        this.mSourceLimited = false;
        this.mCurrentSource = 0;
        this.mLocation = str;
        openFiles();
        readHeader();
    }

    public void close() throws IOException {
        for (RandomAccessFile close : this.mFiles) {
            close.close();
        }
    }

    private void openFiles() throws FileNotFoundException {
        File file = new File(this.mLocation);
        String str = "r";
        this.mFiles.add(new RandomAccessFile(file, str));
        this.mFileNames.add(file.getPath());
        int i = 0;
        while (true) {
            i++;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mLocation);
            stringBuilder.append("-");
            stringBuilder.append(i);
            File file2 = new File(stringBuilder.toString());
            if (file2.exists()) {
                this.mFiles.add(new RandomAccessFile(file2, str));
                this.mFileNames.add(file2.getPath());
            } else {
                return;
            }
        }
    }

    public String getName() {
        return this.mLocation;
    }
}
