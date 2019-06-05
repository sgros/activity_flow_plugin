package p008pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.p000v4.internal.view.SupportMenu;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* renamed from: pl.droidsonroids.gif.GifInfoHandle */
final class GifInfoHandle {
    private volatile long gifInfoPtr;

    private static native void bindSurface(long j, Surface surface, long[] jArr);

    private static native void free(long j);

    private static native long getAllocationByteCount(long j);

    private static native String getComment(long j);

    private static native int getCurrentFrameIndex(long j);

    private static native int getCurrentLoop(long j);

    private static native int getCurrentPosition(long j);

    private static native int getDuration(long j);

    private static native int getFrameDuration(long j, int i);

    private static native int getHeight(long j);

    private static native int getLoopCount(long j);

    private static native long getMetadataByteCount(long j);

    private static native int getNativeErrorCode(long j);

    private static native int getNumberOfFrames(long j);

    private static native long[] getSavedState(long j);

    private static native long getSourceLength(long j);

    private static native int getWidth(long j);

    private static native void glTexImage2D(long j, int i, int i2);

    private static native void glTexSubImage2D(long j, int i, int i2);

    private static native void initTexImageDescriptor(long j);

    private static native boolean isAnimationCompleted(long j);

    private static native boolean isOpaque(long j);

    static native long openByteArray(byte[] bArr) throws GifIOException;

    static native long openDirectByteBuffer(ByteBuffer byteBuffer) throws GifIOException;

    static native long openFd(FileDescriptor fileDescriptor, long j) throws GifIOException;

    static native long openFile(String str) throws GifIOException;

    static native long openStream(InputStream inputStream) throws GifIOException;

    private static native void postUnbindSurface(long j);

    private static native long renderFrame(long j, Bitmap bitmap);

    private static native boolean reset(long j);

    private static native long restoreRemainder(long j);

    private static native int restoreSavedState(long j, long[] jArr, Bitmap bitmap);

    private static native void saveRemainder(long j);

    private static native void seekToFrame(long j, int i, Bitmap bitmap);

    private static native void seekToFrameGL(long j, int i);

    private static native void seekToTime(long j, int i, Bitmap bitmap);

    private static native void setLoopCount(long j, char c);

    private static native void setOptions(long j, char c, boolean z);

    private static native void setSpeedFactor(long j, float f);

    private static native void startDecoderThread(long j);

    private static native void stopDecoderThread(long j);

    static {
        LibraryLoader.loadLibrary(null);
    }

    GifInfoHandle() {
    }

    GifInfoHandle(FileDescriptor fd) throws GifIOException {
        this.gifInfoPtr = GifInfoHandle.openFd(fd, 0);
    }

    GifInfoHandle(byte[] bytes) throws GifIOException {
        this.gifInfoPtr = GifInfoHandle.openByteArray(bytes);
    }

    GifInfoHandle(ByteBuffer buffer) throws GifIOException {
        this.gifInfoPtr = GifInfoHandle.openDirectByteBuffer(buffer);
    }

    GifInfoHandle(String filePath) throws GifIOException {
        this.gifInfoPtr = GifInfoHandle.openFile(filePath);
    }

    GifInfoHandle(InputStream stream) throws GifIOException {
        if (stream.markSupported()) {
            this.gifInfoPtr = GifInfoHandle.openStream(stream);
            return;
        }
        throw new IllegalArgumentException("InputStream does not support marking");
    }

    GifInfoHandle(AssetFileDescriptor afd) throws IOException {
        try {
            this.gifInfoPtr = GifInfoHandle.openFd(afd.getFileDescriptor(), afd.getStartOffset());
        } finally {
            try {
                afd.close();
            } catch (IOException e) {
            }
        }
    }

