package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
    private static final AtomicReference<byte[]> BUFFER_REF = new AtomicReference();

    private static class ByteBufferStream extends InputStream {
        private final ByteBuffer byteBuffer;
        private int markPos = -1;

        public boolean markSupported() {
            return true;
        }

        public ByteBufferStream(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
        }

        public int available() throws IOException {
            return this.byteBuffer.remaining();
        }

        public int read() throws IOException {
            if (this.byteBuffer.hasRemaining()) {
                return this.byteBuffer.get();
            }
            return -1;
        }

        public synchronized void mark(int i) {
            this.markPos = this.byteBuffer.position();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            i2 = Math.min(i2, available());
            this.byteBuffer.get(bArr, i, i2);
            return i2;
        }

        public synchronized void reset() throws IOException {
            if (this.markPos != -1) {
                this.byteBuffer.position(this.markPos);
            } else {
                throw new IOException("Cannot reset to unset mark position");
            }
        }

        public long skip(long j) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            j = Math.min(j, (long) available());
            this.byteBuffer.position((int) (((long) this.byteBuffer.position()) + j));
            return j;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0029 */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0040 A:{SYNTHETIC, Splitter:B:25:0x0040} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0047 A:{SYNTHETIC, Splitter:B:29:0x0047} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0040 A:{SYNTHETIC, Splitter:B:25:0x0040} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0047 A:{SYNTHETIC, Splitter:B:29:0x0047} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:4|5|6|7|8|(2:10|11)|12|13|14) */
    public static java.nio.ByteBuffer fromFile(java.io.File r9) throws java.io.IOException {
        /*
        r0 = 0;
        r5 = r9.length();	 Catch:{ all -> 0x003c }
        r1 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r1 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x0034;
    L_0x000c:
        r7 = new java.io.RandomAccessFile;	 Catch:{ all -> 0x003c }
        r1 = "r";
        r7.<init>(r9, r1);	 Catch:{ all -> 0x003c }
        r9 = r7.getChannel();	 Catch:{ all -> 0x0032 }
        r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ all -> 0x002d }
        r3 = 0;
        r1 = r9;
        r0 = r1.map(r2, r3, r5);	 Catch:{ all -> 0x002d }
        r0 = r0.load();	 Catch:{ all -> 0x002d }
        if (r9 == 0) goto L_0x0029;
    L_0x0026:
        r9.close();	 Catch:{ IOException -> 0x0029 }
    L_0x0029:
        r7.close();	 Catch:{ IOException -> 0x002c }
    L_0x002c:
        return r0;
    L_0x002d:
        r0 = move-exception;
        r8 = r0;
        r0 = r9;
        r9 = r8;
        goto L_0x003e;
    L_0x0032:
        r9 = move-exception;
        goto L_0x003e;
    L_0x0034:
        r9 = new java.io.IOException;	 Catch:{ all -> 0x003c }
        r1 = "File too large to map into memory";
        r9.<init>(r1);	 Catch:{ all -> 0x003c }
        throw r9;	 Catch:{ all -> 0x003c }
    L_0x003c:
        r9 = move-exception;
        r7 = r0;
    L_0x003e:
        if (r0 == 0) goto L_0x0045;
    L_0x0040:
        r0.close();	 Catch:{ IOException -> 0x0044 }
        goto L_0x0045;
    L_0x0045:
        if (r7 == 0) goto L_0x004a;
    L_0x0047:
        r7.close();	 Catch:{ IOException -> 0x004a }
    L_0x004a:
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.util.ByteBufferUtil.fromFile(java.io.File):java.nio.ByteBuffer");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002f A:{SYNTHETIC, Splitter:B:19:0x002f} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0036 A:{SYNTHETIC, Splitter:B:23:0x0036} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x002f A:{SYNTHETIC, Splitter:B:19:0x002f} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0036 A:{SYNTHETIC, Splitter:B:23:0x0036} */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0021 */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|6|(2:8|9)|10|11|27) */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            return;
     */
    public static void toFile(java.nio.ByteBuffer r4, java.io.File r5) throws java.io.IOException {
        /*
        r0 = 0;
        r4.position(r0);
        r1 = 0;
        r2 = new java.io.RandomAccessFile;	 Catch:{ all -> 0x002a }
        r3 = "rw";
        r2.<init>(r5, r3);	 Catch:{ all -> 0x002a }
        r5 = r2.getChannel();	 Catch:{ all -> 0x0027 }
        r5.write(r4);	 Catch:{ all -> 0x0025 }
        r5.force(r0);	 Catch:{ all -> 0x0025 }
        r5.close();	 Catch:{ all -> 0x0025 }
        r2.close();	 Catch:{ all -> 0x0025 }
        if (r5 == 0) goto L_0x0021;
    L_0x001e:
        r5.close();	 Catch:{ IOException -> 0x0021 }
    L_0x0021:
        r2.close();	 Catch:{ IOException -> 0x0024 }
    L_0x0024:
        return;
    L_0x0025:
        r4 = move-exception;
        goto L_0x002d;
    L_0x0027:
        r4 = move-exception;
        r5 = r1;
        goto L_0x002d;
    L_0x002a:
        r4 = move-exception;
        r5 = r1;
        r2 = r5;
    L_0x002d:
        if (r5 == 0) goto L_0x0034;
    L_0x002f:
        r5.close();	 Catch:{ IOException -> 0x0033 }
        goto L_0x0034;
    L_0x0034:
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2.close();	 Catch:{ IOException -> 0x0039 }
    L_0x0039:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.util.ByteBufferUtil.toFile(java.nio.ByteBuffer, java.io.File):void");
    }

    public static InputStream toStream(ByteBuffer byteBuffer) {
        return new ByteBufferStream(byteBuffer);
    }
}
