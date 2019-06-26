// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.os.Build$VERSION;
import android.text.TextUtils;
import android.media.audiofx.AudioEffect$Descriptor;
import java.util.regex.Pattern;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.NoiseSuppressor;
import java.nio.ByteBuffer;
import android.media.AudioRecord;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.AcousticEchoCanceler;

public class AudioRecordJNI
{
    private AcousticEchoCanceler aec;
    private AutomaticGainControl agc;
    private AudioRecord audioRecord;
    private ByteBuffer buffer;
    private int bufferSize;
    private long nativeInst;
    private boolean needResampling;
    private NoiseSuppressor ns;
    private boolean running;
    private Thread thread;
    
    public AudioRecordJNI(final long nativeInst) {
        this.needResampling = false;
        this.nativeInst = nativeInst;
    }
    
    private int getBufferSize(final int b, final int n) {
        return Math.max(AudioRecord.getMinBufferSize(n, 16, 2), b);
    }
    
    private static boolean isGoodAudioEffect(final AudioEffect audioEffect) {
        final Pattern nonEmptyRegex = makeNonEmptyRegex("adsp_good_impls");
        final Pattern nonEmptyRegex2 = makeNonEmptyRegex("adsp_good_names");
        final AudioEffect$Descriptor descriptor = audioEffect.getDescriptor();
        final StringBuilder sb = new StringBuilder();
        sb.append(audioEffect.getClass().getSimpleName());
        sb.append(": implementor=");
        sb.append(descriptor.implementor);
        sb.append(", name=");
        sb.append(descriptor.name);
        VLog.d(sb.toString());
        if (nonEmptyRegex != null && nonEmptyRegex.matcher(descriptor.implementor).find()) {
            return true;
        }
        if (nonEmptyRegex2 != null && nonEmptyRegex2.matcher(descriptor.name).find()) {
            return true;
        }
        if (audioEffect instanceof AcousticEchoCanceler) {
            final Pattern nonEmptyRegex3 = makeNonEmptyRegex("aaec_good_impls");
            final Pattern nonEmptyRegex4 = makeNonEmptyRegex("aaec_good_names");
            if (nonEmptyRegex3 != null && nonEmptyRegex3.matcher(descriptor.implementor).find()) {
                return true;
            }
            if (nonEmptyRegex4 != null && nonEmptyRegex4.matcher(descriptor.name).find()) {
                return true;
            }
        }
        if (audioEffect instanceof NoiseSuppressor) {
            final Pattern nonEmptyRegex5 = makeNonEmptyRegex("ans_good_impls");
            final Pattern nonEmptyRegex6 = makeNonEmptyRegex("ans_good_names");
            if (nonEmptyRegex5 != null && nonEmptyRegex5.matcher(descriptor.implementor).find()) {
                return true;
            }
            if (nonEmptyRegex6 != null && nonEmptyRegex6.matcher(descriptor.name).find()) {
                return true;
            }
        }
        return false;
    }
    
    private static Pattern makeNonEmptyRegex(String string) {
        string = VoIPServerConfig.getString(string, "");
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        try {
            return Pattern.compile(string);
        }
        catch (Exception ex) {
            VLog.e(ex);
            return null;
        }
    }
    
    private native void nativeCallback(final ByteBuffer p0);
    