    static GifInfoHandle openUri(ContentResolver resolver, Uri uri) throws IOException {
        if ("file".equals(uri.getScheme())) {
            return new GifInfoHandle(uri.getPath());
        }
        return new GifInfoHandle(resolver.openAssetFileDescriptor(uri, "r"));
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long renderFrame(Bitmap frameBuffer) {
        return GifInfoHandle.renderFrame(this.gifInfoPtr, frameBuffer);
    }

    /* Access modifiers changed, original: 0000 */
    public void bindSurface(Surface surface, long[] savedState) {
        GifInfoHandle.bindSurface(this.gifInfoPtr, surface, savedState);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void recycle() {
        GifInfoHandle.free(this.gifInfoPtr);
        this.gifInfoPtr = 0;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long restoreRemainder() {
        return GifInfoHandle.restoreRemainder(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized boolean reset() {
        return GifInfoHandle.reset(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void saveRemainder() {
        GifInfoHandle.saveRemainder(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized String getComment() {
        return GifInfoHandle.getComment(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getLoopCount() {
        return GifInfoHandle.getLoopCount(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void setLoopCount(@IntRange(from = 0, to = 65535) int loopCount) {
        if (loopCount < 0 || loopCount > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("Loop count of range <0, 65535>");
        }
        synchronized (this) {
            GifInfoHandle.setLoopCount(this.gifInfoPtr, (char) loopCount);
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long getSourceLength() {
        return GifInfoHandle.getSourceLength(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getNativeErrorCode() {
        return GifInfoHandle.getNativeErrorCode(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void setSpeedFactor(@FloatRange(from = 0.0d, fromInclusive = false) float factor) {
        if (factor <= 0.0f || Float.isNaN(factor)) {
            throw new IllegalArgumentException("Speed factor is not positive");
        }
        if (factor < 4.656613E-10f) {
            factor = 4.656613E-10f;
        }
        synchronized (this) {
            GifInfoHandle.setSpeedFactor(this.gifInfoPtr, factor);
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getDuration() {
        return GifInfoHandle.getDuration(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getCurrentPosition() {
        return GifInfoHandle.getCurrentPosition(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getCurrentFrameIndex() {
        return GifInfoHandle.getCurrentFrameIndex(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getCurrentLoop() {
        return GifInfoHandle.getCurrentLoop(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void seekToTime(@IntRange(from = 0, to = 2147483647L) int position, Bitmap buffer) {
        GifInfoHandle.seekToTime(this.gifInfoPtr, position, buffer);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void seekToFrame(@IntRange(from = 0, to = 2147483647L) int frameIndex, Bitmap buffer) {
        GifInfoHandle.seekToFrame(this.gifInfoPtr, frameIndex, buffer);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long getAllocationByteCount() {
        return GifInfoHandle.getAllocationByteCount(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long getMetadataByteCount() {
        return GifInfoHandle.getMetadataByteCount(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized boolean isRecycled() {
        return this.gifInfoPtr == 0;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void postUnbindSurface() {
        GifInfoHandle.postUnbindSurface(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized boolean isAnimationCompleted() {
        return GifInfoHandle.isAnimationCompleted(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized long[] getSavedState() {
        return GifInfoHandle.getSavedState(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int restoreSavedState(long[] savedState, Bitmap mBuffer) {
        return GifInfoHandle.restoreSavedState(this.gifInfoPtr, savedState, mBuffer);
    }

    /* Access modifiers changed, original: 0000 */
    public int getFrameDuration(@IntRange(from = 0) int index) {
        int frameDuration;
        synchronized (this) {
            if (index >= 0) {
                if (index < GifInfoHandle.getNumberOfFrames(this.gifInfoPtr)) {
                    frameDuration = GifInfoHandle.getFrameDuration(this.gifInfoPtr, index);
                }
            }
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        return frameDuration;
    }

    /* Access modifiers changed, original: 0000 */
    public void setOptions(char sampleSize, boolean isOpaque) {
        GifInfoHandle.setOptions(this.gifInfoPtr, sampleSize, isOpaque);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getWidth() {
        return GifInfoHandle.getWidth(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getHeight() {
        return GifInfoHandle.getHeight(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized int getNumberOfFrames() {
        return GifInfoHandle.getNumberOfFrames(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized boolean isOpaque() {
        return GifInfoHandle.isOpaque(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void glTexImage2D(int target, int level) {
        GifInfoHandle.glTexImage2D(this.gifInfoPtr, target, level);
    }

    /* Access modifiers changed, original: 0000 */
    public void glTexSubImage2D(int target, int level) {
        GifInfoHandle.glTexSubImage2D(this.gifInfoPtr, target, level);
    }

    /* Access modifiers changed, original: 0000 */
    public void startDecoderThread() {
        GifInfoHandle.startDecoderThread(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void stopDecoderThread() {
        GifInfoHandle.stopDecoderThread(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void initTexImageDescriptor() {
        GifInfoHandle.initTexImageDescriptor(this.gifInfoPtr);
    }

    /* Access modifiers changed, original: 0000 */
    public void seekToFrameGL(@IntRange(from = 0) int index) {
        if (index < 0 || index >= GifInfoHandle.getNumberOfFrames(this.gifInfoPtr)) {
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        GifInfoHandle.seekToFrameGL(this.gifInfoPtr, index);
    }
}
