// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import java.nio.ByteBuffer;
import android.media.AudioTrack;

public class AudioTrackJNI
{
    private AudioTrack audioTrack;
    private byte[] buffer;
    private int bufferSize;
    private long nativeInst;
    private boolean needResampling;
    private boolean running;
    private Thread thread;
    
    public AudioTrackJNI(final long nativeInst) {
        this.buffer = new byte[1920];
        this.nativeInst = nativeInst;
    }
    
    private int getBufferSize(final int b, final int n) {
        return Math.max(AudioTrack.getMinBufferSize(n, 4, 2), b);
    }
    
    private native void nativeCallback(final byte[] p0);
    
    private void startThread() {
        if (this.thread == null) {
            this.running = true;
            (this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AudioTrackJNI.this.audioTrack.play();
                        final boolean access$100 = AudioTrackJNI.this.needResampling;
                        ByteBuffer allocateDirect = null;
                        ByteBuffer allocateDirect2;
                        if (access$100) {
                            allocateDirect2 = ByteBuffer.allocateDirect(1920);
                        }
                        else {
                            allocateDirect2 = null;
                        }
                        if (AudioTrackJNI.this.needResampling) {
                            allocateDirect = ByteBuffer.allocateDirect(1764);
                        }
                        while (AudioTrackJNI.this.running) {
                            try {
                                if (AudioTrackJNI.this.needResampling) {
                                    AudioTrackJNI.this.nativeCallback(AudioTrackJNI.this.buffer);
                                    allocateDirect2.rewind();
                                    allocateDirect2.put(AudioTrackJNI.this.buffer);
                                    Resampler.convert48to44(allocateDirect2, allocateDirect);
                                    allocateDirect.rewind();
                                    allocateDirect.get(AudioTrackJNI.this.buffer, 0, 1764);
                                    AudioTrackJNI.this.audioTrack.write(AudioTrackJNI.this.buffer, 0, 1764);
                                }
                                else {
                                    AudioTrackJNI.this.nativeCallback(AudioTrackJNI.this.buffer);
                                    AudioTrackJNI.this.audioTrack.write(AudioTrackJNI.this.buffer, 0, 1920);
                                }
                                if (AudioTrackJNI.this.running) {
                                    continue;
                                }
                                AudioTrackJNI.this.audioTrack.stop();
                            }
                            catch (Exception ex) {
                                VLog.e(ex);
                                continue;
                            }
                            break;
                        }
                        VLog.i("audiotrack thread exits");
                    }
                    catch (Exception ex2) {
                        VLog.e("error starting AudioTrack", ex2);
                    }
                }
            })).start();
            return;
        }
        throw new IllegalStateException("thread already started");
    }
    
    public void init(int n, int i, final int n2, final int bufferSize) {
        Label_0157: {
            if (this.audioTrack != null) {
                break Label_0157;
            }
            i = this.getBufferSize(bufferSize, 48000);
            this.bufferSize = bufferSize;
            if (n2 == 1) {
                n = 4;
            }
            else {
                n = 12;
            }
            this.audioTrack = new AudioTrack(0, 48000, n, 2, i, 1);
            if (this.audioTrack.getState() == 1) {
                return;
            }
            VLog.w("Error initializing AudioTrack with 48k, trying 44.1k with resampling");
            while (true) {
                try {
                    this.audioTrack.release();
                    i = this.getBufferSize(bufferSize * 6, 44100);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("buffer size: ");
                    sb.append(i);
                    VLog.d(sb.toString());
                    if (n2 == 1) {
                        n = 4;
                    }
                    else {
                        n = 12;
                    }
                    this.audioTrack = new AudioTrack(0, 44100, n, 2, i, 1);
                    this.needResampling = true;
                    return;
                    throw new IllegalStateException("already inited");
                }
                catch (Throwable t) {
                    continue;
                }
                break;
            }
        }
    }
    
    public void release() {
        this.running = false;
        final Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.join();
            }
            catch (InterruptedException ex) {
                VLog.e(ex);
            }
            this.thread = null;
        }
        final AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this.audioTrack = null;
        }
    }
    
    public void start() {
        if (this.thread == null) {
            this.startThread();
        }
        else {
            this.audioTrack.play();
        }
    }
    
    public void stop() {
        final AudioTrack audioTrack = this.audioTrack;
        if (audioTrack == null) {
            return;
        }
        try {
            audioTrack.stop();
        }
        catch (Exception ex) {}
    }
}
