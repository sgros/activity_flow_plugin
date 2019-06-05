// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.ContentResolver;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import android.content.res.AssetFileDescriptor;
import android.content.Context;

final class GifInfoHandle
{
    private volatile long gifInfoPtr;
    
    static {
        LibraryLoader.loadLibrary(null);
    }
    
    GifInfoHandle() {
    }
    
    GifInfoHandle(final AssetFileDescriptor p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     4: aload_0        
        //     5: aload_1        
        //     6: invokevirtual   android/content/res/AssetFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
        //     9: aload_1        
        //    10: invokevirtual   android/content/res/AssetFileDescriptor.getStartOffset:()J
        //    13: invokestatic    pl/droidsonroids/gif/GifInfoHandle.openFd:(Ljava/io/FileDescriptor;J)J
        //    16: putfield        pl/droidsonroids/gif/GifInfoHandle.gifInfoPtr:J
        //    19: aload_1        
        //    20: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //    23: return         
        //    24: astore_2       
        //    25: aload_1        
        //    26: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //    29: aload_2        
        //    30: athrow         
        //    31: astore_1       
        //    32: goto            23
        //    35: astore_1       
        //    36: goto            29
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      19     24     31     Any
        //  19     23     31     35     Ljava/io/IOException;
        //  25     29     35     39     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 21 out-of-bounds for length 21
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    GifInfoHandle(final FileDescriptor fileDescriptor) throws GifIOException {
        this.gifInfoPtr = openFd(fileDescriptor, 0L);
    }
    
    GifInfoHandle(final InputStream inputStream) throws GifIOException {
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("InputStream does not support marking");
        }
        this.gifInfoPtr = openStream(inputStream);
    }
    
    GifInfoHandle(final String s) throws GifIOException {
        this.gifInfoPtr = openFile(s);
    }
    
    GifInfoHandle(final ByteBuffer byteBuffer) throws GifIOException {
        this.gifInfoPtr = openDirectByteBuffer(byteBuffer);
    }
    
    GifInfoHandle(final byte[] array) throws GifIOException {
        this.gifInfoPtr = openByteArray(array);
    }
    
    private static native void bindSurface(final long p0, final Surface p1, final long[] p2);
    
    private static native void free(final long p0);
    
    private static native long getAllocationByteCount(final long p0);
    
    private static native String getComment(final long p0);
    
    private static native int getCurrentFrameIndex(final long p0);
    
    private static native int getCurrentLoop(final long p0);
    
    private static native int getCurrentPosition(final long p0);
    
    private static native int getDuration(final long p0);
    
    private static native int getFrameDuration(final long p0, final int p1);
    
    private static native int getHeight(final long p0);
    
    private static native int getLoopCount(final long p0);
    
    private static native long getMetadataByteCount(final long p0);
    
    private static native int getNativeErrorCode(final long p0);
    
    private static native int getNumberOfFrames(final long p0);
    
    private static native long[] getSavedState(final long p0);
    
    private static native long getSourceLength(final long p0);
    
    private static native int getWidth(final long p0);
    
    private static native void glTexImage2D(final long p0, final int p1, final int p2);
    
    private static native void glTexSubImage2D(final long p0, final int p1, final int p2);
    
    private static native void initTexImageDescriptor(final long p0);
    
    private static native boolean isAnimationCompleted(final long p0);
    
    private static native boolean isOpaque(final long p0);
    
    static native long openByteArray(final byte[] p0) throws GifIOException;
    
    static native long openDirectByteBuffer(final ByteBuffer p0) throws GifIOException;
    
    static native long openFd(final FileDescriptor p0, final long p1) throws GifIOException;
    
    static native long openFile(final String p0) throws GifIOException;
    
    static native long openStream(final InputStream p0) throws GifIOException;
    
    static GifInfoHandle openUri(final ContentResolver contentResolver, final Uri uri) throws IOException {
        GifInfoHandle gifInfoHandle;
        if ("file".equals(uri.getScheme())) {
            gifInfoHandle = new GifInfoHandle(uri.getPath());
        }
        else {
            gifInfoHandle = new GifInfoHandle(contentResolver.openAssetFileDescriptor(uri, "r"));
        }
        return gifInfoHandle;
    }
    
    private static native void postUnbindSurface(final long p0);
    
    private static native long renderFrame(final long p0, final Bitmap p1);
    
    private static native boolean reset(final long p0);
    
    private static native long restoreRemainder(final long p0);
    
    private static native int restoreSavedState(final long p0, final long[] p1, final Bitmap p2);
    
    private static native void saveRemainder(final long p0);
    
    private static native void seekToFrame(final long p0, final int p1, final Bitmap p2);
    
    private static native void seekToFrameGL(final long p0, final int p1);
    
    private static native void seekToTime(final long p0, final int p1, final Bitmap p2);
    
    private static native void setLoopCount(final long p0, final char p1);
    
    private static native void setOptions(final long p0, final char p1, final boolean p2);
    
    private static native void setSpeedFactor(final long p0, final float p1);
    
    private static native void startDecoderThread(final long p0);
    
    private static native void stopDecoderThread(final long p0);
    