    private void startThread() {
        if (this.thread == null) {
            this.running = true;
            ByteBuffer allocateDirect;
            if (this.needResampling) {
                allocateDirect = ByteBuffer.allocateDirect(1764);
            }
            else {
                allocateDirect = null;
            }
            (this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (AudioRecordJNI.this.running) {
                        try {
                            if (!AudioRecordJNI.this.needResampling) {
                                AudioRecordJNI.this.audioRecord.read(AudioRecordJNI.this.buffer, 1920);
                            }
                            else {
                                AudioRecordJNI.this.audioRecord.read(allocateDirect, 1764);
                                Resampler.convert44to48(allocateDirect, AudioRecordJNI.this.buffer);
                            }
                            if (!AudioRecordJNI.this.running) {
                                AudioRecordJNI.this.audioRecord.stop();
                                break;
                            }
                            AudioRecordJNI.this.nativeCallback(AudioRecordJNI.this.buffer);
                        }
                        catch (Exception ex) {
                            VLog.e(ex);
                        }
                    }
                    VLog.i("audiorecord thread exits");
                }
            })).start();
            return;
        }
        throw new IllegalStateException("thread already started");
    }
    
    private boolean tryInit(final int i, final int j) {
        final AudioRecord audioRecord = this.audioRecord;
        while (true) {
            if (audioRecord == null) {
                break Label_0013;
            }
            try {
                audioRecord.release();
                final StringBuilder sb = new StringBuilder();
                sb.append("Trying to initialize AudioRecord with source=");
                sb.append(i);
                sb.append(" and sample rate=");
                sb.append(j);
                VLog.i(sb.toString());
                final int bufferSize = this.getBufferSize(this.bufferSize, 48000);
                try {
                    this.audioRecord = new AudioRecord(i, j, 16, 2, bufferSize);
                }
                catch (Exception ex) {
                    VLog.e("AudioRecord init failed!", ex);
                }
                final boolean b = false;
                this.needResampling = (j != 48000);
                final AudioRecord audioRecord2 = this.audioRecord;
                boolean b2 = b;
                if (audioRecord2 != null) {
                    b2 = b;
                    if (audioRecord2.getState() == 1) {
                        b2 = true;
                    }
                }
                return b2;
            }
            catch (Exception ex2) {
                continue;
            }
            break;
        }
    }
    
    public int getEnabledEffectsMask() {
        final AcousticEchoCanceler aec = this.aec;
        final boolean b = aec != null && aec.getEnabled();
        final NoiseSuppressor ns = this.ns;
        int n = b ? 1 : 0;
        if (ns != null) {
            n = (b ? 1 : 0);
            if (ns.getEnabled()) {
                n = ((b ? 1 : 0) | 0x2);
            }
        }
        return n;
    }
    
    public void init(final int n, final int n2, final int n3, final int n4) {
        if (this.audioRecord != null) {
            throw new IllegalStateException("already inited");
        }
        this.bufferSize = n4;
        boolean b;
        if (!(b = this.tryInit(7, 48000))) {
            b = this.tryInit(1, 48000);
        }
        boolean tryInit = b;
        if (!b) {
            tryInit = this.tryInit(7, 44100);
        }
        boolean tryInit2 = tryInit;
        if (!tryInit) {
            tryInit2 = this.tryInit(1, 44100);
        }
        if (!tryInit2) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            final boolean b2 = false;
            try {
                if (AutomaticGainControl.isAvailable()) {
                    this.agc = AutomaticGainControl.create(this.audioRecord.getAudioSessionId());
                    if (this.agc != null) {
                        this.agc.setEnabled(false);
                    }
                }
                else {
                    VLog.w("AutomaticGainControl is not available on this device :(");
                }
            }
            catch (Throwable t) {
                VLog.e("error creating AutomaticGainControl", t);
            }
            try {
                if (NoiseSuppressor.isAvailable()) {
                    this.ns = NoiseSuppressor.create(this.audioRecord.getAudioSessionId());
                    if (this.ns != null) {
                        this.ns.setEnabled(VoIPServerConfig.getBoolean("use_system_ns", true) && isGoodAudioEffect((AudioEffect)this.ns));
                    }
                }
                else {
                    VLog.w("NoiseSuppressor is not available on this device :(");
                }
            }
            catch (Throwable t2) {
                VLog.e("error creating NoiseSuppressor", t2);
            }
            try {
                if (AcousticEchoCanceler.isAvailable()) {
                    this.aec = AcousticEchoCanceler.create(this.audioRecord.getAudioSessionId());
                    if (this.aec != null) {
                        final AcousticEchoCanceler aec = this.aec;
                        boolean enabled = b2;
                        if (VoIPServerConfig.getBoolean("use_system_aec", true)) {
                            enabled = b2;
                            if (isGoodAudioEffect((AudioEffect)this.aec)) {
                                enabled = true;
                            }
                        }
                        aec.setEnabled(enabled);
                    }
                }
                else {
                    VLog.w("AcousticEchoCanceler is not available on this device");
                }
            }
            catch (Throwable t3) {
                VLog.e("error creating AcousticEchoCanceler", t3);
            }
        }
        this.buffer = ByteBuffer.allocateDirect(n4);
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
        final AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.release();
            this.audioRecord = null;
        }
        final AutomaticGainControl agc = this.agc;
        if (agc != null) {
            agc.release();
            this.agc = null;
        }
        final NoiseSuppressor ns = this.ns;
        if (ns != null) {
            ns.release();
            this.ns = null;
        }
        final AcousticEchoCanceler aec = this.aec;
        if (aec != null) {
            aec.release();
            this.aec = null;
        }
    }
    
    public boolean start() {
        final AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                try {
                    if (this.thread == null) {
                        if (this.audioRecord == null) {
                            return false;
                        }
                        this.audioRecord.startRecording();
                        this.startThread();
                    }
                    else {
                        this.audioRecord.startRecording();
                    }
                    return true;
                }
                catch (Exception ex) {
                    VLog.e("Error initializing AudioRecord", ex);
                }
            }
        }
        return false;
    }
    
    public void stop() {
        try {
            if (this.audioRecord != null) {
                this.audioRecord.stop();
            }
        }
        catch (Exception ex) {}
    }
}