    void bindSurface(final Surface surface, final long[] array) {
        bindSurface(this.gifInfoPtr, surface, array);
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.recycle();
        }
        finally {
            super.finalize();
        }
    }
    
    long getAllocationByteCount() {
        synchronized (this) {
            return getAllocationByteCount(this.gifInfoPtr);
        }
    }
    
    String getComment() {
        synchronized (this) {
            return getComment(this.gifInfoPtr);
        }
    }
    
    int getCurrentFrameIndex() {
        synchronized (this) {
            return getCurrentFrameIndex(this.gifInfoPtr);
        }
    }
    
    int getCurrentLoop() {
        synchronized (this) {
            return getCurrentLoop(this.gifInfoPtr);
        }
    }
    
    int getCurrentPosition() {
        synchronized (this) {
            return getCurrentPosition(this.gifInfoPtr);
        }
    }
    
    int getDuration() {
        synchronized (this) {
            return getDuration(this.gifInfoPtr);
        }
    }
    
    int getFrameDuration(@IntRange(from = 0L) int frameDuration) {
        // monitorenter(this)
        while (true) {
            if (frameDuration >= 0) {
                try {
                    if (frameDuration >= getNumberOfFrames(this.gifInfoPtr)) {
                        throw new IndexOutOfBoundsException("Frame index is out of bounds");
                    }
                }
                finally {
                }
                // monitorexit(this)
                frameDuration = getFrameDuration(this.gifInfoPtr, frameDuration);
                // monitorexit(this)
                return frameDuration;
            }
            continue;
        }
    }
    
    int getHeight() {
        synchronized (this) {
            return getHeight(this.gifInfoPtr);
        }
    }
    
    int getLoopCount() {
        synchronized (this) {
            return getLoopCount(this.gifInfoPtr);
        }
    }
    
    long getMetadataByteCount() {
        synchronized (this) {
            return getMetadataByteCount(this.gifInfoPtr);
        }
    }
    
    int getNativeErrorCode() {
        synchronized (this) {
            return getNativeErrorCode(this.gifInfoPtr);
        }
    }
    
    int getNumberOfFrames() {
        synchronized (this) {
            return getNumberOfFrames(this.gifInfoPtr);
        }
    }
    
    long[] getSavedState() {
        synchronized (this) {
            return getSavedState(this.gifInfoPtr);
        }
    }
    
    long getSourceLength() {
        synchronized (this) {
            return getSourceLength(this.gifInfoPtr);
        }
    }
    
    int getWidth() {
        synchronized (this) {
            return getWidth(this.gifInfoPtr);
        }
    }
    
    void glTexImage2D(final int n, final int n2) {
        glTexImage2D(this.gifInfoPtr, n, n2);
    }
    
    void glTexSubImage2D(final int n, final int n2) {
        glTexSubImage2D(this.gifInfoPtr, n, n2);
    }
    
    void initTexImageDescriptor() {
        initTexImageDescriptor(this.gifInfoPtr);
    }
    
    boolean isAnimationCompleted() {
        synchronized (this) {
            return isAnimationCompleted(this.gifInfoPtr);
        }
    }
    
    boolean isOpaque() {
        synchronized (this) {
            return isOpaque(this.gifInfoPtr);
        }
    }
    
    boolean isRecycled() {
        synchronized (this) {
            return this.gifInfoPtr == 0L;
        }
    }
    
    void postUnbindSurface() {
        synchronized (this) {
            postUnbindSurface(this.gifInfoPtr);
        }
    }
    
    void recycle() {
        synchronized (this) {
            free(this.gifInfoPtr);
            this.gifInfoPtr = 0L;
        }
    }
    
    long renderFrame(final Bitmap bitmap) {
        synchronized (this) {
            return renderFrame(this.gifInfoPtr, bitmap);
        }
    }
    
    boolean reset() {
        synchronized (this) {
            return reset(this.gifInfoPtr);
        }
    }
    
    long restoreRemainder() {
        synchronized (this) {
            return restoreRemainder(this.gifInfoPtr);
        }
    }
    
    int restoreSavedState(final long[] array, final Bitmap bitmap) {
        synchronized (this) {
            return restoreSavedState(this.gifInfoPtr, array, bitmap);
        }
    }
    
    void saveRemainder() {
        synchronized (this) {
            saveRemainder(this.gifInfoPtr);
        }
    }
    
    void seekToFrame(@IntRange(from = 0L, to = 2147483647L) final int n, final Bitmap bitmap) {
        synchronized (this) {
            seekToFrame(this.gifInfoPtr, n, bitmap);
        }
    }
    
    void seekToFrameGL(@IntRange(from = 0L) final int n) {
        if (n < 0 || n >= getNumberOfFrames(this.gifInfoPtr)) {
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        seekToFrameGL(this.gifInfoPtr, n);
    }
    
    void seekToTime(@IntRange(from = 0L, to = 2147483647L) final int n, final Bitmap bitmap) {
        synchronized (this) {
            seekToTime(this.gifInfoPtr, n, bitmap);
        }
    }
    
    void setLoopCount(@IntRange(from = 0L, to = 65535L) final int n) {
        if (n < 0 || n > 65535) {
            throw new IllegalArgumentException("Loop count of range <0, 65535>");
        }
        synchronized (this) {
            setLoopCount(this.gifInfoPtr, (char)n);
        }
    }
    
    void setOptions(final char c, final boolean b) {
        setOptions(this.gifInfoPtr, c, b);
    }
    
    void setSpeedFactor(@FloatRange(from = 0.0, fromInclusive = false) final float v) {
        if (v <= 0.0f || Float.isNaN(v)) {
            throw new IllegalArgumentException("Speed factor is not positive");
        }
        float n = v;
        if (v < 4.656613E-10f) {
            n = 4.656613E-10f;
        }
        synchronized (this) {
            setSpeedFactor(this.gifInfoPtr, n);
        }
    }
    
    void startDecoderThread() {
        startDecoderThread(this.gifInfoPtr);
    }
    
    void stopDecoderThread() {
        stopDecoderThread(this.gifInfoPtr);
    }
}
